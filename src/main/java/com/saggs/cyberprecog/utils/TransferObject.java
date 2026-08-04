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

/**
 * This is the base interface that wraps around the GenericTO object.  All
 * business object interfaces should extend this interface to provide access
 * to the underlying GTO objects.
 *
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since Apr 10, 2014 3:24:33 PM
 * @version 1.0
 */
public interface TransferObject extends java.io.Serializable
{
  /**
   * Add an error message string to the GTO.
   *
   * @param sErrorMessage The error message to add to the GTO.
   */
  public void       addErrorMessage (String sErrorMessage);

  /**
   *  Add a message string to the GTO.
   *
   * @param message The message to add to the GTO.
   */
  public void       addMessage( String message );
  /**
   * Get the "Action" attribute of the GTO.
   *
   * @return  The action attribute of the underlying GTO.
   */
  public String     getAction ();

  /**
   * Set the "Action" attribute of the GTO.
   *
   * @param sAction The action attribute of the underlying GTO.
   */
  public void       setAction (String sAction);

  /**
   * Get the underlying GTO from the object that is implementing this
   * interface.
   *
   * @return  The underlying GTO object that the class that implements this
   *          interface is referencing.
   */
  public GenericTO  getGTO ();

  /**
   * Set the "Service" attribute of the GTO.
   *
   * @return  The underlying GTO object that the class that implements this
   *          interface is referencing.
   */
  public void  setService (String sService);

  /**
   * Get the "Service" attribute of the GTO.
   *
   * @return  The service attribute of the underlying GTO.
   */
  public String     getService();
}