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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.testeditor.fixture.core.exceptions.ContinueTestException;
import org.testeditor.fixture.core.exceptions.StopTestException;

import fitnesse.slim.fixtureInteraction.DefaultInteraction;

/**
 * Interaction for real-time test logging.<br>
 * 
 * FitNesse provides the option of user-specific interaction. We use this hook
 * to provide a test logging outside the default FitNesse logging. The
 * Test-Editor is using this logging while executing any tests.
 */
public class TestEditorLoggingInteraction extends DefaultInteraction {
	private static final Logger logger = Logger.getLogger(TestEditorLoggingInteraction.class.getName());

	public static final String WAIT_PROPERTY = "waits.after.teststep";

	static private Map<String, List<StoppableFixture>> stoppableFixtures = new TreeMap<String, List<StoppableFixture>>();
	static private List<StoppableFixture> stoppableFixturesList = new ArrayList<StoppableFixture>();

	public TestEditorLoggingInteraction() {
		super();
	}

	@Override
	public Object newInstance(Constructor<?> constructor, Object... initargs)
			throws InvocationTargetException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		Object object = null;
		if (Modifier.isAbstract(constructor.getDeclaringClass().getModifiers())) {
			Class<?> clazz;
			Constructor<?> newConstructor;
			String className = "de.signaliduna.testautomation.nlv.fixture.NlvFixtureElanImpl";
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new StopTestException(
						"Die Klasse '" + className + "' kann nicht geladen werden. Nachricht" + e.getMessage(), e);
			}
			try {
				newConstructor = clazz.getConstructor();
			} catch (NoSuchMethodException | SecurityException e) {
				throw new StopTestException("Der Standardconstructor für die Klasse '" + className
						+ "' kann nicht erzeugt werden. Nachricht" + e.getMessage(), e);
			}
			object = super.newInstance(newConstructor, initargs);
		} else {
			object = super.newInstance(constructor, initargs);
		}

		if (object instanceof StoppableFixture) {
			String name = object.getClass().getName();
			if (!stoppableFixtures.containsKey(name)) {
				stoppableFixtures.put(name, new ArrayList<StoppableFixture>());
			}
			stoppableFixtures.get(name).add((StoppableFixture) object);
			stoppableFixturesList.add((StoppableFixture) object);
		}

		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object methodInvoke(Method method, Object instance, Object... convertedArgs) throws IllegalAccessException {

		logger.info(
				"call to methodInvoke for method " + method.getName() + " for class " + instance.getClass().getName());
		Object result = null;

		String logMessage = createLogMessage(method, convertedArgs);

		try {

			// pre invoke
			preInvoke(method, instance, convertedArgs);

			// execute test step
			result = super.methodInvoke(method, instance, convertedArgs);
			logger.debug("result for result: " + result);

			// post invoke
			postInvoke(method, instance, convertedArgs);

			// wait after test step
			int timeToWait = getTimeToWait();
			if (timeToWait > 0) {
				// property was set and is > 0, so wait
				waitTime(timeToWait);
				logger.debug("Wait " + timeToWait + " mili seconds");
			}

			// analyze result
			if (result == null || !(result instanceof Boolean) || (Boolean) result) {
				logger.debug(logMessage);
			} else {
				// only boolean results with "false" are logged on error level
				logger.error(logMessage);
			}
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof StopTestException) {
				logger.error(logMessage + e.getTargetException().getMessage(), e);
				logger.error(Arrays.toString(e.getTargetException().getStackTrace()));
				tearDownAllFixtures(true);
				throw (StopTestException) e.getTargetException();
			} else if (e.getCause() instanceof StopTestException) {
				logger.error(logMessage + e.getCause().getMessage(), e);
				logger.error(Arrays.toString(e.getTargetException().getStackTrace()));
				tearDownAllFixtures(true);
				throw (StopTestException) e.getCause();
			} else if (e.getTargetException() instanceof ContinueTestException) {
				logger.debug(logMessage + e.getCause().getMessage(), e);
				throw (ContinueTestException) e.getTargetException();
			} else if (e.getCause() instanceof ContinueTestException) {
				throw (ContinueTestException) e.getCause();
			} else {
				logger.debug(logMessage + e.getCause().getMessage(), e);
				logger.error(logMessage + e.getTargetException().getMessage());
				Throwable targetExcpetion = e.getTargetException();
				if (e.getTargetException().getCause() != null) {
					targetExcpetion = e.getTargetException().getCause();
				}
				StackTraceElement[] stackTrace = targetExcpetion.getStackTrace();
				for (StackTraceElement stackTraceElement : stackTrace) {
					logger.error(stackTraceElement);
				}
				tearDownAllFixtures(true);
				if (e.getTargetException() != null) {
					throw new StopTestException("An unexpected error occurred: " + e.getTargetException().getMessage(),
							e.getTargetException());
				}
				throw new StopTestException("An unexpected error occurred: " + e.getMessage(), e);
			}
		} catch (Exception e) {
			logger.error(logMessage + e.getMessage(), e);
			tearDownAllFixtures(true);
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
	private void postInvoke(Method method, Object instance, Object[] convertedArgs)
			throws InvocationTargetException, IllegalAccessException {

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
	private void preInvoke(Method method, Object instance, Object[] convertedArgs)
			throws InvocationTargetException, IllegalAccessException {

		if (instance instanceof Fixture) {

			((Fixture) instance).preInvoke(method, instance, convertedArgs);
		}

	}

	/**
	 * Tries to shut down the fixture class.
	 */
	public static void tearDownAllFixtures() {
		tearDownAllFixtures(false);
	}

	public static void tearDownAllFixtures(boolean fail) {

		for (StoppableFixture object : stoppableFixturesList) {
			try {
				logger.debug(
						"calling tearDwon on object of type " + object.getClass().getName() + " stoppableFixtures ");
				object.tearDown(fail);
			} catch (Throwable e) {
				String logMessage = "Method : StoppableFixture.tearDown()";
				logger.error(logMessage + " " + e.getClass().getName() + ":" + e.getMessage(), e);
			}
		}
		stoppableFixturesList.clear();
		stoppableFixtures.clear();
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
	 * @param timeToWait
	 *            seconds to wait
	 */
	private void waitTime(long timeToWait) {
		try {
			Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Reads the configuration to extract the time to wait between test steps.
	 * 
	 * @return time to wait in mili seconds.
	 */
	public int getTimeToWait() {
		String waitsAfterTeststep = System.getProperty(WAIT_PROPERTY);
		if (waitsAfterTeststep == null) {
			return 0;
		}
		int scaleFactor = 1000;
		if (waitsAfterTeststep.trim().endsWith("ms")) {
			waitsAfterTeststep = waitsAfterTeststep.substring(0, waitsAfterTeststep.length() - 2);
			scaleFactor = 1;
		}
		if (waitsAfterTeststep.trim().endsWith("s")) {
			waitsAfterTeststep = waitsAfterTeststep.substring(0, waitsAfterTeststep.length() - 1);
		}
		int result = 0;
		try {
			result = Integer.parseInt(waitsAfterTeststep.trim());
			result = result * scaleFactor;
		} catch (NumberFormatException e) {
			logger.error("Unable to parse: " + waitsAfterTeststep.trim());
		}
		return result;
	}

	public static List<StoppableFixture> getBeans(String name) {
		if (stoppableFixtures.containsKey(name)) {
			return stoppableFixtures.get(name);
		}
		return null;
	}

}
