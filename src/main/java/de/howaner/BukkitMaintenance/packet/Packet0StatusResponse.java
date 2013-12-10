package de.howaner.BukkitMaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Status Response for 1.7
 * @author franz
 */
public class Packet0StatusResponse extends Packet {
	public String a;
	
	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = this.readVarIntString(stream, 32767);
	}

	@Override
	public void write(DataOutputStream stream) throws Exception {
		this.writeVarIntString(stream, this.a);
	}

	@Override
	public int getPacketID() {
		return 0x00;  //0x00 = 0
	}
	
}
