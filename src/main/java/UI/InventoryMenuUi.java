package UI;

import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;

public class InventoryMenuUi {
	private final GameController gc;

	public InventoryMenuUi(GameController gc) {
		this.gc = gc;


		resize();
	}

	public void update(double timeGap) {

	}

	public void resize() {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

	}

	public void draw(Graphics2D g2) {


		resize();
	}

	public void mouseOver(Point mouse) {

	}

	public void clicked(MouseEvent e) {

	}
}
