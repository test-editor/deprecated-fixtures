/*******************************************************************************
 * Copyright (c) 2012 - 2016 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 * itemis AG
 *******************************************************************************/
package org.testeditor.fixture.web.angularjs;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testeditor.fixture.web.WebDriverFixture;

import com.paulhammant.ngwebdriver.NgWebDriver;

public class NgOneWebDriverFixture extends WebDriverFixture {

	private Logger logger = LogManager.getLogger(NgOneWebDriverFixture.class);
	private NgWebDriver ngWebDriver;

	@Override
	protected WebElement getWebElement(String elementLocator) {
		waitForAngularCompleteOperations();
		return super.getWebElement(elementLocator);
	}

	@Override
	protected void configureDriver() {
		super.configureDriver();
		ngWebDriver = new NgWebDriver((JavascriptExecutor) getDriver());
		getDriver().manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	}

	/**
	 * Method to wait that angular operations are completed before looking up
	 * new operations.
	 */
	protected void waitForAngularCompleteOperations() {
		logger.trace("Wait until angular operations are completed.");
		ngWebDriver.waitForAngularRequestsToFinish();
		logger.trace("Angular operaitons completed.");
	}

}
