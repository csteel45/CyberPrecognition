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

package com.saggs.cyberprecog.classification;

import com.saggs.cyberprecog.events.CyberEventSequence;

/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since Apr 10, 2014 3:21:04 PM
 * @version 1.0
 */
public interface AttackPatternClassifier {

	public AttackPattern classify(CyberEventSequence sequence);
	
	
}
