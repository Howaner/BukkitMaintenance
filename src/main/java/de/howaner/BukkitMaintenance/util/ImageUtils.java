package de.howaner.BukkitMaintenance.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;

/**
 * Image Utils
 * @author Bai Ben
 */
public class ImageUtils {
	
	/**
	 * Encode image to string
	 */
	public static String encodeToString(BufferedImage image, String type) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, type, bos);
		
		byte[] imageBytes = bos.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		String imageString = encoder.encode(imageBytes);
		
		bos.close();
        return imageString;
    }

}
