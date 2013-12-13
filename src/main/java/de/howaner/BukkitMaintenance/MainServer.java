package de.howaner.BukkitMaintenance;

import com.google.gson.Gson;

import de.howaner.BukkitMaintenance.config.Config;
import de.howaner.BukkitMaintenance.util.ImageUtils;
import de.howaner.BukkitMaintenance.util.PacketListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class MainServer {
	public static MainServer instance;
	private Thread thread;
	public Gson gson;
	private String icon = "";
	
	public static void main(String[] args) {
		System.out.println("Started BukkitMaintenance v1.1!");
		instance = new MainServer();
		instance.load();
	}
	
	public void load() {
		this.extractLibs();
		this.gson = new Gson();
		
		if (!new File("config.yml").exists()) Config.saveConfig();
		Config.loadConfig();
		
		//Servericon
		if (new File("server-icon.png").exists()) {
			try {
				BufferedImage image = ImageIO.read(new File("server-icon.png"));
				this.icon = "data:image/png;base64," + ImageUtils.encodeToString(image, "png");
				System.out.println("Found Server-Icon.");
			} catch (Exception e) {
				this.icon = "";
				System.err.println("Can't load Server-Icon: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		this.thread = new PacketListener(this);
		this.thread.start();
	}
	
	public void extractLibs() {
		if (!new File("lib/gson-2.2.4.jar").exists())
			this.extractLib("/lib/gson-2.2.4.jar", new File("lib/gson-2.2.4.jar"));
		if (!new File("lib/yamlbeans-1.06.jar").exists())
			this.extractLib("/lib/yamlbeans-1.06.jar", new File("lib/yamlbeans-1.06.jar"));
	}
	
	public void extractLib(String path, File to) {
		InputStream stream = this.getClass().getResourceAsStream(path);
		if (stream == null) return;
		int readBytes;
		byte[] buffer = new byte[4096];
		
		try {
			if (!to.getParentFile().exists()) to.getParentFile().mkdirs();
			if (to.exists()) to.delete();
			to.createNewFile();
			
			OutputStream out = new FileOutputStream(to);
			while ((readBytes = stream.read(buffer)) > 0)
				out.write(buffer, 0, readBytes);
			
			out.close();
			stream.close();
		} catch (Exception e) {
			System.err.println("Can't extract Library " + to.getName() + "!");
			e.printStackTrace();
		}
	}
	
	public String getIcon() {
		return this.icon;
	}
	
}
