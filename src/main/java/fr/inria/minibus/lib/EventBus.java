package fr.inria.minibus.lib;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.inria.jfilter.FilterException;
import fr.inria.minibus.Bus;
import fr.inria.minibus.Listener;
import fr.inria.minibus.Subscribe;

/**
 * 
 */
public class EventBus implements Bus {
	private final Subscribers subscribers = new Subscribers();
	private final ExecutorService execution;

	public EventBus(int poolSize) {
		this.execution = Executors.newFixedThreadPool(poolSize);
	}

	public <E> void subscribe(Class<E> eventType, Listener<E> listener) {
		SubscriptionException.checkNotNull(eventType,
				"null event types are not supported");
		SubscriptionException.checkNotNull(listener,
				"empty listener are not supported");
		subscribers.add(eventType, new Subscription<E>(listener));
	}

	public <E> void subscribe(Class<E> eventType, String filter,
			Listener<E> listener) {
		SubscriptionException.checkNotNull(eventType,
				"null event types are not supported");
		SubscriptionException.checkNotNull(filter,
				"null filter are not supported");
		SubscriptionException.checkNotNull(listener,
				"empty listener are not supported");
		try {
			subscribers.add(eventType, new FilterSubscription<E>(filter,
					listener));
		} catch (FilterException e) {
			SubscriptionException.forward(e);
		}
	}

	public final class PublishTask<E> implements Runnable {
		private final E event;

		public PublishTask(E event) {
			this.event = event;
		}

		@SuppressWarnings("unchecked")
		public void run() {
			for (Subscription<?> s : subscribers.get(event.getClass())) {
				((Subscription<E>) s).apply(event, execution);
			}
		}
	}

	public <E> void publish(E event) {
		PublicationException.checkNotNull(event,
				"null events are not supported");
		execution.execute(new PublishTask<E>(event));
	}

	public void shutdown(int time) throws InterruptedException {
		this.execution.shutdown();
		this.execution.awaitTermination(time, TimeUnit.MILLISECONDS);
	}

	public void subscribe(Object subscriber) {
		Class<?> type = subscriber.getClass();
		boolean found = subscribe(subscriber, type);
		for (Class<?> implemented : type.getInterfaces())
			found |= subscribe(subscriber, implemented);
		if (!found)
			SubscriptionException.raise("No method handler found for "
					+ subscriber);
	}

	private boolean subscribe(Object subscriber, Class<?> type) {
		boolean found = false;
		for (Method m : type.getDeclaredMethods()) {
			Subscribe annotation = m.getAnnotation(Subscribe.class);
			if (annotation != null) {
				subscribe(subscriber, m, annotation);
				found = true;
			}
		}
		return found;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void subscribe(Object subscriber, Method m, Subscribe annotation) {
		Class<?>[] parameters = m.getParameterTypes();
		if (parameters.length != 1)
			SubscriptionException.raise("Method \'" + m.getName()
					+ "\' should have a single parameter");
		if ("".equals(annotation.value()))
			subscribe(parameters[0], new MethodListener(subscriber, m));
		else
			subscribe(parameters[0], annotation.value(), new MethodListener(
					subscriber, m));
	}
}
