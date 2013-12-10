package de.howaner.BukkitMaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Disconnect Packet for 1.7
 * @author franz
 */
public class Packet0Disconnect extends Packet {
	public String a;
	public String text;
	
	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = this.readVarIntString(stream, 256);
		//this.a = this.readString(stream, 256);
	}

	@Override
	public void write(DataOutputStream stream) throws Exception {
		this.writeVarIntString(stream, this.a);
		//this.writeString(stream, this.a);
	}

	@Override
	public int getPacketID() {
		return 0x00;  //0x00 = 0
	}
	
}
