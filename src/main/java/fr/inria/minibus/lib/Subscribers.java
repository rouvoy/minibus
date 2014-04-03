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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author rouvoy
 *
 */
public class Subscribers {
	private final Map<Class<?>, Collection<DefaultSubscription<?,?>>> subscribers = new HashMap<Class<?>, Collection<DefaultSubscription<?,?>>>();

	/**
	 * @param eventType
	 * @param handler
	 * @param trigger
	 */
	public <E,R> void add(Class<E> eventType, DefaultSubscription<E,R> subscription) {
		Collection<DefaultSubscription<?,?>> handlers = get(eventType);
		handlers.add(subscription);
	}
	
	/**
	 * @param eventType
	 * @return
	 */
	public <E> Collection<DefaultSubscription<?,?>> get(Class<E> eventType) {
		Collection<DefaultSubscription<?,?>> subscriptions = this.subscribers.get(eventType);
		if (subscriptions == null) {
			subscriptions = new LinkedList<DefaultSubscription<?,?>>();
			this.subscribers.put(eventType, subscriptions);
		}
		return subscriptions;
	}
}
