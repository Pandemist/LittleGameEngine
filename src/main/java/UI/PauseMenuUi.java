package UI;

import UI.Boxes.UiBox;
import UI.Boxes.UiBoxFocusOutline;
import UI.Boxes.UiBoxOutline;
import UI.Boxes.UiTextBox;
import game.GameState;
import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;

import static helper.Arithmetik.subtract;
import static helper.Utilities.getFontBox;

public class PauseMenuUi {
	private final int boxMargin = 15;

	private GameController gc;

	private final UiBoxOutline optionBox;

	private final UiTextBox resumeBtn;
	private final UiTextBox optionBtn;
	private final UiTextBox exitBtn;

	private final Font font;

	public PauseMenuUi(GameController gc) {
		this.gc = gc;

		optionBox = new UiBoxOutline();

		Font font = new Font("Arial", Font.PLAIN, 30);
		resumeBtn = new UiTextBox(new UiBoxFocusOutline(), "Weiter", font, 15, 150);
		optionBtn = new UiTextBox(new UiBoxFocusOutline(), "Option", font, 15, 150);
		exitBtn = new UiTextBox(new UiBoxFocusOutline(), "Beenden", font, 15, 150);

		this.font = new Font("Arial", Font.PLAIN, 50);

		resize();
	}

	public void update() {

	}

	public void resize() {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		optionBox.setPosition(new Point(boxMargin, boxMargin));
		optionBox.setBounds(subtract(screenCenter , new Point(boxMargin * 2, boxMargin * 2)));

		resumeBtn.setPosition(new Point(screenCenter.x/2 - resumeBtn.getSize().x/2, 150));
		optionBtn.setPosition(new Point(screenCenter.x/2 - optionBtn.getSize().x/2, 225));
		exitBtn.setPosition(new Point(screenCenter.x/2 - exitBtn.getSize().x/2, 300));
	}

	public void draw(Graphics2D g2) {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		optionBox.draw(g2);

		String optionText = gc.langManager.getDictEntryByName("lang.pauseMenu.name");
		Point textBox = getFontBox(optionText, font);

		g2.setFont(font);
		g2.setColor(Color.WHITE);
		g2.drawString(optionText, (screenCenter.x/2) - (textBox.x/2), textBox.y + 20);

		resumeBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.resume"));
		optionBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.options"));
		exitBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.exitGame"));
		resize();

		resumeBtn.draw(g2);
		optionBtn.draw(g2);
		exitBtn.draw(g2);
	}

	public void mouseOver(Point mouse) {
		resumeBtn.setFocusedByMousePos(mouse);
		optionBtn.setFocusedByMousePos(mouse);
		exitBtn.setFocusedByMousePos(mouse);
	}

	public void clicked(MouseEvent e) {
		if(e.getButton() == 1) {    // Linke Maustaste
			if(resumeBtn.isFocused()) {
				gc.setGs(GameState.RUNNING_GAME);
			}else if(optionBtn.isFocused()) {
				gc.setGs(GameState.OPTIONS);
			}else if(exitBtn.isFocused()) {
				gc.stopGame();
			}
		}
	}
}
