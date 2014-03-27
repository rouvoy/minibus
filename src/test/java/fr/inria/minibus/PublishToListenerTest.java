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
