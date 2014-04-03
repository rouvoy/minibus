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
