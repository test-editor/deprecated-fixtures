/*******************************************************************************
 * Copyright (c) 2012 - 2015 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 *******************************************************************************/
package org.testeditor.fixture.core.elementlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;
import org.testeditor.fixture.core.exceptions.ElementKeyNotFoundException;

/**
 * Tests the {@link ElementListService}.
 */
public class ElementListServiceInstanceTest {

	/**
	 * Tests instanceFor with a wrong element list path.
	 */
	@Test
	public void testWrongFilePath() {
		ElementListService.instanceFor("wrong/path");
	}

	/**
	 * Tests the handling with more than one element list.
	 * 
	 * @throws ElementKeyNotFoundException
	 *             if key not found
	 */
	@Test
	public void testMultipleServiceInstances() throws ElementKeyNotFoundException {
		// Test first element list
		ElementListService list1 = ElementListService.instanceFor("src/test/resources/ElementList1.conf");
		assertEquals("password", list1.getValue("p"));
		assertEquals("username", list1.getValue("u"));

		// initialization of second element list
		ElementListService list2 = ElementListService.instanceFor("src/test/resources/ElementList2.conf");
		assertEquals("gender", list2.getValue("g"));

		try {
			list2.getValue("u");
			fail("Working with wrong element list");
		} catch (ElementKeyNotFoundException e) {
			// Expected not to find keys of the first list
			assertTrue(true);
		}

		// After initialization of second element list, first works just fine
		assertEquals("username", list1.getValue("u"));
	}

	/**
	 * Tests the correct instantiation of the service singletons.
	 * 
	 * @throws NoSuchFieldException
	 *             cause by java reflection error
	 * @throws SecurityException
	 *             cause by java reflection error
	 * @throws IllegalArgumentException
	 *             cause by java reflection error
	 * @throws IllegalAccessException
	 *             cause by java reflection error
	 */
	@Test
	public void testServiceInitialization() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException {

		// retrieve the instances field
		Field instancesField = null;
		for (Field f : ElementListService.class.getDeclaredFields()) {
			if ("instances".equals(f.getName())) {
				instancesField = f;
				break;
			}
		}

		// expected to find field variable
		assertNotNull("instances filed not found", instancesField);

		instancesField.setAccessible(true);
		Map<?, ?> m = (Map<?, ?>) instancesField.get(Map.class);

		// clear current instance map
		m.clear();

		ElementListService.instanceFor("src/test/resources/ElementList1.conf");
		ElementListService.instanceFor("src/test/../test/resources/ElementList1.conf");
		ElementListService.instanceFor("src/test/resources/ElementList2.conf");
		ElementListService.instanceFor("src/test/resources/ElementList1.conf");

		// two instances are expected
		assertEquals(2, m.size());
	}
}
