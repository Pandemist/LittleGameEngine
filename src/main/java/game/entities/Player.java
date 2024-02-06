package game.entities;

import main.GameController;
import game.Rendering.Renderer;

import java.awt.event.KeyEvent;

public class Player extends Moveable{

	public Player(GameController gc, String path) {
		super(gc, path);
	}

	public void playerMovement(KeyEvent e, boolean value) {
		int code = e.getKeyCode();

		if(code == KeyEvent.VK_W) {
			move_up = value;
		}
		if(code == KeyEvent.VK_S) {
			move_down = value;
		}
		if(code == KeyEvent.VK_A) {
			move_left = value;
		}
		if(code == KeyEvent.VK_D) {
			move_right = value;
		}
	}

	@Override
	public void update(double timeGap) {
		super.update(timeGap);
	}

	@Override
	public void draw(Renderer r) {
		super.draw(r);
	}

	// Getter und Setter

	public boolean isPlayerMovingUp() {
		return move_up;
	}
	public boolean isPlayerMovingDown() {
		return move_down;
	}
	public boolean isPlayerMovingLeft() {
		return move_left;
	}
	public boolean isPlayerMovingRight() {
		return move_right;
	}

	public void resetMovement() {
		move_up = false;
		move_down = false;
		move_right = false;
		move_left = false;
	}
}
