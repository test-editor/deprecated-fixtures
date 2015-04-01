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
 * Common exception which does not implicit stop the Fitnesse tests but shows an
 * error message to the user.
 * 
 */
public class ContinueTestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Initializes the exception.
	 * 
	 * @param message
	 *            error message
	 */
	public ContinueTestException(String message) {
		super("message:<<" + ExceptionMessageHelper.cleanMessage(message) + ">>");

	}

	/**
	 * Initializes the exception.
	 * 
	 * @param throwable
	 *            original exception
	 */
	public ContinueTestException(Throwable throwable) {
		super("message:<<" + ExceptionMessageHelper.cleanMessage(throwable.getMessage()) + ">>", throwable);

	}

}
