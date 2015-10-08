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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Provides methods for a generic way to access any REST web-service.
 */
public class RestFixture {
	private static final Logger LOGGER = Logger.getLogger(RestFixture.class);
	private String response;

	/**
	 * Sends a GET request to the given URL and creates a log with the response.
	 * 
	 * @param url
	 *            URL as a string with parameters of the given REST web-service
	 * @return true if invoke was valid, otherwise false
	 */
	public boolean sendGet(String url) {
		try {
			InputStream inputStream = new URL(url).openStream();
			StringBuilder stringBuilder = new StringBuilder();
			int c = 0;

			try {
				while ((c = inputStream.read()) != -1) {
					stringBuilder.append((char) c);
				}
			} catch (IOException e) {
				LOGGER.error("send url failed with execption: " + e);
				return false;
			}

			response = stringBuilder.toString();
			LOGGER.info("send url: " + url + ", response: \n" + response);
		} catch (Exception e) {
			LOGGER.error("send url failed with execption: " + e);
			return false;
		}

		return true;
	}

	/**
	 * Checks a part of the XML response at the given x-path position.
	 * 
	 * @param xpath
	 *            x-path to find the element
	 * @param response
	 *            expected response part
	 * @return true if the given response is equal to the web-service response
	 */
	public boolean checkXmlResponse(String xpath, String response) {
		try {
			NodeList nodeList = getResponsePartAsNoteList(xpath);
			String responsePart = "";

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				if (node.getNodeValue() != null) {
					responsePart = responsePart.concat(node.getNodeValue());
				}
			}

			Boolean equal = responsePart.equals(response);
			LOGGER.info("check xml response: " + equal + ", responsePart: " + responsePart);

			return equal;
		} catch (Exception e) {
			LOGGER.error("check xml response failed with exception: " + e + ", xpath: " + xpath
					+ ", expectedResponse: " + response);
			return false;
		}
	}

	/**
	 * Checks the children count at the given x-path position.
	 * 
	 * @param xpath
	 *            x-path to find the element
	 * @param count
	 *            numbers of children
	 * @return true if the number of children is equal to the web-service
	 *         response
	 */
	public boolean checkChildrenCount(String xpath, String count) {
		try {
			NodeList nodeList = getResponsePartAsNoteList(xpath);

			Boolean equal = Integer.valueOf(count).intValue() == nodeList.getLength();
			LOGGER.info("check children count: " + equal + "size: " + nodeList.getLength());

			return equal;
		} catch (Exception e) {
			LOGGER.error("check children count failed with exception: " + e + ", xpath: " + xpath + ", count: " + count);
			return false;
		}
	}

	/**
	 * Returns a response part as a XML note list for the given web-service
	 * response.
	 * 
	 * @param xpathExpr
	 *            x-path expression (mustn't contain any namespace prefixes)
	 * @return list of nodes
	 * @throws Exception
	 *             is thrown in case of any XML or x-path compile errors
	 */
	private NodeList getResponsePartAsNoteList(String xpathExpr) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(false);

		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(response)));

		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(xpathExpr);

		NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		return nodeList;
	}
}
