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

import org.apache.log4j.Logger;
import org.testeditor.fixture.core.exceptions.ElementKeyNotFoundException;
import org.testeditor.fixture.core.exceptions.StopTestException;

/**
 * Provides mechanisms to throw general exception messages.
 */
public final class ExceptionUtils {
	private static final Logger LOGGER = Logger.getLogger(ExceptionUtils.class);

	/**
	 * Don't create any objects of this util class.
	 */
	private ExceptionUtils() {
	}

	/**
	 * Throws a StopTestException for the case that the locator doesn't exists
	 * at the AUT.
	 * 
	 * @param locator
	 *            technical locator
	 * @param exception
	 *            exception
	 */
	public static void handleNoSuchElementException(String locator, Exception exception) {
		String message = "The specified Gui-Element \"" + locator + "\" could not be found on Gui-Interface!";
		LOGGER.error(message, exception);

		throw new StopTestException(message);
	}

	/**
	 * Throws a StopTestException for the case that the key wasn't found at the
	 * element list.
	 * 
	 * @param elementListKey
	 *            element list key
	 * @param exception
	 *            exception
	 */
	public static void handleElementKeyNotFoundException(String elementListKey, ElementKeyNotFoundException exception) {
		String message = "The specified Key for the Gui-Element \"" + elementListKey + "\" could not be found!";
		LOGGER.error(message, exception);

		throw new StopTestException(message);
	}
}
