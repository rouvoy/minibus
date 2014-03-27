package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.minibus.Listener;
import fr.inria.minibus.Publisher;

public class Subscription<E, R> {
	private final Listener<E, R> listener;
	private final Publisher publisher;

	public Subscription(Listener<E, R> l, Publisher p) {
		this.listener = l;
		this.publisher = p;
	}

	public static final class NotifyTask<E, R> implements Runnable {
		private final E evt;
		private final Listener<E, R> listen;
		private final Publisher pub;

		public NotifyTask(E e, Listener<E, R> l, Publisher p) {
			this.evt = e;
			listen = l;
			this.pub = p;
		}

		public void run() {
			Object val = listen.notify(evt);
			if (val != null)
				this.pub.publish(val);
		}
	}

	public void apply(E event, Executor executor) {
		for (int i = 0; i < 10; i++)
			// Try 10 times if it fails
			try {
				executor.execute(new NotifyTask<E, R>(event, listener,
						publisher));
				return;
			} catch (Exception e) {
				PublicationException.forward(e);
			}
	}
}
