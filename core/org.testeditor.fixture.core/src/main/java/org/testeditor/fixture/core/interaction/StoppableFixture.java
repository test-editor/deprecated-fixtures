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
package org.testeditor.fixture.core.interaction;

/**
 * Fixture classes implementing this interface are able to gracefully shutdown
 * the application under test, if a test is being abnormally terminated. This
 * may be the case when an unhandled exception occurs or the user manually
 * cancels test execution from within the TestEditor GUI.
 */
public interface StoppableFixture {
	/**
	 * Tries to gracefully shutdown the application under test, when the test is
	 * being stopped due to exceptions or the user manually canceling its
	 * execution.
	 * 
	 * @return <code>true</code> on success; <code>false</code> otherwise
	 */
	boolean tearDown();
}
