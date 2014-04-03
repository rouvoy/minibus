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
