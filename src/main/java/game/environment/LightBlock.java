package game.environment;

import game.Rendering.Renderer;
import main.GameController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LightBlock extends Light{
	private Rectangle rect;

	public LightBlock(GameController gc, int radius, Color color) {
		super(gc, radius, color);

		super.color = new Color(255, 255, 0, 100);

		this.rect = new Rectangle(0,0,15,15);

		prepareLight();
	}

	public LightBlock(GameController gc, int radius, Color color, int puls, double pulsBeat) {
		super(gc, radius, color, puls, pulsBeat);

		super.color = new Color(255, 255, 0, 100);

		this.rect = new Rectangle(0,0,15,15);

		prepareLight();
	}

	private void prepareLight() {
		int width = 2 * radius + rect.width;
		int height = 2 * radius + rect.height;

		BufferedImage newLight = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newLight.createGraphics();

		for (int i = 0; i <= radius; i++) {
			int alpha = (int) ((1.0 / radius) * color.getAlpha());
			Color fadedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
			g2d.setColor(fadedColor);

			int x = i;
			int y = i;
			int w = width - 2 * i;
			int h = height - 2 * i;

			g2d.fillRoundRect(x, y, w, h, radius, radius);
		}
		g2d.dispose();

		light = newLight;
	}

	@Override
	public void update(double timeGap) {
		super.update(timeGap);
	//	prepareLight();
	}

	@Override
	public void draw(Renderer r) {
		super.draw(r);
	}

	@Override
	public void setCenter(Point center) {
		super.setCenter(center);
	}
}
