package UI.Boxes;

import java.awt.*;

public class UiBox {
	protected Point pos;
	protected Point bounds;

	protected boolean isFocused = false;

	public UiBox() {
		pos = new Point(0, 0);
		bounds = new Point( 1, 1);
	}

	public UiBox(Point bounds) {
		pos = new Point(0, 0);
		this.bounds = bounds;
	}

	public void update() {

	}

	public void draw(Graphics2D g2) {

	}

	// Getter und Setter

	public void setFocusedByMousePos(Point mouse) {
		Rectangle boundStartBtn = new Rectangle(pos.x, pos.y, bounds.x, bounds.y);
		this.isFocused = boundStartBtn.contains(mouse);
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setPosition(Point p) {
		pos = p;
	}

	public void setBounds(Point bounds) {
		this.bounds = bounds;
	}

	public Point getSize() {
		return new Point(bounds.x, bounds.y);
	}
}
