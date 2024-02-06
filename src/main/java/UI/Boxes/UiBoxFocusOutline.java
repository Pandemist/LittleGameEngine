package UI.Boxes;

import java.awt.*;

public class UiBoxFocusOutline extends UiBox{
	protected Color frame = new Color(255, 255, 255);
	protected int alphaFucused = 220;

	public UiBoxFocusOutline() {
		super();
	}

	public UiBoxFocusOutline(Point bounds) {
		super(bounds);
	}

	public void update() {
		super.update();
	}

	public void draw(Graphics2D g2) {
		super.draw(g2);
		if(isFocused) {
			Color currentFrame = new Color(frame.getRed(), frame.getGreen(), frame.getBlue(), alphaFucused);
			g2.setColor(currentFrame);
			g2.setStroke(new BasicStroke(5));
			g2.drawRoundRect(pos.x, pos.y, bounds.x, bounds.y, 35, 35);
		}
	}
}
