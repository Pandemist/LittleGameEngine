package game.Rendering;

import java.awt.image.BufferedImage;

public class RenderEntry {
	private BufferedImage im;
	private int posX;
	private int posY;

	public RenderEntry(BufferedImage im, int posX, int posY) {
		this.im = im;
		this.posX = posX;
		this.posY = posY;
	}

	public BufferedImage getIm() {
		return im;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
}
