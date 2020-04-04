package com.timofriedl.simulationbox.assets;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.timofriedl.simulationbox.display.Window;

/**
 * Utility class for asset handling.
 * 
 * @author Timo Friedl
 */
public abstract class Assets {

	/**
	 * Loads a {@link BufferedImage} from a given path.
	 * 
	 * @param path the path of the image file
	 * @return the loaded image
	 * @throws an {@link IOException} if failed to load the image
	 */
	public static BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}

	/**
	 * Crops a circle out of a given {@link BufferedImage}
	 * 
	 * @param img the input image
	 * @return a new circle-shaped image
	 */
	public static BufferedImage cropCircle(BufferedImage img) {
		if (img.getWidth() != img.getHeight())
			throw new IllegalArgumentException("Input image must be a square.");

		int diam = img.getWidth();
		final BufferedImage res = new BufferedImage(diam, diam, BufferedImage.TYPE_INT_ARGB);
		
		final Graphics2D g2 = res.createGraphics();
		Window.setupRenderingHints(g2);
		g2.setClip(new Ellipse2D.Float(0, 0, diam, diam));
		g2.drawImage(img, 0, 0, diam, diam, null);

		return res;
	}

	/**
	 * Crops a centered square from a given {@link BufferedImage}
	 * 
	 * @param img the input image
	 * @return a new square-shaped image
	 */
	public static BufferedImage cropCenterSquare(BufferedImage img) {
		return img.getWidth() > img.getHeight()
				? img.getSubimage((img.getWidth() - img.getHeight()) / 2, 0, img.getHeight(), img.getHeight())
				: img.getSubimage(0, (img.getHeight() - img.getWidth()) / 2, img.getWidth(), img.getWidth());
	}

}
