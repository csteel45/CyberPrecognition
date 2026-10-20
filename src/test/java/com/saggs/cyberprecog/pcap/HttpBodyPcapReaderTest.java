package com.saggs.cyberprecog.pcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import net.ripe.hadoop.pcap.packet.Packet;

import org.junit.Test;

/**
 * Exercises the pcap parsing used by PcapIngest (HttpBodyPcapReader) against a
 * small synthetic capture, so no external pcap file or cache is required.
 */
public class HttpBodyPcapReaderTest {

	private static final byte[] PAYLOAD = "hello".getBytes();

	@Test
	public void readsSyntheticUdpPacket() throws IOException {
		HttpBodyPcapReader reader = new HttpBodyPcapReader(
				new DataInputStream(new ByteArrayInputStream(buildPcap())));

		List<Packet> packets = new ArrayList<Packet>();
		for (Packet packet : reader) {
			packets.add(packet);
		}

		assertFalse("expected at least one packet", packets.isEmpty());
		Packet packet = packets.get(0);
		assertNotNull(packet);
		assertEquals("10.0.0.1", packet.get(Packet.SRC));
		assertEquals("10.0.0.2", packet.get(Packet.DST));
		assertTrue(packet.get("payload") instanceof byte[]);
	}

	/** Builds a pcap with one Ethernet/IPv4/UDP packet carrying PAYLOAD. */
	private static byte[] buildPcap() throws IOException {
		int udpLen = 8 + PAYLOAD.length;
		ByteBuffer frame = ByteBuffer.allocate(14 + 20 + udpLen).order(ByteOrder.BIG_ENDIAN);
		frame.put(new byte[12]); // dst/src MAC
		frame.putShort((short) 0x0800); // IPv4
		frame.put((byte) 0x45); // version/IHL
		frame.put((byte) 0); // TOS
		frame.putShort((short) (20 + udpLen));
		frame.putInt(0); // id, flags, fragment offset
		frame.put((byte) 64); // TTL
		frame.put((byte) 17); // UDP
		frame.putShort((short) 0); // checksum
		frame.put(new byte[] { 10, 0, 0, 1 });
		frame.put(new byte[] { 10, 0, 0, 2 });
		frame.putShort((short) 12345); // src port
		frame.putShort((short) 54321); // dst port
		frame.putShort((short) udpLen);
		frame.putShort((short) 0); // checksum
		frame.put(PAYLOAD);

		ByteBuffer pcap = ByteBuffer.allocate(24 + 16 + frame.capacity()).order(ByteOrder.LITTLE_ENDIAN);
		pcap.putInt(0xa1b2c3d4); // magic
		pcap.putShort((short) 2);
		pcap.putShort((short) 4);
		pcap.putInt(0); // thiszone
		pcap.putInt(0); // sigfigs
		pcap.putInt(65535); // snaplen
		pcap.putInt(1); // Ethernet
		pcap.putInt(1000000); // ts_sec
		pcap.putInt(0); // ts_usec
		pcap.putInt(frame.capacity());
		pcap.putInt(frame.capacity());
		pcap.put(frame.array());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new DataOutputStream(out).write(pcap.array());
		return out.toByteArray();
	}
}
