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

package org.testeditor.fixture.core.exceptions;

/**
 * This class provides methods preparing ExceptionMessages for FitNesse.
 * 
 */
public final class ExceptionMessageHelper {

	/**
	 * Class can not be instantiated.
	 */
	private ExceptionMessageHelper() {
	}

	/**
	 * This method returns a clean message, e.g. no new lines
	 * 
	 * @param message
	 *            the original message
	 * @return the cleaned message
	 */
	public static String cleanMessage(String message) {
		String result = message;
		if (message.contains("\n")) {
			int indexOfNewLine = message.indexOf("\n");
			result = result.substring(0, indexOfNewLine);
		}
		return result;
	}
}
