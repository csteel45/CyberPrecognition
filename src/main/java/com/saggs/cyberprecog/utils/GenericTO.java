/*
 * Copyright 2014 Software AG Government Solutions (Software AG GS)
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Software AG
 * Government Solutions ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Software AG GS.
 *
 * SOFTWARE AG GS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON_INFRINGEMENT. SOFTWARE AG GS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.saggs.cyberprecog.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.security.auth.Subject;

import com.saggs.cyberprecog.utils.xml.ValueMarshaller;

/**
 * This is a generic transfer object implementation for exchanging data between
 * components and layers. It is necessary in order to handle dynamically
 * evolving data sets from yet unknown data sources.
 * 
 * @author Christopher Steel - Software AG Government Solutions
 * 
 * @since Apr 10, 2014 3:24:33 PM
 * @version 1.0
 */
public class GenericTO implements Serializable {

	static final long serialVersionUID = -5831612260903682186L;

	private static HashSet<String> obscured = new HashSet<String>(); //Used to hide sensitive data when printing out
	private HashMap<String, Object> map; //Underlying map to hold all NV pairs
	private float version;
	private String service;
	private String action;
	private Subject subject;
	private short eventTypeId = 0;
	private Exception exception;
	private Collection<Object> errorMessages = new HashSet<Object>();
	private Collection<Object> messages = new HashSet<Object>();

	static {
		obscured.add("password"); //obscure entries with key name 'password'
	}

	/**
	 * Default constructor that initializes the object with empty values.
	 */
	public GenericTO() {
		this("", new HashMap<String, Object>(), 1.0f);
	}

	/**
	 * Constructor for service
	 * 
	 * @param service Name of target service
	 * @param map Collection of NV pairs to pass to or from service
	 * @param version Version of the GenericTO
	 */
	public GenericTO(String service, Map<String, Object> map, float version) {
		this.map = new HashMap<String, Object>();
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String fKey = fixName(key);
			this.map.put(fKey, map.get(key));
		}
		// this.map.putAll(map); //Don't use because we need to fix names
		this.service = service;
		this.version = version;
	}

	/**
	 * Constructor for service and action
	 * 
	 * @param service Name of target service
	 * @param action Name of the action (method...) to invoke on the service
	 * @param map Collection of NV pairs to pass to or from service
	 * @param version Version of the GenericTO
	 */
	public GenericTO(String service, String action, Map<String, Object> map, float version) {
		this(service, map, version);
		this.action = action;
	}

	/**
	 * Constructor for service and action
	 * 
	 * @param service Name of target service
	 * @param action Name of the action (method...) to invoke on the service
	 * @param map Collection of NV pairs to pass to or from service
	 * @param version Version of the GenericTO
	 * @param eventTypeId The type of event associated with this GTO
	 */
	public GenericTO(String service, String action, Map<String, Object> map, float version, short eventTypeId) {
		this(service, action, map, version);
		this.eventTypeId = eventTypeId;
	}

	/**
	 * Fixes the Name/Key by removing dashes and underscores and capitalizing correctly.<br>
	 * For example My-first_Name would be fixed to myFirstName
	 * 
	 * @param str The String to be fixed.
	 * @return Fixed String
	 */
	private String fixName(String str) {
		StringTokenizer token = new StringTokenizer(str, "-,_");
		String result = token.nextToken();
		while (token.hasMoreTokens()) {
			result += capitalize(token.nextToken());
		}
		return result;

	}

	private String capitalize(String str) {
		return str.replaceFirst("[a-z]", str.substring(0, 1).toUpperCase());
	}

	/**
	 * Returns an instance of the specified interface, backed by the underlying Map.<br>
	 * For programmatic ease and type safety, specify an interface with getters and setters
	 * for the keys/names needed in the GTO Map. Then get a Proxy object that enforces that
	 * interface on the GTO. For example, if my map contains NV pairs: "foo:bar, age:40, id:csteel"
	 * and I am interested in foo and age, I can create an interface with String getFoo(), setFoo(String val),
	 * int getAge(), setAge(int val) methods called MyDTO and then call MyDTO myDTO = (MyDTO)gto.getDTO(MyDTO.class).<br>
	 * This makes life easier than call (Integer)gto.get("age"), and messing with the primitive conversion. 
	 * It also gives autocomplete capabilities on the methods and prevents runtime errors from typos when 
	 * specifying string values for the gets. BUT, you can add and get additional values to/from the
	 * underlying map without needing to change the interface. This gives the best of both worlds, type
	 * safety for normal use and ability to dynamically add attributes without changing interfaces.
	 * 
	 * @param c The class (interface) to Proxy to.
	 * @return Instance of an Object implementingthe declared interface, backed by the GTO.
	 */
	public Object getDTO(Class<?> c) {
		return GTOProxyHandler.newInstance(c, this);
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public short getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(short eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public float getVersion() {
		return version;
	}

	public HashMap<String, Object> getMap() {
		return map;
	}

	public Object get(String sKey) {
		return (map.get(sKey));
	}

	/**
	 * Adds a key/value to the parameter list
	 * 
	 * @param skey The key String.
	 * @param obj The value object.
	 */
	public void put(String skey, Object obj) {
		map.put(skey, obj);
	}

	/**
	 * @return True if an exception occurred down stream.
	 */
	public boolean hasException() {
		return (exception != null);
	}

	/**
	 * @return Exception that occurred
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 * 
	 * @param exception The exception to set.
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return True if there are error messages added to this instance.
	 */
	public boolean hasErrorMessages() {
		return (errorMessages != null && !errorMessages.isEmpty());
	}

	/**
	 * Returns a unmodifiable collection of error messages.
	 * 
	 * @return Collection
	 */
	public Collection<String> getErrorMessages() {
		ArrayList<String> toReturn = new ArrayList<String>();
		Iterator<Object> iterator = errorMessages.iterator();
		while (iterator.hasNext()) {
			toReturn.add(((MessageBundle) iterator.next()).getMessage());
		}
		return Collections.unmodifiableCollection(toReturn);
	}

	/**
	 * Returns the first error message in the collection, if any exists.
	 * 
	 * @return String The first error message or null if none exist.
	 */
	public String getErrorMessage() {
		if (errorMessages.size() == 0) {
			return null;
		}
		Iterator<String> i = getErrorMessages().iterator();
		return i.next();
	}

	/**
	 * Returns a modifiable collection of error messages.
	 * 
	 * @return Collection
	 */
	public Collection<Object> getErrorMessageBundles() {
		return errorMessages;
	}

	public void addErrorMessage(Object error) {

		if (error instanceof MessageBundle) {

			errorMessages.add(error);

		} else {

			addErrorMessage(String.valueOf(error), null);
		}
	}

	/**
	 * Push an error into the GTO to carry to the service client.
	 * Use this method to push an error that requires no parametric replacement.
	 * 
	 * @param error The error to carry.
	 */
	public void addErrorMessage(String error) {

		addErrorMessage(error, null);
	}

	/**
	 * Push an error, along with replacement parameters, into the GTO to carry
	 * to the service client.
	 * 
	 * @see java.text.MessageFormat;
	 * 
	 * @param error The error to carry
	 * @param parameters The parameters for replacement.
	 */
	public void addErrorMessage(String error, String[] parameters) {

		if (error == null) {
			throw new IllegalArgumentException("error cannot be null");
		}
		errorMessages.add(new MessageBundle(error, parameters));
	}

	/**
	 * This is to support merging errors from multiple calls into the GTO.
	 * 
	 * @param errorMessages A collection of messages to add.
	 */
	public void addErrorMessages(Collection<?> errorMessages) {

		Iterator<?> iterator = errorMessages.iterator();
		while (iterator.hasNext()) {
			addErrorMessage(iterator.next());
		}
	}

	/**
	 * Sets the errorMessages.
	 * 
	 * @param errorMessages The errorMessages to set. ASSUMED TO CONTAIN STRINGS!!
	 */
	public void setErrorMessages(Collection<?> errorMessages) {

		this.errorMessages = new HashSet<Object>();
		addErrorMessages(errorMessages);
	}

	/**
	 * Returns whether or not this instance has messages associated with it.
	 * @return True if any messages were set.
	 */
	public boolean hasMessages() {

		return (messages != null) && (!messages.isEmpty());
	}

	/**
	 * Returns all the messages set as a Collection.
	 * 
	 * @return Collection of messages
	 */
	public Collection<String> getMessages() {
		ArrayList<String> toReturn = new ArrayList<String>();
		Iterator<Object> iterator = messages.iterator();
		while (iterator.hasNext()) {
			toReturn.add(((MessageBundle) iterator.next()).getMessage());
		}

		return Collections.unmodifiableCollection(toReturn);
	}

	/**
	 * Collection of MessageBundles
	 * 
	 * @return Collection of Messages
	 */
	public Collection<Object> getMessageBundles() {
		return messages;
	}

	/**
	 * Add a message of unknown type. The method attempts to normalize the
	 * parameter into something it can use such as a MessageBundle
	 * 
	 * @param message MessageBundle or String message to add.
	 */
	public void addMessage(Object message) {

		if (message instanceof MessageBundle) {
			messages.add(message);
		} 
		else {
			addMessage(String.valueOf(message));
		}
	}

	/**
	 * Push a message into the GTO to carry to the service client.
	 * 
	 * Use this method to push a message that requires no parametric
	 * replacement.
	 * 
	 * @param message The message to carry.
	 */
	public void addMessage(String message) {

		addMessage(message, null);
	}

	/**
	 * Push a message, along with replacement parameters, into the GTO to carry
	 * to the service client.
	 * 
	 * @see java.text.MessageFormat;
	 * 
	 * @param message The message to carry
	 * @param parameters The parameters for replacement.
	 */
	public void addMessage(String message, String[] parameters) {

		if (message == null) {
			throw new IllegalArgumentException("message cannot be null");
		}
		messages.add(new MessageBundle(message, parameters));
	}

	/**
	 * Replace the current messages with the new Collection
	 * 
	 * @param messages The Collection of messages for replacement.
	 */
	public void setMessages(Collection<?> messages) {

		this.messages = new HashSet<Object>();
		addMessages(messages);
	}

	/**
	 * This is to support the SessionFacadeBase merging messages from multiple
	 * calls into the GTO.
	 * 
	 * @param messages The Collection of messages to add.
	 */
	public void addMessages(Collection<?> messages) {

		Iterator<?> iterator = messages.iterator();
		while (iterator.hasNext()) {

			addMessage(iterator.next());
		}
	}

	/**
	 * Clears the map, error messages, messages, and exceptions previously set.
	 */
	public void clear() {
		this.map.clear();
		this.errorMessages.clear();
		this.messages.clear();
		this.exception = null;
	}

	/**
	 * Clears only the errors and exception in this Object.
	 */
	public void clearError() {
		this.errorMessages.clear();
		this.exception = null;
	}

	/**
	 * Get a String representation of an instance of this. Basically this
	 * returns the service name, the action, the key-value pairs in the map if
	 * any, and the exception and error messages, if any.
	 * 
	 * @return String representation of the Object
	 */
	public String toString() {
		// return(toXML());
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("GTO service = ").append(this.getService());
			sb.append(" action = ").append(this.getAction()).append("\n");
			Set<String> keySet = map.keySet();
			ArrayList<String> list = new ArrayList<>();
			list.addAll(keySet);
			Collections.sort(list);
			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				Object value = map.get(key);
				if (value instanceof byte[]) {
					sb.append("\t" + key + " = " + new String((byte[]) value, "UTF-8") + "\n");
				} else
					sb.append("\t" + key + " = " + value + "\n");
			}
		} catch (Exception e) {
			return ("");
		}
		return sb.toString();
	}

	/**
	 * Returns the Name/Value pairs in XML format.
	 * 
	 * @return NV pairs in valid XML format.
	 * @throws IOException
	 */
	public String toXML() throws IOException {

		StringBuffer sb = new StringBuffer();
		sb.append("<GTO service=\"").append(this.getService()).append("\" ");
		sb.append("action=\"").append(this.getAction()).append("\">\n");
		if (subject != null) {
			sb.append("subject=\"").append(this.getSubject().getPrincipals().toArray()[0]).append("\">\n");
		}

		//
		// Dump any messages to the XML.
		//
		if (messages != null && !messages.isEmpty()) {
			Iterator<Object> iter = messages.iterator();
			while (iter.hasNext()) {
				sb.append("  <Message>");
				sb.append(ValueMarshaller.normalizeString(iter.next().toString(), false));
				sb.append("</Message>\n");
			}
		}

		//
		// Dump any exception that occurred.
		//
		if (exception != null) {
			sb.append("  <Exception>");
			sb.append(ValueMarshaller.normalizeString(exception.getMessage(), false));
			sb.append("</Exception>\n");
		}

		//
		// Dump error messages to the XML.
		//
		if (errorMessages != null && !errorMessages.isEmpty()) {
			Iterator<Object> iter = errorMessages.iterator();
			while (iter.hasNext()) {
				sb.append("  <Error>");
				sb.append(ValueMarshaller.normalizeString(iter.next().toString(), false));
				sb.append("</Error>\n");
			}
		}

		if (map != null && !map.isEmpty()) {
			Iterator<?> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				String key = String.valueOf(entry.getKey());
				Object value = entry.getValue();

				sb.append("  <Value name=\"").append(key).append("\" ");
				sb.append("type=\"").append((value == null) ? "null" : value.getClass().getSimpleName()).append("\">");

				if (value != null) {
					appendValue(sb, value, key);
				}
				sb.append("</Value>\n");
			}
		}

		sb.append("</GTO>");
		return (sb.toString());
	}

	/**
	 * Primary method for building a String representation of the Object
	 * @param toAppendTo The StringBuffer to append to
	 * @param value The Values to append to the StringBuffer
	 * @param key The key in the map used to check if the values should be obscured.
	 * @throws IOException If anything goes wrong normalizing the String.
	 */
	private void appendValue(StringBuffer toAppendTo, Object value, String key) throws IOException {
		if (obscured.contains(key)) {
			toAppendTo.append(new String("{OBSCURED}"));
			return;
		}

		if (value == null) {
			toAppendTo.append("null");
		}

		else if (value instanceof GenericTO) {
			toAppendTo.append(value);
		} 
		else if (value.getClass().isArray()) {
			int length = java.lang.reflect.Array.getLength(value);
			toAppendTo.append("[ ");
			for (int i = 0; i < length; i++) {
				Object obj = java.lang.reflect.Array.get(value, i);
				appendValue(toAppendTo, obj, key);
				toAppendTo.append(',');
			}
			toAppendTo.setCharAt(toAppendTo.length() - 1, ']');
		} else {
			toAppendTo.append(ValueMarshaller.normalizeString(String.valueOf(value), false));
		}
	}
	
	/**
	 * Test harness for testing adding and printing a byte array.
	 * 
	 * @param args Not Used.
	 */
	public static void main(String[] args) {
		GenericTO gto = new GenericTO();
		TestDTO dto = (TestDTO) gto.getDTO(TestDTO.class);
		System.out.println("TestDTO: " + dto);
		dto.setPayload(new byte[0]);
		System.out.println("TestDTO: " + dto);
		dto.setPayload("Hello World".getBytes());
		System.out.println("TestDTO: " + dto);
	}

	/**
	 * Used just for testing
	 */
	private interface TestDTO extends TransferObject {
		public byte[] getPayload();

		public void setPayload(byte[] p);
	}

	/**
	 * Used for passing messages up and down the call stream.
	 */
	static public class MessageBundle implements Serializable {

		static final long serialVersionUID = -1165614544659483106L;

		private String message = null;
		private String[] parameters = null;
		private String string = null;

		MessageBundle(String message, String[] parameters) {

			this.message = message;
			this.parameters = parameters;

			if (message == null) {
				throw new IllegalArgumentException("message cannot be null.");
			}

			if (parameters != null && parameters.length > 0) {
				StringBuffer strBuf = new StringBuffer(message);
				strBuf.append("( ");
				for (int i = 0; i < parameters.length; i++) {
					strBuf.append(parameters[i] + " ");
				}
				strBuf.append(")");
				this.string = strBuf.toString();
			} else {
				this.string = message;
			}
		}

		public String getMessage() {

			return message;
		}

		public String[] getParameters() {

			return parameters;
		}

		public boolean equals(Object object) {

			return ((object instanceof MessageBundle) && (this.string.equals(((MessageBundle) object).toString())))
					|| ((object instanceof String) && (string.equals((String) object)));
		}

		public int hashCode() {
			return string.hashCode();
		}

		public String toString() {
			return string;
		}
	}
}
