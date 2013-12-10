package de.howaner.BukkitMaintenance.util;

import de.howaner.BukkitMaintenance.MainServer;
import de.howaner.BukkitMaintenance.config.Config;
import de.howaner.BukkitMaintenance.json.DisconnectJSON;
import de.howaner.BukkitMaintenance.json.StatusResponseJSON;
import de.howaner.BukkitMaintenance.packet.*;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PacketListener extends Thread {
	private MainServer server;
	public static Class[] packets = new Class[256];
	
	static {
		packets[0x00] = Packet0Handshake.class; //For 1.7
		packets[0x02] = Packet2Handshake.class;
		packets[0xFA] = Packet250PluginMessage.class;
		packets[0xFE] = Packet254ServerPing.class;
		packets[0xFF] = Packet255Disconnect.class;
	}
	
	public static Packet getNewPacket(int packetID) {
		if (packetID < 0 || packetID >= packets.length || packets[packetID] == null)
			return null;
		try {
			return (Packet) packets[packetID].newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public PacketListener(MainServer server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(25565, 50, InetAddress.getByName("0.0.0.0"));
			
			Socket socket;
			while ((socket = server.accept()) != null) {
				try {
					DataInputStream reader = new DataInputStream(socket.getInputStream());

					int packetID = reader.readUnsignedByte();
					if (packetID == 15)
						packetID = Varint.readVarInt(reader);

					Packet packet = getNewPacket(packetID);
					if (packet == null) {
						System.out.println("Unkown Packet ID received: " + packetID);
						reader.close();
						socket.close();
						continue;
					}

					packet.read(reader);
					DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

					if (packet instanceof Packet254ServerPing)
						this.a( (Packet254ServerPing) packet, reader, writer );
					else if (packet instanceof Packet2Handshake)
						this.a( (Packet2Handshake) packet, writer );
					else if (packet instanceof Packet0Handshake)
						this.a( (Packet0Handshake) packet, reader, writer );

					writer.flush();
					writer.close();
					reader.close();
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void a(Packet0Handshake packet, DataInputStream reader, DataOutputStream writer) throws Exception {
		System.out.println("Received 1.7 Handshake!");
		
		if (packet.d == 2) {
			DisconnectJSON json = new DisconnectJSON();
			json.setText(Config.KICK_MESSAGE);
			
			Packet0Disconnect dPacket = new Packet0Disconnect();
			dPacket.a = MainServer.instance.gson.toJson(json);
			
			Packet0LoginStart loginPacket = new Packet0LoginStart();
			loginPacket.read(reader);
			
			this.send17Packet(writer, dPacket);
		}
		
		else if (packet.d == 1) {
			Varint.readVarInt(reader); //Packet Length
			if (Varint.readVarInt(reader) != 0x00) {
				System.out.println("The Client doesn't send a Status Request Packet!");
				return;
			}
			
			StatusResponseJSON.Version version = new StatusResponseJSON.Version();
			version.setName(Config.VERSION_NAME);
			version.setProtocol(0);
			
			StatusResponseJSON.Players players = new StatusResponseJSON.Players();
			players.setOnline(Config.ONLINE_PLAYERS);
			players.setMax(Config.MAX_PLAYERS);
			players.setSample(new ArrayList<StatusResponseJSON.SamplePlayer>());
			
			StatusResponseJSON.Description description = new StatusResponseJSON.Description();
			description.setText(Config.MULTILINE_MOTD);
			
			StatusResponseJSON json = new StatusResponseJSON();
			json.setVersion(version);
			json.setPlayers(players);
			json.setDescription(description);
			
			Packet0StatusResponse statusPacket = new Packet0StatusResponse();
			statusPacket.a = MainServer.instance.gson.toJson(json);
			this.send17Packet(writer, statusPacket);
			
			
			//Ping Time Packets
			Varint.readVarInt(reader); //Packet Size
			if (Varint.readVarInt(reader) != 0x01) {
				System.out.println("Client doesn't send Ping Packet!");
				return;
			}
			Packet1Ping pingPacket = new Packet1Ping();
			pingPacket.read(reader);
			this.send17Packet(writer, pingPacket);
		}
	}
	
	public void a(Packet2Handshake packet, DataOutputStream writer) throws Exception {
		Packet255Disconnect disconnectPacket = (Packet255Disconnect) getNewPacket(0xFF);
		disconnectPacket.a = Config.KICK_MESSAGE;
		this.sendPacket(writer, disconnectPacket);
	}
	
	public void a(Packet254ServerPing packet, DataInputStream reader, DataOutputStream writer) throws Exception {
		if (packet.a != (byte)1) { //Magical Byte Check
			System.out.println("Magic Byte isn't 1!");
			return;
		}
		
		//Is the next Packet a Pluginmessage?
		if (reader.readUnsignedByte() != 0xFA) {
			System.out.println("The received Packet is not a Pluginmessage.");
			return;
		}
		
		Packet250PluginMessage pluginPacket = (Packet250PluginMessage) getNewPacket(0xFA);
		pluginPacket.read(reader);
		
		if (!pluginPacket.a.equals("MC|PingHost")) {
			System.out.println("Received bad channel: " + pluginPacket.a);
			return;
		}
		
		Packet255Disconnect responsePacket = new Packet255Disconnect();
		responsePacket.a = PingUtil.createPingString(0, Config.VERSION_NAME, Config.MOTD, Config.ONLINE_PLAYERS, Config.MAX_PLAYERS);
		
		this.sendPacket(writer, responsePacket);
	}
	
	public void send17Packet(DataOutputStream stream, Packet packet) throws Exception {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream packetStream = new DataOutputStream(b);
		Varint.writeVarInt(packetStream, packet.getPacketID());
		packet.write(packetStream);
		
		byte[] out = b.toByteArray();
		Varint.writeVarInt(stream, out.length);
		stream.write(out);
	}
	
	public void sendPacket(DataOutputStream stream, Packet packet) throws Exception {
		stream.writeByte(packet.getPacketID());
		packet.write(stream);
	}
	
}
