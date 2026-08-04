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

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since May 7, 2014 7:31:38 PM
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class CyberEventSequenceImpl extends ArrayList implements CyberEventSequence {

/*	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator it = this.iterator();
		while(it.hasNext()) {
			sb.append(it.next() + ", ");
		}
		return sb.toString();
	}
*/
	
	private static final long serialVersionUID = 2258060222025541288L;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.out.println("Starting");
		CyberEventSequenceImpl impl = new CyberEventSequenceImpl();
		impl.add("A");
		impl.add("B");
		impl.add("E");
		impl.add("C");
		impl.add("D");
		System.out.println("Impl: " + impl.toString());
		Collections.sort(impl);
		System.out.println("Impl: " + impl);
	}

	@Override
	public double[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEvent(CyberEvent event) {
		// TODO Auto-generated method stub
		
	}
}
