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

package org.testeditor.fixture.core.utils;

import org.junit.Test;
import org.testeditor.fixture.core.exceptions.ElementKeyNotFoundException;
import org.testeditor.fixture.core.exceptions.StopTestException;

/**
 * Tests for the {@link ExceptionUtils}.
 */
public class ExceptionUtilsTest {

	/**
	 * Tests the handleElementKeyNotFoundException.
	 */
	@Test(expected = StopTestException.class)
	public void testHandleNoSuchElementException() {
		ExceptionUtils.handleNoSuchElementException("testLocator", new Exception());
	}

	/**
	 * Tests the handleElementKeyNotFoundException.
	 */
	@Test(expected = StopTestException.class)
	public void testHandleElementKeyNotFoundException() {
		ExceptionUtils.handleElementKeyNotFoundException("testKey", new ElementKeyNotFoundException("testKey"));
	}

}
