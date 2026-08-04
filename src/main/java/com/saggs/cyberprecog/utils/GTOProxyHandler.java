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

import java.lang.reflect.*;
import java.io.*;
import java.util.HashMap;

/**
 * This class implements a dynamic proxy and invocation handler that wraps
 * around a GenericTO (Generic Transfer Object) to provide strong type casting
 * abilities to the GTO.
 * 
 * @author Christopher Steel - Software AG Government Solutions
 * 
 * @since Apr 10, 2014 3:24:33 PM
 * @version 1.0
 */
public class GTOProxyHandler implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 9144389024715092617L;
	private GenericTO _gto;
	private static HashMap<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

	static {
		primitiveDefaults.put(Byte.TYPE, new Byte((byte) 0));
		primitiveDefaults.put(Short.TYPE, new Short((short) 0));
		primitiveDefaults.put(Integer.TYPE, new Integer(0));
		primitiveDefaults.put(Long.TYPE, new Long(0L));
		primitiveDefaults.put(Float.TYPE, new Float(0.0F));
		primitiveDefaults.put(Double.TYPE, new Double(0.0));
		primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaults.put(Character.TYPE, new Character((char) 0));
	}
	
	/**
	 * Create a new proxy handler object that has a new generic transfer object
	 * as its storage.
	 */
	private GTOProxyHandler() {
		this(new GenericTO());
	}

	/**
	 * Create a new proxy handler object that uses an existing generic transfer
	 * object as its storage.
	 * 
	 * @param gto The GTO that should be used as the underlying storage area for member data.
	 */
	private GTOProxyHandler(GenericTO gto) {
		_gto = gto;
	}

	/**
	 * Create a new object that implements a particular class or interface with
	 * underlying storage using a Generic Transfer Object (GTO). Since the GTO
	 * is passed into this method, the new object can already be "seeded" with
	 * data if the data conforms to the interface being implemented.
	 * 
	 * @param classToCreate The class or interface to implement.
	 * @param gto The GTO to use as the underlying storage object.
	 * 
	 * @return A new object that implements the specified class.
	 */
	public static Object newInstance(Class<?> classToCreate, GenericTO gto) {
		GTOProxyHandler handler = new GTOProxyHandler(gto);
		return Proxy.newProxyInstance(classToCreate.getClassLoader(), new Class[] { classToCreate }, handler);
	}

	/**
	 * Create a new object that implements a particular class or interface with
	 * underlying storage using a Generic Transfer Object (GTO).
	 * 
	 * @param classToCreate The class or interface to implement.
	 * 
	 * @return A new object that implements the specified class.
	 */
	public static Object newInstance(Class<?> classToCreate) {
		GTOProxyHandler handler = new GTOProxyHandler();
		return Proxy.newProxyInstance(classToCreate.getClassLoader(), new Class[] { classToCreate }, handler);
	}

	/**
	 * This is where all the "magic" happens. This method is called every time a
	 * method on a proxy class is called. This implementation of the invoke()
	 * method EXPECTS only getXXXX() and setXXXX() method calls. Any other call
	 * will return a null. The getXXXX() methods store objects into the
	 * underlying GTO and setXXXX() methods return objects from the GTO.
	 * 
	 * @param proxy The proxy object that was called
	 * @param method The method on the proxy object that was called.
	 * @param args The arguments that were passed to the proxy object.
	 * 
	 * @return The return object from the method call (can be null).
	 */
	public Object invoke(Object proxy, Method method, Object[] args) {

		String sMethodName = method.getName();

		if (sMethodName.equals("hashCode"))
			return (new Integer(_gto.getMap().hashCode()));
		if (sMethodName.equals("equals"))
			return (new Boolean(_gto.getMap().equals(((TransferObject) args[0]).getGTO().getMap())));

		if ("toString".equals(sMethodName)) {
			return _gto.toString();
		}

		if (sMethodName.startsWith("set")) {
			// Set the action attribute for the GTO?
			if (sMethodName.equals("setAction")) {
				_gto.setAction((String) args[0]);
				return (_gto);
			}
			// Set the action attribute for the GTO?
			if (sMethodName.equals("setService")) {
				_gto.setService((String) args[0]);
				return (_gto);
			}
			String sAttribute = sMethodName.substring(3, 4).toLowerCase() + sMethodName.substring(4);
			_gto.put(sAttribute, args[0]);
			return (null);
		}
		// Is this a getter?
		else if (sMethodName.startsWith("get")) {
			// Special case. If this 'getter' is trying to get the generic
			// transfer
			// object from the base class, then make sure to catch it here.
			if (sMethodName.equals("getGTO")) {
				return (_gto);
			}
			// Special case handler for getting a GTO's action attribute.
			else if (sMethodName.equals("getAction")) {
				return (_gto.getAction());
			} else if (sMethodName.equals("getService")) {
				return (_gto.getService());
			}
			String sAttribute = sMethodName.substring(3, 4).toLowerCase() + sMethodName.substring(4);
			Object obj = _gto.get(sAttribute);
			if (obj == null && method.getReturnType().isPrimitive()) {
				// throw (new UnsupportedOperationException ("error." + sAttribute + "NotFound."));
				// quick default boxed type returner
				obj = primitiveDefaults.get(method.getReturnType());
				_gto.put(sAttribute, obj);
			}
			return (obj);
		}
		// This is a method used for adding error messages to the underlying GTO.
		else if (sMethodName.equals("addErrorMessage")) {
			_gto.addErrorMessage((String) args[0]);
		}

		// This adds regular messages to the underlying GTO.
		else if (sMethodName.equals("addMessage")) {
			_gto.addMessage((String) args[0]);
		}
		return (null);
	}

}