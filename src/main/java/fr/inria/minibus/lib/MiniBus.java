package fr.inria.minibus.lib;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.inria.jfilter.FilterException;
import fr.inria.minibus.Bus;
import fr.inria.minibus.Listener;
import fr.inria.minibus.Subscribe;
import fr.inria.minibus.Subscription;

/**
 * Core implementation of the bus.
 */
public class MiniBus implements Bus {
	private final Subscribers subscribers = new Subscribers();
	private final ExecutorService execution;

	public MiniBus(int poolSize) {
		this.execution = Executors.newFixedThreadPool(poolSize);
	}

	public <E, R> Subscription subscribe(Class<E> eventType,
			Listener<E, R> listener) {
		SubscriptionException.checkNotNull(eventType,
				"null event types are not supported");
		SubscriptionException.checkNotNull(listener,
				"empty listener are not supported");
		DefaultSubscription<E, R> s = new DefaultSubscription<E, R>(listener,
				this, subscribers, eventType);
		subscribers.add(eventType, s);
		return s;
	}

	public <E, R> Subscription subscribe(Class<E> eventType, String filter,
			Listener<E, R> listener) {
		SubscriptionException.checkNotNull(eventType,
				"null event types are not supported");
		SubscriptionException.checkNotNull(filter,
				"null filter are not supported");
		SubscriptionException.checkNotNull(listener,
				"empty listener are not supported");
		try {
			FilterSubscription<E, R> s = new FilterSubscription<E, R>(filter,
					listener, this, subscribers, eventType);
			subscribers.add(eventType, s);
			return s;
		} catch (FilterException e) {
			SubscriptionException.forward(e);
		}
		return null;
	}

	public final class PublishTask<E> implements Runnable {
		private final E event;

		public PublishTask(E event) {
			this.event = event;
		}

		@SuppressWarnings("unchecked")
		public void run() {
			for (DefaultSubscription<?, ?> s : subscribers
					.get(event.getClass())) {
				((DefaultSubscription<E, ?>) s).apply(event, execution);
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

	public Subscription subscribe(Object subscriber) {
		Class<?> type = subscriber.getClass();
		GroupSubscription s = new GroupSubscription();
		subscribe(subscriber, type, s);
		for (Class<?> implemented : type.getInterfaces())
			subscribe(subscriber, implemented, s);
		if (s.isEmpty())
			SubscriptionException.raise("No method handler found in "
					+ subscriber);
		return s;
	}

	private void subscribe(Object subscriber, Class<?> type,
			GroupSubscription subscriptions) {
		for (Method m : type.getDeclaredMethods()) {
			Subscribe annotation = m.getAnnotation(Subscribe.class);
			if (annotation != null)
				subscriptions.add(subscribe(subscriber, m, annotation));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Subscription subscribe(Object subscriber, Method m,
			Subscribe annotation) {
		Class<?>[] parameters = m.getParameterTypes();
		if (parameters.length != 1)
			SubscriptionException.raise("Method \'" + m.getName()
					+ "\' should have a single parameter");
		if ("".equals(annotation.value()))
			return subscribe(parameters[0], new MethodListener(subscriber, m));
		else
			return subscribe(parameters[0], annotation.value(),
					new MethodListener(subscriber, m));
	}
}
