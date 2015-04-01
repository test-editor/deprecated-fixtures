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

/**
 * Utilities for any String operations.
 */
public final class StringUtils {

	/**
	 * Don't create any objects of this util class.
	 */
	private StringUtils() {
	}

	/**
	 * Returns true if the given value is a digit, otherwise false.
	 * 
	 * @param value
	 *            value
	 * @return true if the value is a digit, otherwise false
	 * 
	 */
	public static boolean isDigit(String value) {
		boolean result = true;

		char[] charArray = value.toCharArray();

		for (char c : charArray) {
			if (!Character.isDigit(c)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Returns true if the given value is a x-path routine, otherwise false.
	 * 
	 * @param value
	 *            value
	 * @return true if x-path, otherwise false
	 */
	public static boolean isXPath(String value) {

		if (value != null && value.startsWith("//")) {
			return true;
		}

		return false;
	}
}
