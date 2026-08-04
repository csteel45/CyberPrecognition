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

package com.saggs.cyberprecog.utils;


/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since Apr 30, 2014 3:52:40 PM
 * @version 1.0
 */
public class DoubleConverter {

	public static double convertIPv4(String ip) {
		String stripped = ip.replace(".", "");
		//System.out.println("Stripped IP = " + stripped);
		double val = Double.parseDouble(stripped);
		//System.out.println("Stripped IP val = " + val);
		return val;
	}

	public static double ipToDouble(String ipAddress) {
		long result = 0;
		String[] atoms = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {
			result += (Long.parseLong(atoms[3 - i]) << (i * 8));
		}

		//return result & 0xFFFFFFFF;
		return result * 1.0d;
	}

	public static String doubleToIp(double ip) {
		StringBuilder sb = new StringBuilder(15);
		long ipL = (long)ip;
		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ipL & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}

			ipL >>= 8;
		}

		return sb.toString();
	}	

	public static void main(String[] args) {
		DoubleConverter.convertIPv4("192.168.001.001");
		double ip1 = DoubleConverter.ipToDouble("192.0.1.1");
		System.out.println("IP to long val = " + DoubleConverter.ipToDouble("192.0.1.1"));
		System.out.println("IP to long val = " + DoubleConverter.ipToDouble("192.167.1.1"));
		System.out.println("IP to long val = " + DoubleConverter.doubleToIp(ip1));
	}
}
