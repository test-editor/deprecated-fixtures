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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * test for the KeyValueStore.
 * 
 */
public class KeyValueStoreTest {

	private KeyValueStore instance;
	private static String myKey = "foo";
	private static String myValue = "bar";

	/**
	 * test the storing and getting of a key-value-pair in the key.store.
	 */
	@Test
	public void testStoringGettingKeyValue() {
		Object valueToKey = instance.getValueToKey(myKey);
		assertNull(valueToKey);
		storeAndCheckGet();
	}

	/**
	 * test the removing of a key.
	 */
	@Test
	public void removeKey() {
		storeAndCheckGet();
		assertTrue(instance.remove(myKey));
		assertNull(instance.getValueToKey(myKey));
	}

	/**
	 * stores the key-value-pair and checks the existing of the pair.
	 */
	private void storeAndCheckGet() {
		instance.store(myKey, myValue);
		assertEquals(myValue, instance.getValueToKey(myKey));
	}

	/**
	 * test the clearing of the store.
	 */
	@Test
	public void clear() {
		storeAndCheckGet();
		instance.clear();
		assertNull(instance.getValueToKey(myKey));
	}

	/**
	 * test to get the same instance.
	 */
	@Test
	public void instance() {
		assertEquals(instance, KeyValueStore.instance());
	}

	/**
	 * setup before every test.
	 */
	@Before
	public void setup() {
		instance = KeyValueStore.instance();
		instance.clear();

	}
}
