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

import java.util.HashMap;

/**
 * this class holds a key-value map to store and get them.
 * 
 * 
 */
public final class KeyValueStore {

	private static KeyValueStore instance;
	private HashMap<String, Object> store = new HashMap<String, Object>();

	/**
	 * private constructor.
	 */
	private KeyValueStore() {

	}

	/**
	 * 
	 * @return the actual instance of the store. If the instance is null, then a
	 *         new store is created and returned.
	 */
	public static KeyValueStore instance() {
		if (instance == null) {
			instance = new KeyValueStore();
		}
		return instance;
	}

	/**
	 * stores the key-value-pair.
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return true, if the key-value-pair, can be found in the store, else
	 *         false.
	 */
	public boolean store(String key, Object value) {
		store.put(key, value);
		return store.get(key).equals(value);
	}

	/**
	 * 
	 * @param key
	 *            String
	 * @return the value to the key of the store. If the key is not in the
	 *         store, null-value will be returned.
	 */
	public Object getValueToKey(String key) {
		return store.get(key);
	}

	/**
	 * removes the given key from the store.
	 * 
	 * @param key
	 *            String
	 * @return true, if the key can not be found in the store, else false.
	 */
	public boolean remove(String key) {
		store.remove(key);
		return !store.containsKey(key);
	}

	/**
	 * clears the store.
	 * 
	 * @return true, if the store is empty, else false.
	 */
	public boolean clear() {
		store.clear();
		return store.isEmpty();
	}
}
