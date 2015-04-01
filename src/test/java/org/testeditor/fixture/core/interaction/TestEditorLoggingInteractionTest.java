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

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * Tests the {@link TestEditorLoggingInteraction}.
 */
public class TestEditorLoggingInteractionTest {

	/**
	 * Test class for any tests.
	 */
	protected class TestClass {
		/**
		 * Just a dummy method.
		 * 
		 * @return dummy return value
		 */
		public String getTestMessage() {
			return "test";
		}

		/**
		 * Just a dummy method.
		 * 
		 * @param test
		 *            dummy param
		 * @return dummy return value
		 */
		public String getTestMessageWithArg(String test) {
			return "test";
		}
	}

	/**
	 * Tests the interaction with a method without any arguments.
	 * 
	 * @throws Exception
	 *             should be thrown
	 */
	@Test
	public void testMethodWithoutArgs() throws Exception {
		Method method = TestClass.class.getMethod("getTestMessage", new Class[] {});
		new TestEditorLoggingInteraction().methodInvoke(method, new TestClass());
	}

	/**
	 * Tests the interaction with a method with an arguments.
	 * 
	 * @throws Exception
	 *             should be thrown
	 */
	@Test
	public void testMethodWithArgs() throws Exception {
		Method method = TestClass.class.getMethod("getTestMessageWithArg", new Class[] { String.class });
		new TestEditorLoggingInteraction().methodInvoke(method, new TestClass(), "test it");
	}
}
