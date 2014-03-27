package fr.inria.minibus;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.inria.minibus.lib.EventBus;
import fr.inria.minibus.lib.PublicationException;

/**
 * Unit test for an event bus.
 */
@RunWith(Parameterized.class)
public class PublishTest extends BusTest {
	private final int poolSize;
	private Event event;

	@Parameters
	public static Collection<Object[]> poolSize() {
		Object[][] data = new Object[][] { { 1 }, { 10 }, { 100 }, { 1000 } };
		return Arrays.asList(data);
	}

	public PublishTest(int poolSize) {
		this.poolSize = poolSize;

	}

	@Before
	public void setup() {
		eventBus = new EventBus(this.poolSize);
		MockitoAnnotations.initMocks(this);
		event = new Event();
	}

	@Test(expected = PublicationException.class)
	public void publishNullEventIsForbidden() {
		eventBus.publish(null);
	}

	@Test
	public void publishOtherEventShouldNotBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, listener);
		eventBus.publish(new OtherEvent());
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishEventShouldBeDelivered() throws InterruptedException {
		eventBus.subscribe(Event.class, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMultipleEventsShouldBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, listener);
		final int nb = 100000;
		for (int i = 0; i < nb; i++)
			eventBus.publish(event);
		Thread.sleep(400);
		eventBus.shutdown((int) (nb / (5 * Math.log10(1 + poolSize))));
		Mockito.verify(listener, Mockito.times(nb)).notify(event);
	}

	@Test
	public void publishEventShouldBeDeliveredTwice()
			throws InterruptedException {
		eventBus.subscribe(Event.class, listener);
		eventBus.subscribe(Event.class, anotherListener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(listener).notify(event);
		Mockito.verify(anotherListener).notify(event);
	}

	@Test
	public void publishUnknownFilterShouldNotBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, unknownFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishUnmatchingStringFilterShouldNotBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, unmatchingStringFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishMatchingStringEventShouldBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, matchingStringFilter, listener);
		eventBus.publish(event);
		Thread.sleep(200);
		eventBus.shutdown(200);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishUnmatchingIntFilterShouldNotBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, unmatchingIntFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verifyZeroInteractions(listener);
	}

	@Test
	public void publishMatchingIntEventShouldBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, exactMatchingIntFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMatchingMoreThanIntEventShouldBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, moreThanMatchingIntFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMatchingLessThanIntEventShouldBeDelivered()
			throws InterruptedException {
		eventBus.subscribe(Event.class, lessThanMatchingIntFilter, listener);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(listener).notify(event);
	}

	@Test
	public void publishMatchingEventToHandler() throws InterruptedException {
		eventBus.subscribe(this.correctHandler);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(this.correctHandler).onEvent(event);
	}

	@Test
	public void publishMatchingEventToFilteredHandler()
			throws InterruptedException {
		eventBus.subscribe(this.filteredHandler);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(this.filteredHandler).onEvent(event);
	}

	@Test
	public void publishUnMatchingEventToFilteredHandler()
			throws InterruptedException {
		eventBus.subscribe(this.filteredHandler);
		eventBus.publish(event);
		Thread.sleep(100);
		eventBus.shutdown(100);
		Mockito.verify(this.filteredHandler).onEvent(event);
	}
}
