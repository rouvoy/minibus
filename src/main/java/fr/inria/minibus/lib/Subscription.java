package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.minibus.Listener;

public class Subscription<E> {

	private final Listener<E> listener;

	public Subscription(Listener<E> listener) {
		this.listener = listener;
	}

	public static final class NotifyTask<E> implements Runnable {
		private E event;
		private Listener<E> listener2;

		public NotifyTask(E event, Listener<E> listener) {
			this.event = event;
			listener2 = listener;
		}

		public void run() {
			listener2.notify(event);
		}
	}

	public void apply(E event, Executor executor) {
		try {
			executor.execute(new NotifyTask<E>(event, listener));
		} catch (Exception e) {
			PublicationException.forward(e);
		}
	}
}
