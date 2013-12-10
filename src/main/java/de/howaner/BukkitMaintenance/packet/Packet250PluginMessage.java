package de.howaner.BukkitMaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet250PluginMessage extends Packet {
	public String a; //Channel
	public int b;    //Length
	public byte[] c; //Data
	
	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = this.readString(stream, 20);
		this.b = stream.readShort();
		if (this.b > 0 && this.b < 32767) {
			this.c = new byte[this.b];
			stream.readFully(this.c);
		}
	}

	@Override
	public void write(DataOutputStream stream) throws Exception {
		this.writeString(stream, this.a);
		stream.writeShort((short)this.b);
		if (this.c != null)
			stream.write(this.c);
	}

	@Override
	public int getPacketID() {
		return 0xFA;  //0xFA = 250
	}
	
}
