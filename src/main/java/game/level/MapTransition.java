package game.level;

import game.entities.Direction;
import helper.Option;
import main.GameController;
import org.json.JSONObject;

import java.awt.*;

import static helper.Option.Some;

public class MapTransition {
	private Point transitionTile;
	private Direction transitionDirection;

	private String destinationMapName;
	private Point destinationTile;
	private Direction destinationDirection;

	private double timer;
	private boolean transitionDone;


	public MapTransition(JSONObject node) {
		int transitionX = node.getInt("transition-tile-x");
		int transitionY = node.getInt("transition-tile-y");
		char transitionDir = node.getString("transition-direction").charAt(0);

		String destMap = node.getString("dest-name");
		char destDir = node.getString("dest-orient").charAt(0);
		int destX = node.getInt("dest-tile-x");
		int destY = node.getInt("dest-tile-y");

		this.transitionTile = new Point(transitionX, transitionY);
		this.transitionDirection = Direction.getDirectionByChar(transitionDir);

		this.destinationMapName = destMap;
		this.destinationTile = new Point(destX, destY);
		this.destinationDirection = Direction.getDirectionByChar(destDir);
	}


	public Point getTransitionTile() {
		return transitionTile;
	}

	public Direction getTransitionDirection() {
		return transitionDirection;
	}

	public String getDestinationMapName() {
		return destinationMapName;
	}

	public Point getDestinationTile() {
		return destinationTile;
	}

	public Direction getDestinationDirection() {
		return destinationDirection;
	}

	private int getColorValue() {
		return Math.min((int) timer, 255);
	}

	public void drawTransition(Graphics2D g2, GameController gc) {
		Color dark = new Color(0, 0, 0, getColorValue());
		g2.setColor(dark);
		g2.fillRect(0, 0, gc.screenWidth, gc.screenHeight);
	}

	public boolean runTransition(double timeGap) {
		if(!transitionDone) {
			timer += timeGap * (255 / 1.5);
		}else{
			timer -= timeGap * (255 / 1.5);
		}

		if((timer > 260) && (!transitionDone)) {
			transitionDone = true;
			return true;
		}else{
			return false;
		}
	}

	public boolean isTransitionDone() {
		return transitionDone && (timer < 0);
	}

	public boolean movementLocked() {
		return !transitionDone || (timer >= 128);
	}

	public Option<Color> drawTransitionColor() {
		return Some(new Color(0, 0, 0, getColorValue()));
	}
}
