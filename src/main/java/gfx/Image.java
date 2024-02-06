package gfx;

import helper.ReadFromFile;
import helper.Utilities;
import main.GameController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class Image {
	protected int scaledFactor;
	protected BufferedImage image;
	private int imageH;
	private int imageW;

	public Image(GameController gc, String path) {
		try {
			InputStream is = ReadFromFile.getInputStream(Image.class, path);

			if (is == null) {
				throw new RuntimeException("Die Ressource '" + path + "' wurde nicht gefunden.");
			}

			BufferedImage original = ImageIO.read(is);
			imageW = original.getWidth() * gc.scale;
			imageH = original.getHeight() * gc.scale;
			this.scaledFactor = gc.scale;

			BufferedImage scaledImage = new BufferedImage(imageW, imageH, original.getType());
			Graphics2D g2 = scaledImage.createGraphics();
			g2.drawImage(original,0,0, imageW, imageH, null);
			image = scaledImage;

			imageW = image.getWidth();
			imageH = image.getHeight();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	// Getter und Setter

	public BufferedImage getImage() {
		return image;
	}

	public int getScaledFactor() {
		return scaledFactor;
	}

	public BufferedImage getImageTile(long tileId, int tileW, int tileH) {
		tileW = tileW * this.scaledFactor;
		tileH = tileH * this.scaledFactor;
		int tilesPerRow = image.getWidth() / tileW;
		long row = tileId / tilesPerRow;
		long col = tileId % tilesPerRow;
		long x = col * tileW;
		long y = row * tileH;

		return image.getSubimage((int) x, (int) y, tileW, tileH);
	}

	public BufferedImage getImageTile(int posX, int posY, int tileW, int tileH) {
		long x = (long) posX * tileW * this.scaledFactor;
		long y = (long) posY * tileH * this.scaledFactor;
		tileW *= this.scaledFactor;
		tileH *= this.scaledFactor;

		return image.getSubimage((int) x, (int) y, tileW, tileH);
	}
}
