package UI;

import UI.Boxes.UiBoxFocusOutline;
import UI.Boxes.UiTextBox;
import game.GameState;
import main.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;

import static helper.Arithmetik.subtract;
import static helper.Utilities.getFontBox;

public class MainMenuUi {
	private final GameController gc;
	private final UiTextBox startBtn;
	private final UiTextBox optionBtn;
	private final UiTextBox exitBtn;

	private final Font font;

	public MainMenuUi(GameController gc) {
		this.gc = gc;

		Font font = new Font("Arial", Font.PLAIN, 30);
		startBtn = new UiTextBox(new UiBoxFocusOutline(), "Start", font, 15);
		optionBtn = new UiTextBox(new UiBoxFocusOutline(), "Option", font, 15);
		exitBtn = new UiTextBox(new UiBoxFocusOutline(), "End", font, 15);

		this.font = new Font("Arial", Font.PLAIN, 50);

		resize();
	}

	public void update() {

	}

	public void resize() {
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		startBtn.setPosition(new Point(screenCenter.x/2 - startBtn.getSize().x/2, 300));
		optionBtn.setPosition(new Point(screenCenter.x/2 - optionBtn.getSize().x/2, 375));
		exitBtn.setPosition(new Point(screenCenter.x/2 - exitBtn.getSize().x/2, 450));
	}

	public void draw(Graphics2D g2) {
		Color bg = Color.BLACK;
		g2.setBackground(bg);
		Point screenCenter = new Point(gc.screenWidth, gc.screenHeight);

		String gameName = gc.langManager.getDictEntryByName("lang.game.name");
		Point textBox = getFontBox(gameName, font);

		g2.setFont(font);
		g2.setColor(Color.WHITE);
		g2.drawString(gameName, (screenCenter.x/2) - (textBox.x/2), textBox.y + 50);

		startBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.start"));
		optionBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.options"));
		exitBtn.setText(gc.langManager.getDictEntryByName("lang.menuBtn.exitGame"));
		resize();

		startBtn.draw(g2);
		optionBtn.draw(g2);
		exitBtn.draw(g2);
	}

	// Getter und Setter

	public void mouseOver(Point mouse) {
		startBtn.setFocusedByMousePos(mouse);
		optionBtn.setFocusedByMousePos(mouse);
		exitBtn.setFocusedByMousePos(mouse);
	}

	public void clicked(MouseEvent e) {
		if(e.getButton() == 1) {    // Linke Maustaste
			if(startBtn.isFocused()) {
				gc.setGs(GameState.RUNNING_GAME);
			}else if(optionBtn.isFocused()) {
				gc.setGs(GameState.OPTIONS);
			}else if(exitBtn.isFocused()) {
				gc.stopGame();
			}
		}
	}
}
