package gfx;

import main.GameController;

import java.awt.image.BufferedImage;

public class ImageTile extends Image{
	private int tileW;
	private int tileH;

	public ImageTile(GameController gc, String path, int tileW, int tileH) {
		super(gc, path);
		this.tileW = tileW;
		this.tileH = tileH;
	}

	// Getter und Setter

	public BufferedImage getImageTile(long tileId) {
		return super.getImageTile(tileId, tileW, tileH);
	}

	public BufferedImage getImageTile(int posX, int posY) {
		return super.getImageTile(posX, posY, tileW, tileH);
	}

	public void updateTileW(int tileW) {
		this.tileW = tileW;
	}

	public void updateTileH(int tileH) {
		this.tileH = tileH;
	}
}
