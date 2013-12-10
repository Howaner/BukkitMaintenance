package de.howaner.BukkitMaintenance.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Config {
	public static String KICK_MESSAGE = "§4Wartung! Kommen sie später wieder!";
	public static String VERSION_NAME = "Wartung";
	public static String MOTD = "Wir sind bald wieder für sie da!";
	public static String MULTILINE_MOTD = "Wartungsmodus!\nBald können sie wieder rein!";
	public static int ONLINE_PLAYERS = 0;
	public static int MAX_PLAYERS = 0;
	
	public static void loadConfig() {
		try {
			YamlReader reader = new YamlReader(new FileReader("config.yml"));
			
			Map map = (Map) reader.read();
			KICK_MESSAGE = (String) map.get("KickMessage");
			VERSION_NAME = (String) map.get("Version");
			MOTD = (String) map.get("Motd");
			MULTILINE_MOTD = (String) map.get("MultilineMotd");
			ONLINE_PLAYERS = Integer.parseInt( (String) map.get("OnlinePlayers") );
			MAX_PLAYERS =  Integer.parseInt( (String) map.get("Slots") );
			
			reader.close();
		} catch (Exception e) {
			System.err.println("Can't load config!");
			e.printStackTrace();
		}
	}
	
	public static void saveConfig() {
		try {
			YamlWriter writer = new YamlWriter(new FileWriter("config.yml"));
			
			Map map = new HashMap();
			map.put("KickMessage", KICK_MESSAGE);
			map.put("Version", VERSION_NAME);
			map.put("Motd", MOTD);
			map.put("MultilineMotd", MULTILINE_MOTD);
			map.put("OnlinePlayers", ONLINE_PLAYERS);
			map.put("Slots", MAX_PLAYERS);
			
			writer.write(map);
			writer.close();
		} catch (Exception e) {
			System.err.println("Can't save config!");
			e.printStackTrace();
		}
	}
	
}
