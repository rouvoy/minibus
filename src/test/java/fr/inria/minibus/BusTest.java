package fr.inria.minibus;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.inria.minibus.Listener;
import fr.inria.minibus.Subscribe;
import fr.inria.minibus.lib.MiniBus;

public class BusTest {
	public static final class Event {
		public String label;
		public int number;
		
		public Event(String l, int n) {
			this.label = l;
			this.number = n;
		}
		
		public String toString() {
			return "<"+label+","+number+">";
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
		@Subscribe(matchingStringFilter)
		void onEvent(Event e);
	}

	public static final class OtherEvent {
		public String label = "another label";
	}

	protected static final String matchingStringFilter = "label=event";
	protected static final String unmatchingStringFilter = "label=none";
	protected static final String exactMatchingIntFilter = "number=10";
	protected static final String moreThanMatchingIntFilter = "number>5";
	protected static final String lessThanMatchingIntFilter = "number<15";
	protected static final String unmatchingIntFilter = "number=5";
	protected static final String unknownFilter = "name=Doe";

	@Mock
	protected Listener<Event,Void> listener;
	@Mock
	protected Listener<Event,Void> anotherListener;
	@Mock
	protected Listener<OtherEvent,Void> otherListener;

	@Mock
	protected EventHandler correctHandler;
	@Mock
	protected FilteredEventHandler filteredHandler;

	protected MiniBus miniBus;

	@Before
	public void setup() {
		miniBus = new MiniBus(1);
		MockitoAnnotations.initMocks(this);
	}
}