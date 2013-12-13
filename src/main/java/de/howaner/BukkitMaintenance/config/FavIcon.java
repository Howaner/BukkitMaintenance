package de.howaner.BukkitMaintenance.config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.howaner.BukkitMaintenance.util.ImageUtils;

public class FavIcon {
	
	public static String FavIcon = "";
	
	public static void loadFavIcon() {
		File icon = new File("server-icon.png");
		if (icon.exists()) {
			BufferedImage image = null;
			try { 
				 image = ImageIO.read(icon); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
			
			FavIcon = ImageUtils.encodeToString(image, "png");
		}
	}

}
