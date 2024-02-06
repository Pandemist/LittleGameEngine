package game.entities;

import main.GameController;
import game.Rendering.Renderer;

import java.awt.*;

public class NPC extends Moveable{

	public NPC(GameController gc, String path) {
		super(gc, path);
	}

	public void npcMovement() {

	}

	@Override
	public void update(double timeGap) {
		super.update(timeGap);
	}

	@Override
	public void draw(Renderer r) {
		super.draw(r);
	}

	public boolean isClicked(Point mouse) {
		Rectangle hitBox = new Rectangle(entityPos.x - getCenter().x, entityPos.y - getCenter().y, spriteSize.x * gc.scale, spriteSize.y * gc.scale);
		return hitBox.contains(mouse);
	}

}
