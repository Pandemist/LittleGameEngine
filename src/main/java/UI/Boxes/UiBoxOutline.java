package UI.Boxes;

import java.awt.*;

public class UiBoxOutline extends UiBox{
	protected Color bg = new Color(0, 0, 0, 150);
	protected Color frame = new Color(255, 255, 255, 150);

	public UiBoxOutline() {
		super();
	}

	public UiBoxOutline(Point bounds) {
		super(bounds);
	}

	public void update() {
		super.update();
	}

	public void draw(Graphics2D g2) {
		super.draw(g2);

		g2.setColor(bg);
		g2.fillRoundRect(pos.x, pos.y, bounds.x, bounds.y, 35, 35);

		g2.setColor(frame);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(pos.x+5, pos.y+5, bounds.x-10, bounds.y-10, 25, 25);
	}
}
