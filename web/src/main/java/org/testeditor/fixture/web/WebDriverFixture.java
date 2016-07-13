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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testeditor.fixture.core.interaction.FixtureMethod;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.MarionetteDriverManager;

public class WebDriverFixture {

	private WebDriver driver;
	private Logger logger = LogManager.getLogger(WebDriverFixture.class);
	private String exeuteScript = null;

	@FixtureMethod
	public void waitSeconds(long timeToWait) throws InterruptedException {
		Thread.sleep(timeToWait * 1000);
	}

	public WebDriver getDriver() {
		return driver;
	}

	@FixtureMethod
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	@FixtureMethod
	public void setExecuteScript(String exec) {
		exeuteScript = exec;
	}

	@FixtureMethod
	public WebDriver startBrowser(String browser) {
		logger.info("Starting brwoser: {}", browser);
		switch (browser) {
		case "default":
			if (System.getProperty("os.name").startsWith("Windows")) {
				launchIE();
			} else {
				launchFirefoxMarionetteDriver();
			}
			break;
		case "firefox":
			launchFirefox();
			break;
		case "modernfirefox":
			launchFirefoxMarionetteDriver();
			break;
		case "ie":
			launchIE();
			break;
		case "chrome":
			launchChrome();
			break;
		}
		configureDriver();
		return driver;
    }

	@FixtureMethod
	public WebDriver startFireFoxPortable(String browserPath) {
		logger.info("Starting firefox portable: {}", browserPath);
		FirefoxBinary binary = new FirefoxBinary(new File(browserPath));
		FirefoxProfile profile = getFireFoxProfile();
		driver = new FirefoxDriver(binary, profile);
		configureDriver();
		return driver;
	}

	private void launchIE() {
		InternetExplorerDriverManager.getInstance().setup();
		driver = new InternetExplorerDriver();
	}

	private void launchChrome() {
		ChromeDriverManager.getInstance().setup();
		driver = new ChromeDriver();
		registerShutdownHook(driver);
	}

	private void launchFirefoxMarionetteDriver() {
//		Authenticator.setDefault(new Authenticator() {
//	          @Override
//	         public PasswordAuthentication getPasswordAuthentication() {
//	               if(getRequestorType() == Authenticator.RequestorType.PROXY) 
//	                   return new PasswordAuthentication("your_proxy_username", "your_proxy_password".toCharArray());
//	               else
//	                  return super.getPasswordAuthentication();
//	         }});
		MarionetteDriverManager.getInstance().setup("v0.7.1");
		driver = new MarionetteDriver();
		registerShutdownHook(driver);
	}

	private void registerShutdownHook(final WebDriver driver) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				driver.quit();
			}
		});
	}

	protected void configureDriver() {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	private void launchFirefox() {
		FirefoxProfile profile = getFireFoxProfile();
		driver = new FirefoxDriver(profile);
	}

	private FirefoxProfile getFireFoxProfile() {
		File profFile = new File(System.getenv("java.io.tmpdir"), "selenium");
		if (profFile.exists()) {
			profFile.delete();
		}
		profFile.mkdir();
		logger.debug("Creating firefox profile in: {}", profFile);
		FirefoxProfile profile = new FirefoxProfile(profFile);
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		profile.setPreference("security.mixed_content.block_active_content", false);
		profile.setPreference("security.mixed_content.block_display_content", true);
		
		logger.debug("proxyHost:\"" + System.getProperty("http.proxyHost") + "\"");
		logger.debug("proxyPort:\"" + System.getProperty("http.proxyPort") + "\"");
		logger.debug("nonProxyHosts:\"" + System.getProperty("http.nonProxyHosts") + "\"");
		
		if (System.getProperty("http.proxyHost") != null) {
			logger.info("Setting up proxy: {} on port {} with nonProxyHosts {} ", System.getProperty("http.proxyHost"), System.getProperty("http.proxyPort"), System.getProperty("http.nonProxyHosts") );
			profile.setPreference("network.proxy.user_name", System.getProperty("http.proxyUser"));
			profile.setPreference("network.proxy.password", System.getProperty("http.proxyPassword"));
			profile.setPreference("network.proxy.type", 1);
			profile.setPreference("network.proxy.http", System.getProperty("http.proxyHost"));
			profile.setPreference("network.proxy.http_port", System.getProperty("http.proxyPort"));
			profile.setPreference("network.proxy.no_proxies_on", System.getProperty("http.nonProxyHosts"));
		}
		return profile;
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
	
	@FixtureMethod
	public void selectElementInSelection(String elementLocator, String value) throws InterruptedException{
		clickOn(elementLocator);
		Thread.sleep(300);
		WebElement element = getWebElement(elementLocator);
	    new Select(element).selectByVisibleText(value);
		//logger.trace("Selected value {} in selection {}", value, elementLocator);
	}
	
	@FixtureMethod
	public Map<String, String>  getOptionsInSelection(String elementLocator) throws InterruptedException {
		clickOn(elementLocator);
		Thread.sleep(300);
		Map<String, String> namesOfAllSelectedOptions = new HashMap<String, String>();
		Select selection = new Select(getWebElement(elementLocator));
		List<WebElement> allSelectedOptions;allSelectedOptions = selection.getAllSelectedOptions();
		for (WebElement webElement : allSelectedOptions) {
			namesOfAllSelectedOptions.put(webElement.getText(), webElement.getText());
		}
		return namesOfAllSelectedOptions;
	}
	
	@FixtureMethod
	public void moveToElementAndClick(String elementLoacator) {
		WebElement element = getWebElement(elementLoacator);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();
	}
	
	@FixtureMethod
	public Boolean checkEnabled(String elementLoacator) {
		WebElement element = getWebElement(elementLoacator);
		return element.isEnabled();
	}

	protected WebElement getWebElement(String elementLocator) {
		if (exeuteScript != null) {
			executeScript();
		}
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
		if (elementLocator.startsWith("[css]")) {
			result = driver.findElement(By.cssSelector(extractLocatorStringFrom(elementLocator)));
		}
		if (result == null) {
			result = driver.findElement(By.name(elementLocator));
		}
		return result;
	}

	private void executeScript() {
		try {
			System.err.println(exeuteScript);
			BufferedReader br = new BufferedReader(new FileReader(exeuteScript));
			StringBuilder sb = new StringBuilder();
			while (br.ready()) {
				sb.append(br.readLine()).append("\n");
			}
			br.close();
			logger.info("execute script {}", exeuteScript);
			((JavascriptExecutor) driver).executeScript(sb.toString());
		} catch (IOException e) {
			logger.error("Can't read java script", e);
			throw new RuntimeException(e);
		}
	}

	protected String extractLocatorStringFrom(String elementLocator) {
		return elementLocator.substring(elementLocator.indexOf(']') + 1);
	}
	
}
