package com.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
   The ImageManager class manages the loading and processing of images.
*/

public class ImageManager {
      
   	public ImageManager () {

	}

	public static BufferedImage loadImage(String path) {
    try {
        var stream = ImageManager.class.getResourceAsStream(path);

        if (stream == null) {
            System.out.println("STREAM IS NULL for: " + path);
            return null;
        }

        return ImageIO.read(stream);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

	public static BufferedImage loadBufferedImage(String filename) {
		BufferedImage bi = null;

		File file = new File (filename);
		try {
			bi = ImageIO.read(file);
		}
		catch (IOException ioe) {
			System.out.println ("Error opening file " + filename + ":" + ioe);
		}
		return bi;
	}


  	// make a copy of the BufferedImage src

	public static BufferedImage copyImage(BufferedImage src) {
		if (src == null)
			return null;


		int imWidth = src.getWidth();
		int imHeight = src.getHeight();

		BufferedImage copy = new BufferedImage (imWidth, imHeight,
							BufferedImage.TYPE_INT_ARGB);

    		Graphics2D g2d = copy.createGraphics();

    		// copy image
    		g2d.drawImage(src, 0, 0, null);
    		g2d.dispose();

    		return copy; 
	  }

}



