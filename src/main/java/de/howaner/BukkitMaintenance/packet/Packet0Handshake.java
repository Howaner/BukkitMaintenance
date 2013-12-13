package de.howaner.BukkitMaintenance.packet;

import de.howaner.BukkitMaintenance.util.Varint;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Handshake for 1.7
 * @author franz
 */
public class Packet0Handshake extends Packet {
	public int a; //Protocol Version
	public String b; //Server Address
	public int c; //Server Port
	public int d; //Next State ( 1 = Status | 2 = Login )
	
	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = Varint.readVarInt(stream);
		this.b = this.readVarIntString(stream, 255);
		this.c = stream.readUnsignedShort();
		this.d = Varint.readVarInt(stream);
	}

	@Override
	public void write(DataOutputStream stream) throws Exception {
		Varint.writeVarInt(stream, this.a);
		this.writeVarIntString(stream, this.b);
		stream.writeShort(this.c);
		Varint.writeVarInt(stream, this.d);
	}

	@Override
	public int getPacketID() {
		return 0x00;  //0x00 = 0
	}
	
}
