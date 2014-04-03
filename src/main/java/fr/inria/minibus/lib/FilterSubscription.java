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

import fr.inria.jfilter.Filter;
import fr.inria.jfilter.FilterException;
import fr.inria.jfilter.FilterParser;
import fr.inria.minibus.Listener;

public class FilterSubscription<E, R> extends DefaultSubscription<E, R> {
	private final Filter filter;

	public FilterSubscription(String f, Listener<E, R> l, MiniBus p, Subscribers subscribers, Class<E> type)
			throws FilterException {
		super(l, p, subscribers, type);
		this.filter = FilterParser.instance.parse(f);
	}

	public void apply(E event, Executor executor) {
		if (filter.match(event))
			super.apply(event, executor);
	}
}
