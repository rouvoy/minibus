package fr.inria.minibus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;

import fr.inria.minibus.BusTest.Event;

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
