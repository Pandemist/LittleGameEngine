package UI.Boxes;

import java.awt.*;

public class UiBox2 {
	protected Color bg = new Color(0, 0, 0);
	protected Color frame = new Color(255, 255, 255);
	protected int alphaUnfucused = 100;
	protected int alphaFucused = 220;

	private Point pos = new Point(0, 0);
	private Point bounds = new Point( 1, 1);

	private boolean isFocused = false;

	public UiBox2() {}

	public UiBox2(int w, int h) {
		bounds = new Point(w, h);
	}

	public void update() {

	}

	public void isFocusedByMousePos(Point mouse) {
		Rectangle boundStartBtn = new Rectangle(pos.x, pos.y, bounds.x, bounds.y);
		this.isFocused = boundStartBtn.contains(mouse);
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setBounds(int w, int h) {
		this.bounds = new Point( w, h);
	}

	public Point getSize() {
		return new Point(bounds.x, bounds.y);
	}

	public void draw(Graphics2D g2, Point p) {
		this.pos = p;
		Color currentBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alphaUnfucused);
		Color currentFrame = new Color(frame.getRed(), frame.getGreen(), frame.getBlue(), alphaUnfucused);
		if(isFocused) {
			currentBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alphaFucused);
			currentFrame = new Color(frame.getRed(), frame.getGreen(), frame.getBlue(), alphaFucused);
		}
		
		g2.setColor(currentBg);
	//	g2.fillRoundRect(p.x+5, p.y+5, bounds.x-10, bounds.y-10, 25, 25);

		g2.setColor(currentFrame);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(p.x, p.y, bounds.x, bounds.y, 35, 35);
	}

	public void draw(Graphics2D g2, Point p, Point size) {
		this.pos = p;

		Color currentBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alphaUnfucused);
		Color currentFrame = new Color(frame.getRed(), frame.getGreen(), frame.getBlue(), alphaUnfucused);
		if(isFocused) {
			currentBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alphaFucused);
			currentFrame = new Color(frame.getRed(), frame.getGreen(), frame.getBlue(), alphaFucused);
		}

		g2.setColor(currentBg);
		g2.fillRoundRect(p.x, p.y, size.x, size.y, 35, 35);

		g2.setColor(currentFrame);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(p.x+5, p.y+5, size.x-10, size.y-10, 25, 25);

	}
}
