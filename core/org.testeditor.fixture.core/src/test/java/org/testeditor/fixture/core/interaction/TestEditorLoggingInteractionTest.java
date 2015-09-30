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

import static org.junit.Assert.assertEquals;

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

	/**
	 * Test the creation of the time to wait from configuration property.
	 * 
	 */
	@Test
	public void testGetWaitTimeSeconds() {
		TestEditorLoggingInteraction loggingInteraction = new TestEditorLoggingInteraction();
		System.getProperties().remove(TestEditorLoggingInteraction.WAIT_PROPERTY);
		assertEquals("Expect 0 second", 0, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "1");
		assertEquals("Expect 1 second", 1000, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "10");
		assertEquals("Expect 10 seconds", 10000, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "3");
		assertEquals("Expect 3 seconds", 3000, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "0");
		assertEquals("Expect 0 seconds", 0, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "hello");
		assertEquals("Expect 0 seconds", 0, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "3s");
		assertEquals("Expect 3 seconds", 3000, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "3 s");
		assertEquals("Expect 3 seconds", 3000, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "300 ms");
		assertEquals("Expect 300 milliseconds", 300, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "300ms");
		assertEquals("Expect 300 milliseconds", 300, loggingInteraction.getTimeToWait());
		System.setProperty(TestEditorLoggingInteraction.WAIT_PROPERTY, "20ms");
		assertEquals("Expect 300 milliseconds", 20, loggingInteraction.getTimeToWait());
	}

}
