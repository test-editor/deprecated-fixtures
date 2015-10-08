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
package org.testeditor.fixture.webservice.rest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the generic REST fixture.
 */
public class RestFixtureTest {

	private RestFixture restFixture;

	@Before
	public void init() {
		restFixture = new RestFixture();
	}

	@Test
	public void testSendGetWithInvalidUrl() {
		Assert.assertFalse("invalid url should return false", restFixture.sendGet(""));
	}
}
