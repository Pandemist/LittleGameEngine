package game.environment;

import main.GameController;
import game.Rendering.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LightSource extends Light{

	public LightSource(GameController gc, int radius, Color color) {
		super(gc, radius, color);

		prepareLight();
	}

	public LightSource(GameController gc, int radius, Color color, int puls, double pulsBeat) {
		super(gc, radius, color, puls, pulsBeat);

		prepareLight();
	}

	private void prepareLight() {
		BufferedImage newLight = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newLight.createGraphics();

		for (int i = 0; i <= radius; i++) {
			int alpha = (int) ((1.0 / radius) * color.getAlpha());
			Color fadedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
			g2d.setColor(fadedColor);

			g2d.fillOval(i, i, (2 * radius) - (2 * i), (2 * radius) - (2 * i));
		}
		g2d.dispose();

		light = newLight;
	}


	@Override
	public void update(double timeGap) {
		super.update(timeGap);
		prepareLight();
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
