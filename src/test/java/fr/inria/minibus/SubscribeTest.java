package fr.inria.minibus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import fr.inria.minibus.lib.SubscriptionException;

/**
 * Unit test for an event bus.
 */
@RunWith(JUnit4.class)
public class SubscribeTest extends BusTest {
	@Mock
	protected IncorrectEventHandler incorrectHandler;
	
	
	@Test(expected = SubscriptionException.class)
	public void subscribeNullListenerIsForbidden() {
		miniBus.subscribe(Event.class, null);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeNullEventIsForbidden() {
		miniBus.subscribe(null, listener);
	}

	@Test
	public void subscribeListenerShouldBeCorrect() {
		miniBus.subscribe(Event.class, listener).unsubscribe();
	}

	@Test
	public void subscribeMultipleListenersShouldBeCorrect() {
		miniBus.subscribe(Event.class, listener);
		miniBus.subscribe(Event.class, anotherListener);
	}

	@Test
	public void subscribeMultipleEventsShouldBeCorrect() {
		miniBus.subscribe(Event.class, listener);
		miniBus.subscribe(OtherEvent.class, otherListener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullListenerIsForbidden() {
		miniBus.subscribe(Event.class, matchingStringFilter, null);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullEventIsForbidden() {
		miniBus.subscribe(null, matchingStringFilter, listener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeWithFilterNullFilterIsForbidden() {
		miniBus.subscribe(Event.class, null, listener);
	}

	@Test
	public void subscribeFilteredListenerShouldBeCorrect() {
		miniBus.subscribe(Event.class, matchingStringFilter, listener).unsubscribe();
	}

	@Test
	public void subscribeFilteredMultipleListenersShouldBeCorrect() {
		miniBus.subscribe(Event.class, matchingStringFilter, listener);
		miniBus.subscribe(Event.class, matchingStringFilter, anotherListener);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeIncorrectHandlerisForbidden() {
		miniBus.subscribe(this.incorrectHandler);
	}

	@Test(expected = SubscriptionException.class)
	public void subscribeEmptyHandlerisForbidden() {
		miniBus.subscribe(this.listener);
	}

	@Test
	public void subscribeHandlerShouldBeCorrect() {
		miniBus.subscribe(this.correctHandler).unsubscribe();
	}

	@Test
	public void subscribeFilteredHandlerShouldBeCorrect() {
		miniBus.subscribe(this.filteredHandler).unsubscribe();
	}

	@Test
	public void subscribeMultipleHandlersShouldBeCorrect() {
		miniBus.subscribe(this.correctHandler);
		miniBus.subscribe(this.filteredHandler);
	}
}
