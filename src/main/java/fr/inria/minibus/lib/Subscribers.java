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
	private final Map<Class<?>, Collection<Subscription<?,?>>> subscribers = new HashMap<Class<?>, Collection<Subscription<?,?>>>();

	/**
	 * @param eventType
	 * @param handler
	 * @param trigger
	 */
	public <E,R> void add(Class<E> eventType, Subscription<E,R> subscription) {
		Collection<Subscription<?,?>> handlers = get(eventType);
		handlers.add(subscription);
	}
	
	/**
	 * @param eventType
	 * @return
	 */
	public <E> Collection<Subscription<?,?>> get(Class<E> eventType) {
		Collection<Subscription<?,?>> subscriptions = this.subscribers.get(eventType);
		if (subscriptions == null) {
			subscriptions = new LinkedList<Subscription<?,?>>();
			this.subscribers.put(eventType, subscriptions);
		}
		return subscriptions;
	}
}
