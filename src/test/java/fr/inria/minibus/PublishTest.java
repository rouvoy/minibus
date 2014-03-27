package fr.inria.minibus;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.MockitoAnnotations;

import fr.inria.minibus.lib.MiniBus;

public class PublishTest extends BusTest {
	protected final int poolSize;
	protected Event event;

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
		miniBus = new MiniBus(this.poolSize);
		MockitoAnnotations.initMocks(this);
		event = new Event("event",10);
	}
}