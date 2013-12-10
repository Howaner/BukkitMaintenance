package de.howaner.BukkitMaintenance.util;

public class PingUtil {
	
	public static String createPingString(int protocolVersion, String version, String motd, int players, int slots) {
		return new StringBuilder()
				.append("§1")
				.append("\0").append(protocolVersion)
				.append("\0").append(version)
				.append("\0").append(motd)
				.append("\0").append(players)
				.append("\0").append(slots)
			.toString();
	}
	
}
