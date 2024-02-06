package game.entities;

import gfx.ImageTile;
import helper.ReadFromFile;
import main.GameController;
import game.Rendering.Renderer;
import org.json.JSONObject;

import java.awt.*;

import static helper.Arithmetik.*;
import static helper.Utilities.getPathOfSource;

public class Entity {
	protected GameController gc;
	protected String id;

	protected Point spriteSize;
	protected Point entityPos;
	protected Rectangle collisionBox;
	protected ImageTile im;

	public Entity(GameController gc, String path) {
		this.gc = gc;
		entityPos = new Point(0,0);
		try {
			JSONObject json = ReadFromFile.getJsonObject(Entity.class, path);

			assert json != null;

			spriteSize = new Point(json.getInt("spriteWidth"), json.getInt("spriteHeight"));

			this.id = json.getString("entityId");

			im = new ImageTile(gc, getPathOfSource(path)+"/"+json.getString("source"), json.getInt("spriteWidth"), json.getInt("spriteHeight"));

			JSONObject box = json.getJSONObject("collision-box");
			collisionBox = new Rectangle(box.getInt("x") * im.getScaledFactor(), box.getInt("y") * im.getScaledFactor(),
					box.getInt("w") * im.getScaledFactor(), box.getInt("h") * im.getScaledFactor());

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(double timeGap) {

	}

	public void draw(Renderer r) {

	}

	// Getter und Setter

	public void setPosition(Point p) {
		entityPos = p;
	}

	public void movePosition(Point p) {
		entityPos = add(entityPos, p);
	}

	public Point getCenter() {
		return new Point((spriteSize.x*im.getScaledFactor())/2, (spriteSize.y*im.getScaledFactor())/2);
	}

	public Point getDisplayPos() {
		Point p = subtract(add(entityPos, gc.world.getMapShift()), getCenter());
		return p;
	}

	public Point getMapPos() {
		return entityPos;
	}

	public Point getSize() {
		return spriteSize;
	}

	public String getName() {
		return id;
	}
}
