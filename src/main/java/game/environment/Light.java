package game.environment;

import game.Rendering.Renderer;
import main.GameController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Light{
	protected final GameController gc;
	protected Point center;
	protected final int baseRadius;
	protected int radius;
	protected Color color;
	private int puls = -1;
	protected double pulsBeat;
	protected double timer;

	protected BufferedImage light;

	public Light(GameController gc, int radius, Color color) {
		this.gc = gc;
		this.baseRadius = radius;
		this.radius = radius;
		this.color = color;
		this.center = new Point(0, 0);

		prepareLight();
	}

	public Light(GameController gc, int radius, Color color, int puls, double pulsBeat) {
		this(gc, radius, color);
		this.puls = puls;
		this.pulsBeat = pulsBeat;
	}

	private void prepareLight() {

	}

	public void update(double timeGap) {
		if(puls > 0) {
	//		timer += timeGap * pulsBeat;
			if(timer > (Math.PI * 2)) {
				timer -= (Math.PI*2);
			}
			radius = (int) (baseRadius + (Math.sin(timer) * (puls / 2)));
		}
	}

	public void draw(Renderer r) {
		r.addLightSources(light, center.x - radius, center.y - radius);
	}

	public void setCenter(Point center) {
		this.center = center;
	}
}
