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
package org.testeditor.fixture.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testeditor.fixture.core.interaction.FixtureMethod;

public class WebDriverFixture {

	private WebDriver driver;
	private Logger logger = LogManager.getLogger(WebDriverFixture.class);

	@FixtureMethod
	public void waitSeconds(long timeToWait) throws InterruptedException {
		Thread.sleep(timeToWait * 1000);
	}

	public WebDriver getDriver() {
		return driver;
	}

	@FixtureMethod
	public void startBrowser(String browser) {
		logger.info("Starting brwoser: {}", browser);
		switch (browser) {
		case "default":
			if (System.getProperty("os.name").startsWith("Windows")) {
				System.setProperty("webdriver.ie.driver", "c:/vagrant_bin/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			} else {
				launchFirefox();
			}
			break;
		case "firefox":
			launchFirefox();
			break;
		case "ie":
			driver = new InternetExplorerDriver();
			break;
		case "chrome":
			driver = new ChromeDriver();
			break;
		}
		configureDriver();
	}

	protected void configureDriver() {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	private void launchFirefox() {
		File profFile = new File(System.getenv("java.io.tmpdir"), "selenium");
		profFile.mkdir();
		logger.debug("Creating firefox profile in: {}", profFile);
		FirefoxProfile profile = new FirefoxProfile(profFile);
		if (System.getProperty("http.proxyHost") != null) {
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", System.getProperty("http.proxyHost"));
			profile.setPreference("network.proxy.http_port", System.getProperty("http.proxyPort"));
			profile.setPreference("network.proxy.no_proxies_on", System.getProperty("http.nonProxyHosts"));
		}
		driver = new FirefoxDriver(profile);
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
		WebElement element = getWebElement(elementLocator);
		element.submit();
	}

	@FixtureMethod
	public void typeInto(String elementLocator, String value) {
		WebElement element = getWebElement(elementLocator);
		element.sendKeys(value);
	}

	@FixtureMethod
	public void clear(String elementLocator) {
		WebElement element = getWebElement(elementLocator);
		element.clear();
	}

	@FixtureMethod
	public void clickOn(String elementLocator) {
		WebElement element = getWebElement(elementLocator);
		element.click();
	}

	@FixtureMethod
	public String readValue(String elementLocator) {
		WebElement element = getWebElement(elementLocator);
		return element.getText();
	}

	protected WebElement getWebElement(String elementLocator) {
		logger.info("Lookup element {}", elementLocator);
		WebElement result = null;
		if (elementLocator.startsWith("[xpath]")) {
			result = driver.findElement(By.xpath(extractLocatorStringFrom(elementLocator)));
		}
		if (elementLocator.startsWith("[name]")) {
			result = driver.findElement(By.name(extractLocatorStringFrom(elementLocator)));
		}
		if (elementLocator.startsWith("[link]")) {
			result = driver.findElement(By.linkText(extractLocatorStringFrom(elementLocator)));
		}
		if (elementLocator.startsWith("[id]")) {
			result = driver.findElement(By.id(extractLocatorStringFrom(elementLocator)));
		}
		if (result == null) {
			result = driver.findElement(By.name(elementLocator));
		}
		return result;
	}

	protected String extractLocatorStringFrom(String elementLocator) {
		return elementLocator.substring(elementLocator.indexOf(']') + 1);
	}
}
