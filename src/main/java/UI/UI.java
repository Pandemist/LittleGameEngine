package UI;

import UI.Boxes.UiBox;
import game.GameState;
import main.GameController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UI {
	private GameController gc;

	public MainMenuUi mainMenu;
	public PauseMenuUi pauseMenu;
	public OptionMenuUi optionMenu;
	public DialogMenuUi dialogMenu;
	public InventoryMenuUi inventoryMenu;
	public OverlayUi overlayUi;

	private ArrayList<UiBox> uiBoxes = new ArrayList<>();

	public UI(GameController gc) {
		this.gc = gc;
		mainMenu = new MainMenuUi(gc);
		pauseMenu = new PauseMenuUi(gc);
		optionMenu = new OptionMenuUi(gc);
		dialogMenu = new DialogMenuUi(gc);
		inventoryMenu = new InventoryMenuUi(gc);
		overlayUi = new OverlayUi(gc);
	}

	public void update(double timeGap) {
		switch (gc.getGs()) {
			case START_SCREEN: {
				mainMenu.update();
				break;
			}
			case PAUSE_MENU: {
				pauseMenu.update();
				break;
			}
			case RUNNING_GAME: {
				overlayUi.update(timeGap);
				break;
			}
			case IN_DIALOG: {
				dialogMenu.update(timeGap);
				break;
			}
			case INVENTORY: {
				inventoryMenu.update(timeGap);
				break;
			}
			case OPTIONS: {
				optionMenu.update();
				break;
			}
		}
	}

	public void render(Graphics2D g2) {
		switch (gc.getGs()) {
			case START_SCREEN: {
				mainMenu.draw(g2);
				break;
			}
			case PAUSE_MENU: {
				pauseMenu.draw(g2);
				break;
			}
			case RUNNING_GAME: {
				overlayUi.draw(g2);
				break;
			}
			case IN_DIALOG: {
				dialogMenu.draw(g2);
				break;
			}
			case INVENTORY: {
				inventoryMenu.draw(g2);
				break;
			}
			case OPTIONS: {
				optionMenu.draw(g2);
				break;
			}
		}
	}

	public void menuKeyNavigation(KeyEvent e, boolean value) {
		int code = e.getKeyCode();

		if((code == KeyEvent.VK_ESCAPE) && value) {
			switch (gc.getGs()) {
				case START_SCREEN: {
					gc.stopGame();
					break;
				}
				case INVENTORY:
				case PAUSE_MENU: {
					gc.setGs(GameState.RUNNING_GAME);
					break;
				}
				case RUNNING_GAME: {
					gc.setGs(GameState.PAUSE_MENU);
					break;
				}
				case OPTIONS: {
					gc.setGs(gc.getGsLast());
					break;
				}
			}
		}
		if((code == KeyEvent.VK_E) && value) {
			switch (gc.getGs()) {
				case RUNNING_GAME: {
					gc.setGs(GameState.INVENTORY);
					break;
				}
				case INVENTORY: {
					gc.setGs(GameState.RUNNING_GAME);
					break;
				}
			}
		}
	}

	public void menuMouseClickNavigation(MouseEvent e) {
		switch (gc.getGs()) {
			case START_SCREEN: {
				mainMenu.clicked(e);
				break;
			}
			case PAUSE_MENU: {
				pauseMenu.clicked(e);
				break;
			}
			case OPTIONS: {
				optionMenu.clicked(e);
				break;
			}
			case INVENTORY: {
				inventoryMenu.clicked(e);
				break;
			}
			case IN_DIALOG: {
				dialogMenu.clicked(e);
			}
		}
	}

	public void menuMouseOverNavigation(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		switch (gc.getGs()) {
			case START_SCREEN: {
				mainMenu.mouseOver(p);
				break;
			}
			case PAUSE_MENU: {
				pauseMenu.mouseOver(p);
				break;
			}
			case OPTIONS: {
				optionMenu.mouseOver(p);
				break;
			}
			case INVENTORY: {
				inventoryMenu.mouseOver(p);
				break;
			}
			case IN_DIALOG: {
				dialogMenu.mouseOver(p);
				break;
			}
		}
	}
}
