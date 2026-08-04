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
 * PURPOSE, OR NON-INFRINGEMENT. SOFTWARE AG GS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.saggs.cyberprecog.events;

import com.saggs.cyberprecog.utils.DoubleConverter;

/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since Apr 10, 2014 3:42:50 PM
 * @version 1.0
 */
public class NetworkEventSequence extends CyberEventSequenceImpl {
	String	ipv4_src_addr;
	String	ipv4_dst_addr;
	String	l4_dst_port;
	String	l4_src_port;
	String	f_sum_out_pkts;
	double timestamp = System.currentTimeMillis() * 1.0d; //FIXME: get the real timestamp
	
	/* (non-Javadoc)
	 * @see com.saggs.cyberprecog.events.CyberEventSequence#getData()
	 */
	@Override
	public double[] getData() {
		double[] data = {DoubleConverter.convertIPv4(ipv4_src_addr),
				DoubleConverter.convertIPv4(ipv4_dst_addr),
				DoubleConverter.convertIPv4(l4_src_port),
				DoubleConverter.convertIPv4(l4_dst_port)};
		return data;
	}

}
