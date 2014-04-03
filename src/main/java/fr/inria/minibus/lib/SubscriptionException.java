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


/**
 * @author rouvoy
 * 
 */
public class SubscriptionException extends RuntimeException {
	private static final long serialVersionUID = -9158829764854871263L;

	private SubscriptionException(String cause) {
		super(cause);
	}

	private SubscriptionException(Throwable e) {
		super(e);
	}

	public static final void raise(String cause) {
		throw new SubscriptionException(cause);
	}

	public static final void checkNotNull(Object obj, String cause) {
		if (obj == null)
			raise(cause);
	}

	public static void forward(Throwable e) {
		throw new SubscriptionException(e);
	}
}
