package de.howaner.BukkitMaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet2Handshake extends Packet {
	public byte a; //Protocol Version
	public String b; //Username
	public String c; //Server Host
	public int d; //Server Port
	
	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = stream.readByte();
		this.b = this.readString(stream, 16);
		this.c = this.readString(stream, 255);
		this.d = stream.readInt();
	}

	@Override
	public void write(DataOutputStream stream) throws Exception {
		stream.writeByte(this.a);
		this.writeString(stream, this.b);
		this.writeString(stream, this.c);
		stream.writeInt(this.d);
	}

	@Override
	public int getPacketID() {
		return 0x02;  //0x02 = 2
	}
	
}
