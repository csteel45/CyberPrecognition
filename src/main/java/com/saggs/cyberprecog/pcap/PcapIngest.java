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

package com.saggs.cyberprecog.pcap;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import net.ripe.hadoop.pcap.PcapReader;
import net.ripe.hadoop.pcap.packet.Packet;

import com.google.common.base.Stopwatch;
import com.saggs.cyberprecog.cache.CacheUtils;
import com.saggs.cyberprecog.dto.NetworkPacketDTO;
import com.saggs.cyberprecog.utils.GenericTO;

/**
 * @author Christopher Steel - Software AG Government Solutions
 *
 * @since Apr 17, 2014 10:14:11 PM
 * @version 1.0
 */
public class PcapIngest {
	protected CacheUtils cache = CacheUtils.getCacheUtils();
	private Logger log = Logger.getLogger(PcapIngest.class);
	private Stopwatch watch = new Stopwatch();
	
	/**
	 * This method is meant to act as the entry point for ingesting the data.
	 * Currently, it simply loads a file, but should eventually support streaming
	 * data from a live file or alternate stream.
	 * 
	 * @param pcapReaderClass The parser implementation (i.e. HttpBodyPcapReader)
	 * @param path The path to the pcap file
	 * @throws IOException If anything goes wrong loading and parsing the file.
	 */
	public void run(String pcapReaderClass, String path) throws Exception {
		log.debug("Cache size: " + cache.getSize() + " of 1011149"); //TODO Remove second part.
		cache.clear();
		if(cache.getSize() != 1011149) {
			cache.clear();
			loadPcapFile(pcapReaderClass, path);
//			addLabels();
			//verifyCache();
		}
//		addLabels();
//		query();
		log.info("Flushing cache to disk.");
		cache.getCache().flush();
		log.info("Run complete.");
	}
	
	private void addLabels() {
		addLabel("03/10/1999 13:44:18", "172.16.112.50", "mail bomb");		
	}
		
	public void verifyCache() {
		NetworkPacketDTO dto = null;
		for(long i = 0; i < cache.getSize(); i++) {
			dto = (NetworkPacketDTO) cache.get(i);
			//log.debug(dto);
			if(dto == null) {
				log.error("DTO null for cache key: " + i);
				//break;
			}
		}	
	}
	
	public void addLabel(String dateTime, String dest, String label) {
		NetworkPacketDTO dto = null;
		long first = 0, last = 0;
		for (long i = 0; i < cache.getSize(); i++) {
			dto = (NetworkPacketDTO) cache.get(i);
			if(dto == null)
				continue;
			if(i == 0)
				first = dto.getTs();
			//if(dto.getTs() < 921091459l && dto.getTs() > 921091457) {
			//	log.info("Date time = [" + dto.getDateTime() + "]   " + dto);
			//}
			if(dto.getDateTime().equals(dateTime) && dto.getDst().equals(dest) && dto.getDstPort().equals(25) && dto.getPayloadLength() != dto.getLen()) {
				dto.setLabel(label);
				log.info("**********Added label to: " + dto);
			}
			else {
				dto.setLabel("normal");
			}
		}
		last = dto.getTs();
		log.info("First: " + first + " Last: " + last);
	}

	public void query() {
		NetworkPacketDTO dto = null;
		HashMap<String, NetworkPacketDTO> map = new HashMap<>();
		for (long i = 0; i < cache.getSize(); i++) {
			dto = (NetworkPacketDTO) cache.get(i);
			if(dto == null)
				continue;
			String key = dto.getDst() + ":" + dto.getSrc();
			
			if (dto.getSrc() != null && dto.getDst() != null && dto.getTcpSeq() != null) {
				NetworkPacketDTO last = (NetworkPacketDTO) map.get(key);
				map.put(key, dto);
				if(null == last)
					continue;
				if (last.getTcpSeq() > dto.getTcpSeq() || dto.getTcpSeq() > last.getTcpSeq() + 1000) {
					log.debug(dto.getDateTime() + " " + dto.getSrc() + "->" + dto.getDst() + "   "
							+ last.getTcpSeq() + " " + dto.getTcpSeq() + "    " + (dto.getTcpSeq() - last.getTcpSeq()) + " " + (dto.getTs() - last.getTs()));
				}
			}
		}	
	}
	
	/**
	 * Loads a Pcap file, converting packets into Generic Transfer Objects.
	 * 
	 * @param pcapReaderClass The implementation class used to parse the packets
	 * @param path The path to the pcap file.
	 */
	public void loadPcapFile(String pcapReaderClass, String path) {
		InputStream is = null;
		Long numPackets = 0l;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //Used when printing the DTO
		Calendar cal = Calendar.getInstance();
		BigDecimal multipland = new BigDecimal(1000);
		
		watch.start(); //Start a watch to measure ingestion time.

		try {
			is = new FileInputStream(path);
			if (path.endsWith(".gz") || path.endsWith(".gzip")) //Read a gzip file
				is = new GZIPInputStream(is);

			PcapReader reader = initPcapReader(pcapReaderClass, new DataInputStream(is));
			
			for (Packet packet : reader) {
				GenericTO gto = new GenericTO("PcapIngest", "getPacket", packet, 1.0f);
				NetworkPacketDTO dto = (NetworkPacketDTO) gto.getDTO(NetworkPacketDTO.class); //Store as DTOs to make life easier
				//Convert the date to a Java-friendly format (milliseconds)
				long timeStamp = multipland.multiply(dto.getTsUsec()).longValue();
				cal.setTimeInMillis(timeStamp);
				//Save the String, we can convert the uSecs to mSecs later if needed, no need to store both.
				dto.setDateTime(format.format(cal.getTime()));
				if(null == dto.getPayload()) {
					//log.warn("Payload null for packet: " + numPackets + ". Setting to byte[0].");
					dto.setPayload(new byte[0]);
				}
				dto.setPayloadLength(dto.getPayload().length);
				dto.setId(numPackets);
				cache.put(numPackets, dto); //Use the packet number as the key for now. 

				numPackets++;
				if(log.isDebugEnabled() && numPackets % 1000 == 0) { //Periodically output progress
					log.debug("Loaded number of packets: " + numPackets);
				}
			}
			watch.stop();
			log.debug("Loaded number of packets: " + numPackets);
			long elapsed = watch.elapsed(TimeUnit.SECONDS);
			log.info("Elapsed time = " + elapsed + " seconds. Rate = " + numPackets/elapsed + " packets/sec. Cache size = " + cache.getSize());
		} 
		catch(Exception e) {
			log.error("Exception loading pcap file: " + e, e);
		}	
		finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	private PcapReader initPcapReader(String className, DataInputStream is) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends PcapReader> pcapReaderClass = (Class<? extends PcapReader>)Class.forName(className);
			Constructor<? extends PcapReader> pcapReaderConstructor = pcapReaderClass.getConstructor(DataInputStream.class);
			return pcapReaderConstructor.newInstance(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		PcapIngest runner = null;
		try {
			runner = new PcapIngest();
			// System.out.println(runner.fixName("tcp_ack-seq"));
			runner.run("com.saggs.cyberprecog.pcap.HttpBodyPcapReader", "data/inside-tcpdump.pcap");
			return;
		} catch (Exception e) {
			if(runner != null && runner.log != null)
				runner.log.error(e,e);
		}
	}

}
