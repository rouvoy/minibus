package fr.inria.minibus;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.inria.minibus.Listener;
import fr.inria.minibus.Subscribe;
import fr.inria.minibus.lib.EventBus;

public class BusTest {
	public static final class Event {
		public String label = "event";
		public int number = 10;

		@Override
		public String toString() {
			return "Event(" + label + "," + number + ")";
		}
	}

	public interface IncorrectEventHandler {
		@Subscribe
		void onEvent(Event e, int value);
	}

	public interface EventHandler {
		@Subscribe
		void onEvent(Event e);
	}

	public interface FilteredEventHandler {
		@Subscribe("label=event")
		void onEvent(Event e);
	}

	public static final class OtherEvent {
		String label = "another label";
	}

	protected static final String matchingStringFilter = "label=event";
	protected static final String unmatchingStringFilter = "label=none";
	protected static final String exactMatchingIntFilter = "number=10";
	protected static final String moreThanMatchingIntFilter = "number>5";
	protected static final String lessThanMatchingIntFilter = "number<15";
	protected static final String unmatchingIntFilter = "number=5";
	protected static final String unknownFilter = "name=Doe";

	@Mock
	protected Listener<Event> listener;
	@Mock
	protected Listener<Event> anotherListener;
	@Mock
	protected Listener<OtherEvent> otherListener;

	@Mock
	protected IncorrectEventHandler incorrectHandler;
	@Mock
	protected EventHandler correctHandler;
	@Mock
	protected FilteredEventHandler filteredHandler;

	protected EventBus eventBus;

	@Before
	public void setup() {
		eventBus = new EventBus(1);
		MockitoAnnotations.initMocks(this);
	}
}