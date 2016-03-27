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
 *******************************************************************************/
package org.testeditor.fixture.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testeditor.fixture.core.interaction.FixtureMethod;

public class WebDriverFixture {

	private WebDriver driver;

	@FixtureMethod
	public void waitSeconds(long timeToWait) throws InterruptedException {
		Thread.sleep(timeToWait * 1000);
	}

	@FixtureMethod
	public void startBrowser(String browser) {
		switch (browser) {
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "ie":
			driver = new InternetExplorerDriver();
			break;
		case "chrome":
			driver = new ChromeDriver();
		}
	}

	@FixtureMethod
	public void gotToUrl(String url) {
		driver.get(url);
	}

	@FixtureMethod
	public void closeBrowser() {
		driver.close();
	}

	@FixtureMethod
	public void pressEnterOn(String elementLocator) {
		WebElement element = driver.findElement(By.name(elementLocator));
		element.submit();
	}

	@FixtureMethod
	public void typeInto(String elementLocator, String value) {
		WebElement element = driver.findElement(By.name(elementLocator));
		element.sendKeys(value);
	}

	@FixtureMethod
	public void clear(String elementLocator) {
		WebElement element = driver.findElement(By.name(elementLocator));
		element.clear();
	}

}
