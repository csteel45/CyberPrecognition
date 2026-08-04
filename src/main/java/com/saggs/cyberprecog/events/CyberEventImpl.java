/*
 * Copyright 2014 Software AG Government Solutions
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Softwre AG
 * Government Solutions ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Software AG Government Soltuions.
 *
 * SOFTWARE AG GS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SOFTWARE AG GS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.saggs.cyberprecog.events;

/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since May 7, 2014 9:43:57 PM
 * @version 1.0
 */
public class CyberEventImpl implements CyberEvent {

	/* (non-Javadoc)
	 * @see com.saggs.cyberprecog.events.CyberEvent#getTimeStamp()
	 */
	@Override
	public long getTimeStamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.saggs.cyberprecog.events.CyberEvent#setTimeStamp(long)
	 */
	@Override
	public void setTimeStamp(long val) {
		// TODO Auto-generated method stub

	}
	
	public int hashCode() {
		return (int)(System.currentTimeMillis() % 1000000000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CyberEventImpl impl = new CyberEventImpl();
		System.out.println("Time = " + System.currentTimeMillis());
		System.out.println("HashCode = " + impl.hashCode());
	}

}
