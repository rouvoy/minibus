package fr.inria.minibus.lib;

import java.util.LinkedList;

import fr.inria.minibus.Subscription;

public class GroupSubscription implements Subscription {
	private final LinkedList<Subscription> subscriptions = new LinkedList<Subscription>();

	public void add(Subscription s) {
		this.subscriptions.add(s);
	}
	
	public boolean isEmpty() {
		return this.subscriptions.isEmpty();
	}

	public void unsubscribe() {
		for (Subscription s : subscriptions)
			s.unsubscribe();
	}
}
