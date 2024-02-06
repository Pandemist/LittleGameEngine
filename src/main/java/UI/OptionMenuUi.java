package UI;

import UI.Boxes.UiBox;
import UI.Boxes.UiBoxFocusOutline;
import UI.Boxes.UiBoxOutline;
import UI.Boxes.UiTextBox;
import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;

import static helper.Arithmetik.subtract;
import static helper.Utilities.getFontBox;

public class OptionMenuUi {
	private final int boxMargin = 15;

	private GameController gc;

	private final UiBoxOutline optionBox;
	private final UiTextBox backBtn;

	private final Font font;

	public OptionMenuUi(GameController gc) {
		this.gc = gc;

		optionBox = new UiBoxOutline();

		Font font = new Font("Arial", Font.PLAIN, 30);
		backBtn = new UiTextBox(new UiBoxFocusOutline(), "Zurück", font, 15, new Color(0, 0, 0, 0));

		this.font = new Font("Arial", Font.PLAIN, 50);

		resize();
	}

	public void update() {

	}

	public void resize() {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		optionBox.setPosition(new Point(boxMargin, boxMargin));
		optionBox.setBounds(subtract(screenCenter , new Point(boxMargin * 2, boxMargin * 2)));

		backBtn.setPosition(new Point(screenCenter.x/2 - backBtn.getSize().x/2, 150));
	}

	public void draw(Graphics2D g2) {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		optionBox.draw(g2);

		String optionText = gc.langManager.getDictEntryByName("lang.optionMenu.name");
		Point textBox = getFontBox(optionText, font);

		g2.setFont(font);
		g2.setColor(Color.WHITE);
		g2.drawString(optionText, (screenCenter.x/2) - (textBox.x/2), textBox.y + 20);

		backBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.back"));

		backBtn.draw(g2);
	}

	public void mouseOver(Point mouse) {
		backBtn.setFocusedByMousePos(mouse);
	}

	public void clicked(MouseEvent e) {
		if(e.getButton() == 1) {    // Linke Maustaste
			if(backBtn.isFocused()) {
				gc.setGs(gc.getGsLast());
			}
		}
	}
}
