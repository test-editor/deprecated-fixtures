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
 * Exception is thrown if a key inside the element list couldn't be found.
 */
public class ElementKeyNotFoundException extends Exception {
	private static final long serialVersionUID = -7479728422906822655L;
	private String key;

	/**
	 * Initializes the exception with the invalid key.
	 * 
	 * @param key
	 *            key
	 */
	public ElementKeyNotFoundException(String key) {
		this.key = key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage() {
		return "The Element with the name " + key + " could not be found";
	}
}
