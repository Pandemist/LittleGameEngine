package UI;

import UI.Boxes.UiBoxOutline;
import UI.Boxes.UiTextBox;
import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;

import static helper.Utilities.*;

public class OverlayUi {
	private final int margin = 15;
	private final GameController gc;

	private final UiTextBox ingameTimeBox;

	private final Font font;

	public OverlayUi(GameController gc) {
		this.gc = gc;

		font = new Font("Arial", Font.PLAIN, 15);
		ingameTimeBox = new UiTextBox(new UiBoxOutline(), "Uhrzeit", font, margin);

		resize();
	}

	public void update(double timeGap) {

	}

	public void resize() {
		ingameTimeBox.setPosition(new Point(margin, margin));
	}

	public void draw(Graphics2D g2) {
		String uhrzeit = getWeekDayShort(gc.world.ingameWeekday).substring(0, 2) + ". " + getIngameHours(gc.world.ingameTime) + ":" + getIngameMinutes(gc.world.ingameTime) + ":" + getIngameSeconds(gc.world.ingameTime) + " " + gc.langManager.getDictEntryByName("lang.ui.time");

		ingameTimeBox.setText(uhrzeit);

		ingameTimeBox.draw(g2);
	}

	public void mouseOver(Point mouse) {

	}

	public void clicked(MouseEvent e) {

	}
}
