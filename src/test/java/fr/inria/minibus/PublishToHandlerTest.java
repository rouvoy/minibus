package fr.inria.minibus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

/**
 * Unit test for an event bus.
 */
@RunWith(Parameterized.class)
public class PublishToHandlerTest extends PublishTest {
	public PublishToHandlerTest(int poolSize) {
		super(poolSize);
	}
	
	@Test
	public void publishMatchingEventToHandler() throws InterruptedException {
		miniBus.subscribe(this.correctHandler);
		miniBus.publish(event);
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verify(this.correctHandler).onEvent(event);
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
		miniBus.publish(new Event("wrong",0));
		Thread.sleep(100);
		miniBus.shutdown(100);
		Mockito.verifyZeroInteractions(this.filteredHandler);
	}
	
}
