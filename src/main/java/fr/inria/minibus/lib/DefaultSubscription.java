/**
 * Copyright (c) 2014 Inria, University Lille 1.
 *
 * This file is part of Minibus.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 *
 * Contact: romain.rouvoy@univ-lille1.fr.
 */
package fr.inria.minibus.lib;

import java.util.concurrent.Executor;

import fr.inria.minibus.Listener;
import fr.inria.minibus.Publisher;
import fr.inria.minibus.Subscription;

public class DefaultSubscription<E, R> implements Subscription {
	private final Listener<E, R> listener;
	private final MiniBus bus;
	private final Subscribers subscribers;
	private final Class<E> type;

	public DefaultSubscription(Listener<E, R> l, MiniBus p, Subscribers s,
			Class<E> type) {
		this.listener = l;
		this.bus = p;
		this.subscribers = s;
		this.type = type;

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
				executor.execute(new NotifyTask<E, R>(event, listener, bus));
				return;
			} catch (Exception e) {
				PublicationException.forward(e);
			}
	}

	public void unsubscribe() {
		subscribers.get(type).remove(this);
	}
}
