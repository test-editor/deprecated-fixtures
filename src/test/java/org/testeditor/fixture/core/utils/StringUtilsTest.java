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

package org.testeditor.fixture.core.utils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for the {@link StringUtils}.
 */
public class StringUtilsTest {

	/**
	 * Tests the isDigit method.
	 */
	@Test
	public void testIsDigit() {
		Assert.assertTrue("invalid digit: 12", StringUtils.isDigit("12"));
		Assert.assertTrue("invalid digit: 1092394", StringUtils.isDigit("1092394"));
		Assert.assertFalse("invalid digit: 1e", StringUtils.isDigit("1e"));
		Assert.assertFalse("invalid digit: iiasdfiwefy??wee", StringUtils.isDigit("iiasdfiwefy??wee"));
		Assert.assertFalse("invalid digit: 1iias2dfiwefy??we5e7", StringUtils.isDigit("1iias2dfiwefy??we5e7"));
	}

	/**
	 * Tests the isXpath method.
	 */
	@Test
	public void isXpath() {
		Assert.assertTrue("Is not a XPath: //div[contains(@id,'loginDialog.textUsername')]/div/input",
				StringUtils.isXPath("//div[contains(@id,'loginDialog.textUsername')]/div/input"));
		Assert.assertFalse("Is a XPath: filter.buttonSearch", StringUtils.isXPath("filter.buttonSearch"));
		Assert.assertFalse("Is a XPath", StringUtils.isXPath(""));
		Assert.assertFalse(StringUtils.isXPath(null));
	}
}
