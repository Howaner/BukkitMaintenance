package de.howaner.BukkitMaintenance.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Login Start Paket for 1.7
 * @author franz
 */
public class Packet0LoginStart extends Packet {
	public String a; //Playername

	@Override
	public void read(DataInputStream stream) throws Exception {
		this.a = this.readVarIntString(stream, 16);
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
