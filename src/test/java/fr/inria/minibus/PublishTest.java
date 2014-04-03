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