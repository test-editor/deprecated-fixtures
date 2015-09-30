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

import org.apache.log4j.Logger;

/**
 * General exception which stops the FitNesse test and shows an error message to
 * the end-user. The name of the class must contain "StopTest" because of the
 * FitNesse syntax.
 */
public class StopTestException extends RuntimeException {

	private static final long serialVersionUID = 2142474544753902692L;
	private static final Logger LOGGER = Logger.getLogger(StopTestException.class);

	/**
	 * Initializes the exception.
	 * 
	 * @param message
	 *            error message
	 */
	public StopTestException(String message) {
		this(message, null);
	}

	/**
	 * Initializes the exception.
	 * 
	 * @param message
	 *            error message
	 * @param cause
	 *            cause
	 */
	public StopTestException(String message, Throwable cause) {
		super("message:<<" + ExceptionMessageHelper.cleanMessage(message) + ">>", cause);
	}

	/**
	 * Initializes the exception.
	 * 
	 * @param throwable
	 *            original exception
	 */
	public StopTestException(Throwable throwable) {
		super("message:<<" + ExceptionMessageHelper.cleanMessage(throwable.getMessage()) + ">>", throwable);
	}

	public String getCleanMessage() {
		LOGGER.debug("call to getClanMessage");
		if (getMessage().length() > "message:<<>>".length()) {
			LOGGER.debug("call to clean message '"
					+ getMessage().substring("message:<<".length(), getMessage().length() - 2) + "'");
			return getMessage().substring("message:<<".length(), getMessage().length() - 2);
		}
		return getMessage();
	}
}
