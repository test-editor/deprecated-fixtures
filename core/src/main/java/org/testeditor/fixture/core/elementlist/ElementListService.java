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

package org.testeditor.fixture.core.elementlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testeditor.fixture.core.exceptions.ElementKeyNotFoundException;

/**
 * This service provides access to the key-value pairs of an element list. <br />
 * 
 * Each FitNesse project which uses any Test-Editor fixtures should have an
 * element list stored inside the FitNesse project (see for example
 * .testeditor/DemoWebTests/FitNesseRoot/DemoWebTests/ElementListe/content.txt).
 * This list is just a wiki page with key-value pairs.<br />
 * 
 * Keys are used inside any test-cases in human readable type. The fixture
 * implementation on the other translates the keys into the values via this
 * service. Values are the technical addresses of any items (e.g. the x-path to
 * a HTML object).
 */
public final class ElementListService {
	private static final Logger LOGGER = Logger.getLogger(ElementListService.class);

	/**
	 * ElementListService exists only once per element list file.
	 */
	private static Map<String, ElementListService> instances = new HashMap<String, ElementListService>();

	private Properties properties;

	/**
	 * Initializes the singleton.
	 * 
	 * @param file
	 *            the element list file
	 */
	private ElementListService(String fileName) {
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream(fileName);
			properties = new Properties();
			properties.load(new InputStreamReader(fileInputStream, "UTF-8"));
		} catch (Exception e) {
			LOGGER.error("ElementListService :: FAILED - systemException: " + e.getMessage());
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					LOGGER.error("ElementListService :: FAILED - systemException: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Returns the singleton instance of the service.
	 * 
	 * @param filePath
	 *            path to the element list
	 * @return singleton
	 */
	public static ElementListService instanceFor(String filePath) {
		File file = new File(filePath);
		String canonicalPath = null;
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			throw new IllegalArgumentException("ElementListService :: FAILED - systemException: ", e);
		}

		if (!instances.containsKey(canonicalPath)) {
			instances.put(canonicalPath, new ElementListService(filePath));
		}
		return instances.get(canonicalPath);
	}

	/**
	 * Returns the value for a given key.
	 * 
	 * @param key
	 *            key (e.g. inputUsername)
	 * @return value (e.g. x-path to a HTML object)
	 * @throws ElementKeyNotFoundException
	 *             is thrown if the element key doesn't exist
	 */
	public String getValue(String key) throws ElementKeyNotFoundException {

		if (key.contains(" ")) {
			key = key.replaceAll("\\s", "");
		}
		String property = properties.getProperty(key);

		if (property == null) {
			LOGGER.info(key + " was not found in properties " + properties);
			throw new ElementKeyNotFoundException(key);
		} else {
			property = property.trim();
		}

		return property;
	}

}
