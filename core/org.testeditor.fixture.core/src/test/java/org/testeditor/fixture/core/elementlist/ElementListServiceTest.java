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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testeditor.fixture.core.exceptions.ElementKeyNotFoundException;

/**
 * Tests the {@link ElementListService}.
 */
public class ElementListServiceTest {
	private static ElementListService elementListService;

	/**
	 * Initializes the test.
	 */
	@BeforeClass
	public static void init() {
		elementListService = ElementListService.instanceFor("src/test/resources/elementListContent.txt");
	}

	/**
	 * Test the getValue with a valid value.
	 * 
	 * @throws ElementKeyNotFoundException
	 *             shouldn't be thrown
	 */
	@Test
	public void valueFound1() throws ElementKeyNotFoundException {
		String value = elementListService.getValue("x");
		Assert.assertEquals("username", value);
	}

	/**
	 * Test the getValue with a valid value.
	 * 
	 * @throws ElementKeyNotFoundException
	 *             shouldn't be thrown
	 */
	@Test
	public void valueFound2() throws ElementKeyNotFoundException {
		String value = elementListService.getValue("y");
		Assert.assertEquals("//div[contains(@class, 'styleid-HeaderComponent-password')]", value);
	}

	/**
	 * Test the getValue with an invalid value.
	 * 
	 * @throws ElementKeyNotFoundException
	 *             should be thrown
	 */
	@Test(expected = ElementKeyNotFoundException.class)
	public void valueNotFound() throws ElementKeyNotFoundException {
		String value = elementListService.getValue("z");
		Assert.fail("exception wasn't thrown, value: " + value);
	}
}
