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
/**
 * 
 */
package org.testeditor.fixture.core.interaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This interface represents methods for usage in behalf of performance
 * measuring and display this information on a Jenkins server.
 */
public interface Fixture {

	/**
	 * Returns the current name of running test.
	 * 
	 * @return running test name.
	 */
	String getTestName();

	/**
	 * Sets the test name for performance measuring.
	 * 
	 * @param testName
	 *            String
	 */
	void setTestName(String testName);

	/**
	 * This Method will be invoked before Fixture-Method will be invoked.
	 * 
	 * @param method
	 * @param instance
	 * @param convertedArgs
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	void preInvoke(Method method, Object instance, Object... convertedArgs) throws InvocationTargetException,
			IllegalAccessException;

	/**
	 * This Method will be invoked after Fixture-Method will be invoked.
	 * 
	 * @param method
	 * @param instance
	 * @param convertedArgs
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	void postInvoke(Method method, Object instance, Object... convertedArgs) throws InvocationTargetException,
			IllegalAccessException;

}
