package input;

import main.GameController;

import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	GameController gp;

	private int currentMouseX;
	private int currentMouseY;

	public InputHandler(GameController gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyEventManager(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyEventManager(e, false);
	}

	public void keyEventManager(KeyEvent e, boolean value) {
		gp.ui.menuKeyNavigation(e, value);
		switch (gp.getGs()) {
			case RUNNING_GAME: {
				gp.world.movementManager(e, value);
				break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClickManager(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseEventManager(e, true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseEventManager(e, false);
	}

	public void mouseClickManager(MouseEvent e) {
		switch (gp.getGs()) {
			case RUNNING_GAME: {
				gp.world.clickManager(e);
				break;
			}
			default: {
				gp.ui.menuMouseClickNavigation(e);
			}
		}
	}

	public void mouseEventManager(MouseEvent e, boolean value) {
		switch (gp.getGs()) {
			case RUNNING_GAME: {
			//	gp.world.clickManager(e, value);
				break;
			}
			default: {
			//	gp.ui.menuMouseClickNavigation(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentMouseX = e.getX();
		currentMouseY = e.getY();
		switch (gp.getGs()) {
			case RUNNING_GAME: {

				break;
			}
			default: {
				gp.ui.menuMouseOverNavigation(e);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	public int getCurrentMouseX() {
		return currentMouseX;
	}

	public int getCurrentMouseY() {
		return currentMouseY;
	}
}
