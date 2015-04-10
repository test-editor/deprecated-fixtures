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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.testeditor.fixture.core.exceptions.ContinueTestException;
import org.testeditor.fixture.core.exceptions.StopTestException;

import fitnesse.slim.fixtureInteraction.DefaultInteraction;

/**
 * Interaction for real-time test logging.<br />
 * 
 * FitNesse provides the option of user-specific interaction. We use this hook
 * to provide a test logging outside the default FitNesse logging. The
 * Test-Editor is using this logging while executing any tests.
 */
public class TestEditorLoggingInteraction extends DefaultInteraction {
	private static final Logger LOGGER = Logger.getLogger(TestEditorLoggingInteraction.class.getName());

	private static final String WAIT_PROPERTY = "waits.after.teststep";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object methodInvoke(Method method, Object instance, Object... convertedArgs) throws IllegalAccessException {

		Object result = null;

		String logMessage = createLogMessage(method, convertedArgs);

		try {

			// pre invoke
			preInvoke(method, instance, convertedArgs);

			// execute test step
			result = super.methodInvoke(method, instance, convertedArgs);

			// post invoke
			postInvoke(method, instance, convertedArgs);

			// wait after test step
			String waitsAfterTeststep = System.getProperty(WAIT_PROPERTY);
			if (waitsAfterTeststep != null) {
				try {
					int seconds = Integer.parseInt(waitsAfterTeststep);
					if (seconds > 0) {
						// property was set and is > 0, so wait
						waitTime(seconds);
						LOGGER.debug("Wait " + waitsAfterTeststep + " seconds");
					}
				} catch (NumberFormatException e) {
					LOGGER.error("Wrong value for " + WAIT_PROPERTY + ": " + waitsAfterTeststep);
				}
			}

			// analyze result
			if (result == null || !(result instanceof Boolean) || (Boolean) result) {
				LOGGER.debug(logMessage);
			} else {
				// only boolean results with "false" are logged on error level
				LOGGER.error(logMessage);
			}
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof StopTestException) {
				LOGGER.error(logMessage + e.getTargetException().getMessage());
				handleTearDown(instance);
				throw (StopTestException) e.getTargetException();
			} else if (e.getCause() instanceof StopTestException) {
				LOGGER.error(logMessage + e.getCause().getMessage());
				handleTearDown(instance);
				throw (StopTestException) e.getCause();
			} else if (e.getTargetException() instanceof ContinueTestException) {
				throw (ContinueTestException) e.getTargetException();
			} else if (e.getCause() instanceof ContinueTestException) {
				throw (ContinueTestException) e.getCause();
			} else {
				LOGGER.error(logMessage + e.getTargetException().getMessage());
				LOGGER.error(Arrays.toString(e.getTargetException().getStackTrace()));
				handleTearDown(instance);
				throw new StopTestException("An unexpected error occurred: " + e.getTargetException().getMessage());
			}
		} catch (Exception e) {
			LOGGER.error(logMessage + e.getMessage(), e);
			handleTearDown(instance);
			if (e instanceof StopTestException) {
				throw (StopTestException) e;
			} else if (e instanceof ContinueTestException) {
				throw (ContinueTestException) e;
			} else {
				throw new StopTestException("An unexpected error occurred: " + e.getMessage());
			}
		}

		return result;
	}

	/**
	 * Creates the Log-Message.
	 * 
	 * @param method
	 *            fixture method.
	 * @param convertedArgs
	 *            arguments of fixte method.
	 * @return created log message.
	 */
	private String createLogMessage(Method method, Object... convertedArgs) {
		String logMessage = "";
		StringBuilder argumentListAsStr = new StringBuilder();

		for (int i = 0; i < convertedArgs.length; i++) {

			argumentListAsStr.append(formatArg(convertedArgs[i]));

			if (i < (convertedArgs.length - 1)) {
				argumentListAsStr.append(", ");
			}
		}

		logMessage = "Method : " + method.getName() + " ( " + argumentListAsStr.toString() + " )";

		return logMessage;
	}

	/**
	 * @param method
	 * @param instance
	 * @param convertedArgs
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void postInvoke(Method method, Object instance, Object[] convertedArgs) throws InvocationTargetException,
			IllegalAccessException {

		if (instance instanceof Fixture) {

			((Fixture) instance).postInvoke(method, instance, convertedArgs);
		}

	}

	/**
	 * 
	 * @param method
	 * @param instance
	 * @param convertedArgs
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void preInvoke(Method method, Object instance, Object[] convertedArgs) throws InvocationTargetException,
			IllegalAccessException {

		if (instance instanceof Fixture) {

			((Fixture) instance).preInvoke(method, instance, convertedArgs);
		}

	}

	/**
	 * Tries to shut down the fixture class.
	 * 
	 * @param instance
	 *            instance of the fixture class being used to invoke the fixture
	 *            methods
	 * @return <code>true</code>, if the instance supports being stopped and did
	 *         so successfully; <code>false</code>, if instance is
	 *         <code>null</code>, instance does not implement interface
	 *         <code>StoppableFixture</code> or instance returns
	 *         <code>false</code> from method <code>tearDown()</code>.
	 */
	private boolean handleTearDown(Object instance) {
		boolean result = false;
		if (instance == null || !(instance instanceof StoppableFixture)) {
			return false;
		}
		StoppableFixture stoppableInstance = (StoppableFixture) instance;

		try {
			result = stoppableInstance.tearDown();
		} catch (Exception e) {
			String logMessage = "Method : StoppableFixture.tearDown()";
			LOGGER.error(logMessage + e.getMessage());
		}

		return result;
	}

	/**
	 * Formats a given argument for logging. This method is intended to handle
	 * arrays as theses would otherwise be logged as something like
	 * "[Ljava.lang.String;@62a98a".
	 * 
	 * @param arg
	 *            the argument to be formatted
	 * @return a string containing the formatted argument.
	 */
	private String formatArg(Object arg) {
		Object argToAppend;
		if (arg != null && arg.getClass().isArray()) {
			Object[] argArray = (Object[]) arg;
			argToAppend = Arrays.deepToString(argArray);
		} else {
			argToAppend = arg;
		}
		return "\"" + argToAppend + "\"";
	}

	/**
	 * Waits the given seconds.
	 * 
	 * @param seconds
	 *            seconds to wait
	 */
	private void waitTime(long seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
