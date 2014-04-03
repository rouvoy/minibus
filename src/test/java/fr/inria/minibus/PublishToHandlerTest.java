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
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Unit test for an event bus.
 */
@RunWith(Parameterized.class)
public class PublishToHandlerTest extends PublishTest {
	public PublishToHandlerTest(int poolSize) {
		super(poolSize);
	}

	public interface PingPong {
		@Subscribe
		String ping(Event e);

		@Subscribe
		void pong(String msg);
	}

	@Mock
	protected PingPong pp;

	@Test
	public void publishMatchingEventToHandler() throws InterruptedException {
		miniBus.subscribe(this.correctHandler);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(this.correctHandler).onEvent(event);
	}

	@Test
	public void publishFeedbackEventToHandler() throws InterruptedException {
		Mockito.when(this.pp.ping(event)).thenReturn("Hello");
		miniBus.subscribe(this.pp);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(this.pp).ping(event);
		Mockito.verify(this.pp).pong("Hello");
	}

	@Test
	public void publishMatchingEventToFilteredHandler()
			throws InterruptedException {
		miniBus.subscribe(this.filteredHandler);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(this.filteredHandler).onEvent(event);
	}
	
	@Test
	public void unsubscribedEventShouldNotBeDelivered() throws InterruptedException {
		Subscription s = miniBus.subscribe(this.correctHandler);
		s.unsubscribe();
		miniBus.publish(event);
		Mockito.verifyZeroInteractions(this.correctHandler);
	}

	@Test
	public void publishUnMatchingEventToFilteredHandler()
			throws InterruptedException {
		miniBus.subscribe(this.filteredHandler);
		miniBus.publish(new OtherEvent());
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(this.filteredHandler);
	}

	@Test
	public void publishUnMatchingEventToUnFilteredHandler()
			throws InterruptedException {
		miniBus.subscribe(this.filteredHandler);
		miniBus.publish(new Event("wrong", 0));
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(this.filteredHandler);
	}

}
