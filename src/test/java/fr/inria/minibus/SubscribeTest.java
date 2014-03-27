package fr.inria.minibus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fr.inria.minibus.lib.SubscriptionException;

/**
 * Unit test for an event bus.
 */
@RunWith(JUnit4.class)
public class SubscribeTest extends BusTest {
	@Test(expected = SubscriptionException.class)
	public void subscribeNullListenerIsForbidden() {
		eventBus.subscribe(Event.class, null);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeNullEventIsForbidden() {
		eventBus.subscribe(null, listener);
	}

	@Test
	public void subscribeListenerShouldBeCorrect() {
		eventBus.subscribe(Event.class, listener);
	}

	@Test
	public void subscribeMultipleListenersShouldBeCorrect() {
		eventBus.subscribe(Event.class, listener);
		eventBus.subscribe(Event.class, anotherListener);
	}

	@Test
	public void subscribeMultipleEventsShouldBeCorrect() {
		eventBus.subscribe(Event.class, listener);
		eventBus.subscribe(OtherEvent.class, otherListener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullListenerIsForbidden() {
		eventBus.subscribe(Event.class, matchingStringFilter, null);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullEventIsForbidden() {
		eventBus.subscribe(null, matchingStringFilter, listener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullFilterIsForbidden() {
		eventBus.subscribe(Event.class, null, listener);
	}

	@Test
	public void subscribeFilteredListenerShouldBeCorrect() {
		eventBus.subscribe(Event.class, matchingStringFilter, listener);
	}

	@Test
	public void subscribeFilteredMultipleListenersShouldBeCorrect() {
		eventBus.subscribe(Event.class, matchingStringFilter, listener);
		eventBus.subscribe(Event.class, matchingStringFilter, anotherListener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeIncorrectHandlerisForbidden() {
		eventBus.subscribe(this.incorrectHandler);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeEmptyHandlerisForbidden() {
		eventBus.subscribe(this.listener);
	}

	@Test
	public void subscribeHandlerShouldBeCorrect() {
		eventBus.subscribe(this.correctHandler);
	}

	@Test
	public void subscribeFilteredHandlerShouldBeCorrect() {
		eventBus.subscribe(this.filteredHandler);
	}

	@Test
	public void subscribeMultipleHandlersShouldBeCorrect() {
		eventBus.subscribe(this.correctHandler);
		eventBus.subscribe(this.filteredHandler);
	}
}
