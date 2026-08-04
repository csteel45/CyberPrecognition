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

package com.saggs.cyberprecog.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.BasicStatusLine;

import com.saggs.cyberprecog.utils.TransferObject;

/**
 * @author Christopher Steel _ Software AG Government Solutions
 * 
 * @since Apr 18, 2014 5:11:03 PM
 * @version 1.0
 */
public interface NetworkPacketDTO extends TransferObject {

	public String getHeaderUaColor();
	public void  setHeaderUaColor(String val);
	public Integer getDnsQueryid();
	public void  setDnsQueryid(Integer val);
	public Long getTs();
	public void  setTs(Long val);
	public String getHeaderRefresh();
	public void  setHeaderRefresh(String val);
	public Boolean getTcpFlagPsh();
	public void  setTcpFlagPsh(Boolean val);
	public Boolean getTcpFlagRst();
	public void  setTcpFlagRst(Boolean val);
	public String getHeaderRequeststartsec();
	public void  setHeaderRequeststartsec(String val);
	public String getDst();
	public void  setDst(String val);
	public String getHeaderAcceptCharset();
	public void  setHeaderAcceptCharset(String val);
	public Integer getSrcPort();
	public void  setSrcPort(Integer val);
	public String getHeaderSetCookie();
	public void  setHeaderSetCookie(String val);
	public Long getTsmicros();
	public void  setTsmicros(Long val);
	public String getHeaderXPad();
	public void  setHeaderXPad(String val);
	public Integer getTcpHeaderLength();
	public void  setTcpHeaderLength(Integer val);
	public String getHeaderXArtHeight();
	public void  setHeaderXArtHeight(String val);
	public Boolean getTcpFlagEce();
	public void  setTcpFlagEce(Boolean val);
	public String getHeaderReplyTo();
	public void  setHeaderReplyTo(String val);
	public Integer getUdpsum();
	public void  setUdpsum(Integer val);
	public BasicRequestLine getHeaderRequestLine();
	public void  setHeaderRequestLine(BasicRequestLine val);
	public String getHeaderContentLength();
	public void  setHeaderContentLength(String val);
	public String getDnsFlags();
	public void  setDnsFlags(String val);
	public String getHeaderAcceptEncoding();
	public void  setHeaderAcceptEncoding(String val);
	public BigDecimal getTsUsec();
	public void  setTsUsec(BigDecimal val);
	public String getHeaderContentType();
	public void  setHeaderContentType(String val);
	public Integer getDnsQtype();
	public void  setDnsQtype(Integer val);
	public String getDnsQuestion();
	public void  setDnsQuestion(String val);
	public String getHeaderCookie();
	public void  setHeaderCookie(String val);
	public ArrayList<?> getDnsAdditional();
	public void  setDnsAdditional(ArrayList<?> val);
	public String getDnsOpcode();
	public void  setDnsOpcode(String val);
	public String getHeaderPicsLabel();
	public void  setHeaderPicsLabel(String val);
	public String getHeaderAllow();
	public void  setHeaderAllow(String val);
	public Boolean getTcpFlagAck();
	public void  setTcpFlagAck(Boolean val);
	public Long getTcpSeq();
	public void  setTcpSeq(Long val);
	public ArrayList<?> getDnsAuthority();
	public void  setDnsAuthority(ArrayList<?> val);
	public ArrayList<?> getDnsAnswer();
	public void  setDnsAnswer(ArrayList<?> val);
	public String getHeaderContentLanguage();
	public void  setHeaderContentLanguage(String val);
	public String getHeaderVersion();
	public void  setHeaderVersion(String val);
	public String getHeaderNegotiate();
	public void  setHeaderNegotiate(String val);
	public String getHeaderExpires();
	public void  setHeaderExpires(String val);
	public String getHeaderKeepAlive();
	public void  setHeaderKeepAlive(String val);
	public BasicStatusLine getHeaderStatusLine();
	public void  setHeaderStatusLine(BasicStatusLine val);
	public Boolean getTcpFlagFin();
	public void  setTcpFlagFin(Boolean val);
	public String getHeaderVia();
	public void  setHeaderVia(String val);
	public String getHeaderUaPixels();
	public void  setHeaderUaPixels(String val);
	public String getHeaderWindowTarget();
	public void  setHeaderWindowTarget(String val);
	public String getHeaderCacheControl();
	public void  setHeaderCacheControl(String val);
	public String getHeaderContentBase();
	public void  setHeaderContentBase(String val);
	public String getHeaderDate();
	public void  setHeaderDate(String val);
	public Boolean getDnsQr();
	public void  setDnsQr(Boolean val);
	public String getHeaderAcceptLanguage();
	public void  setHeaderAcceptLanguage(String val);
	public String getHeaderPragma();
	public void  setHeaderPragma(String val);
	public Integer getDstPort();
	public void  setDstPort(Integer val);
	public String getHttpHeaders();
	public void  setHttpHeaders(String val);
	public String getHeaderContentLocation();
	public void  setHeaderContentLocation(String val);
	public String getHeaderServer();
	public void  setHeaderServer(String val);
	public String getHeaderMimeVersion();
	public void  setHeaderMimeVersion(String val);
	public String getHeaderRequeststartusec();
	public void  setHeaderRequeststartusec(String val);
	public String getHeaderLink();
	public void  setHeaderLink(String val);
	public Integer getIpHeaderLength();
	public void  setIpHeaderLength(Integer val);
	public String getHeaderWwwAuthenticate();
	public void  setHeaderWwwAuthenticate(String val);
	public Integer getReassembledFragments();
	public void  setReassembledFragments(Integer val);
	public Boolean getTcpFlagNs();
	public void  setTcpFlagNs(Boolean val);
	public String getHeaderConnection();
	public void  setHeaderConnection(String val);
	public String getHeaderUaOs();
	public void  setHeaderUaOs(String val);
	public String getSrc();
	public void  setSrc(String val);
	public String getDnsQname();
	public void  setDnsQname(String val);
	public String getHeaderVary();
	public void  setHeaderVary(String val);
	public String getProtocol();
	public void  setProtocol(String val);
	public String getHeaderXArtWidth();
	public void  setHeaderXArtWidth(String val);
	public String getHeaderLocation();
	public void  setHeaderLocation(String val);
	public String getHeaderUri();
	public void  setHeaderUri(String val);
	public String getHeaderAcceptRanges();
	public void  setHeaderAcceptRanges(String val);
	public String getHeaderUserAgent();
	public void  setHeaderUserAgent(String val);
	public String getHeaderClientDate();
	public void  setHeaderClientDate(String val);
	public String getHeaderTitle();
	public void  setHeaderTitle(String val);
	public String getHeaderReferer();
	public void  setHeaderReferer(String val);
	public Boolean getTcpFlagUrg();
	public void  setTcpFlagUrg(Boolean val);
	public Integer getLen();
	public void  setLen(Integer val);
	public Integer getTtl();
	public void  setTtl(Integer val);
	public String getHeaderContentEncoding();
	public void  setHeaderContentEncoding(String val);
	public String getHeaderHost();
	public void  setHeaderHost(String val);
	public String getHeaderUaCpu();
	public void  setHeaderUaCpu(String val);
	public Boolean getTcpFlagSyn();
	public void  setTcpFlagSyn(Boolean val);
	public String getHeaderSecurityScheme();
	public void  setHeaderSecurityScheme(String val);
	public Integer getUdpLength();
	public void  setUdpLength(Integer val);
	public Boolean getTcpFlagCwr();
	public void  setTcpFlagCwr(Boolean val);
	public String getDnsRcode();
	public void  setDnsRcode(String val);
	public String getHeaderEtag();
	public void  setHeaderEtag(String val);
	public String getHeaderCacheLastChecked();
	public void  setHeaderCacheLastChecked(String val);
	public String getHeaderAccept();
	public void  setHeaderAccept(String val);
	public String getHeaderLastModified();
	public void  setHeaderLastModified(String val);
	public String getHttpType();
	public void  setHttpType(String val);
	public String getHeaderProxyAgent();
	public void  setHeaderProxyAgent(String val);
	public Integer getIpVersion();
	public void  setIpVersion(Integer val);
	public Long getTcpAck();
	public void  setTcpAck(Long val);
	public byte[] getPayload();
	public void setPayload(byte[] val);
	public long getPayloadLength();
	public void setPayloadLength(long val);
	public String getDateTime();
	public void setDateTime(String val);
	public String getLabel();
	public void setLabel(String val);
	public long getId();
	public void setId(long val);
}
