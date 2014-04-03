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
package fr.inria.minibus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
 import fr.inria.minibus.lib.PublicationException;

/**
 * Unit test for an event bus.
 */
@RunWith(Parameterized.class)
public class PublishToListenerTest extends PublishTest {
	public PublishToListenerTest(int poolSize) {
		super(poolSize);
	}

	@Test(expected = PublicationException.class)
	public void publishNullEventIsForbidden() {
		miniBus.publish(null);
	}

	@Test
	public void publishOtherEventShouldNotBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, listener);
		miniBus.publish(new OtherEvent());
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishEventShouldBeDelivered() throws InterruptedException {
		miniBus.subscribe(Event.class, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void unsubscribedEventShouldNotBeDelivered() throws InterruptedException {
		Subscription s = miniBus.subscribe(Event.class, listener);
		s.unsubscribe();
		miniBus.publish(event);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishMultipleEventsShouldBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, listener);
		final int nb = 100000;
		for (int i = 0; i < nb; i++)
			miniBus.publish(event);
		Thread.sleep(400);
		miniBus.shutdown((int) (nb / (5 * Math.log10(1 + poolSize))));
		Mockito.verify(listener, Mockito.times(nb)).notify(event);
	}

	@Test
	public void publishEventShouldBeDeliveredTwice()
			throws InterruptedException {
		miniBus.subscribe(Event.class, listener);
		miniBus.subscribe(Event.class, anotherListener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
		Mockito.verify(anotherListener).notify(event);
	}

	@Test
	public void publishUnknownFilterShouldNotBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, unknownFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishUnmatchingStringFilterShouldNotBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, unmatchingStringFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishMatchingStringEventShouldBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, matchingStringFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishUnmatchingIntFilterShouldNotBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, unmatchingIntFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishMatchingIntEventShouldBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, exactMatchingIntFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMatchingMoreThanIntEventShouldBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, moreThanMatchingIntFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMatchingLessThanIntEventShouldBeDelivered()
			throws InterruptedException {
		miniBus.subscribe(Event.class, lessThanMatchingIntFilter, listener);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}
}
