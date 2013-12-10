package de.howaner.BukkitMaintenance.packet;

import de.howaner.BukkitMaintenance.util.Varint;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

public abstract class Packet {
	
	public abstract void read(DataInputStream stream) throws Exception;
	
	public abstract void write(DataOutputStream stream) throws Exception;
	
	public abstract int getPacketID();
	
	public void writeString(DataOutputStream stream, String s) throws Exception {
		if (s.length() > 32767) return;
		stream.writeShort(s.length());
		stream.writeChars(s);
	}
	
	public String readString(DataInputStream stream, int maxLength) throws Exception {
		short length = stream.readShort();
		if (length > maxLength || length < 0) return null;
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append(stream.readChar());
		}
		return builder.toString();
	}
	
	public void writeVarIntString(DataOutputStream stream, String s) throws Exception {
		byte[] in = s.getBytes(Charset.forName("UTF-8"));
		if (in.length > 32767)
			throw new Exception("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		Varint.writeVarInt(stream, in.length);
		stream.write(in, 0, in.length);
	}
	
	public String readVarIntString(DataInputStream stream, int maxLength) throws Exception {
		int length = Varint.readVarInt(stream);
		if (length > maxLength * 4)
			throw new Exception("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
		if (length < 0)
			throw new Exception("The received encoded string buffer length is less than zero! Weird string!");
		byte[] in = new byte[length];
		stream.readFully(in);
		String s = new String(in, Charset.forName("UTF-8"));
		if (s.length() > maxLength)
			throw new Exception("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
		return s;
	}
	
}
