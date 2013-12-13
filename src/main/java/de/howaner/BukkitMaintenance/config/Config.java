package de.howaner.BukkitMaintenance.config;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Config {
	public static String BIND_ADDRESS = "0.0.0.0";
	public static int BIND_PORT = 25565;
	public static String KICK_MESSAGE = "§4Wartung! Kommen sie später wieder!";
	public static String VERSION_NAME = "Wartung";
	public static String MOTD = "Wir sind bald wieder für sie da!";
	public static String MULTILINE_MOTD = "Wartungsmodus!\nBald können sie wieder rein!";
	
	public static void loadConfig() {
		try {
			YamlReader reader = new YamlReader(new FileReader("config.yml"));
			
			Map map = (Map) reader.read();
			BIND_ADDRESS = (String) map.get("BindAddress");
			BIND_PORT = Integer.valueOf( (String) map.get("BindPort") );
			KICK_MESSAGE = (String) map.get("KickMessage");
			VERSION_NAME = (String) map.get("Version");
			MOTD = (String) map.get("Motd");
			MULTILINE_MOTD = (String) map.get("MultilineMotd");
			
			reader.close();
		} catch (Exception e) {
			System.err.println("Error while loading the Config: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void saveConfig() {
		try {
			YamlWriter writer = new YamlWriter(new FileWriter("config.yml"));
			
			Map map = new HashMap();
			map.put("BindAddress", BIND_ADDRESS);
			map.put("BindPort", BIND_PORT);
			map.put("KickMessage", KICK_MESSAGE);
			map.put("Version", VERSION_NAME);
			map.put("Motd", MOTD);
			map.put("MultilineMotd", MULTILINE_MOTD);
			
			writer.write(map);
			writer.close();
		} catch (Exception e) {
			System.err.println("Error while saving the Config: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
