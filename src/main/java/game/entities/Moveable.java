package game.entities;

import gfx.SpriteAnimation;
import helper.ReadFromFile;
import main.GameController;
import game.Rendering.Renderer;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;

import static helper.Utilities.divideSafely;

public class Moveable extends Entity{

	protected double speed;
	protected double rawspeed;

	protected Direction dir;
	protected double spriteTimer;
	protected int walkingFrame;
	protected HashMap<String, SpriteAnimation> animations;

	protected boolean move_up = false;
	protected boolean move_down = false;
	protected boolean move_right = false;
	protected boolean move_left = false;

	public Moveable(GameController gc, String path) {
		super(gc, path);

		animations = new HashMap<>();

		this.spriteTimer = 0;
		this.walkingFrame = 0;
		this.dir = Direction.SOUTH;

		try {
			JSONObject json = ReadFromFile.getJsonObject(Moveable.class, path);

			assert json != null;
			JSONObject sets = json.getJSONObject("sets");

			for (Iterator<String> it = sets.keys(); it.hasNext(); ) {
				String setName = it.next();
				JSONObject set = sets.getJSONObject(setName);
				animations.put(setName, new SpriteAnimation(setName, set));
			}

			JSONObject stats = json.getJSONObject("initial-stats");
			this.rawspeed = stats.getDouble("base-speed");
			this.speed = stats.getDouble("base-speed") * gc.scale * gc.tileSize;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void movement(double timeGap, Point movement) {
		if(movement.y < 0) {
			dir = Direction.NORTH;
		}else if(movement.y > 0) {
			dir = Direction.SOUTH;
		}else if(movement.x < 0) {
			dir = Direction.WEST;
		}else if(movement.x > 0) {
			dir = Direction.EAST;
		}

		double speedFrame = divideSafely(0.5, rawspeed, 0);

		if((movement.x != 0)||(movement.y != 0)) {
			this.movePosition(movement);

			spriteTimer += timeGap;
			if (spriteTimer > speedFrame) {
				spriteTimer -= speedFrame;
				walkingFrame++;
			}
		}else{
			walkingFrame = 0;
		}
	}

	public void update(double timeGap) {
		super.update(timeGap);
	}

	@Override
	public void draw(Renderer r) {
		BufferedImage sprite = getRenderSprite();

		Point p = getDisplayPos();
		r.addGameLayer(sprite, p.x, p.y);
	//	g2.drawImage(sprite, p.x, p.y, null);
	}

	// Getter und Setter

	private BufferedImage getRenderSprite() {
		String movement_direction = dir.moveSetString();

		assert !animations.containsKey(movement_direction);

		SpriteAnimation anim = animations.get(movement_direction);

		walkingFrame = walkingFrame % anim.getFrames();
		int xPos = anim.getxOffset() + walkingFrame;
		int yPos = anim.getyOffset();

		return super.im.getImageTile(xPos, yPos);
	}

	public double getSpeed() {
		return speed;
	}

	public Rectangle getColissionBoxOnMap() {
		Rectangle collisionBoxOnMap = collisionBox;
		Point c = getCenter();
		return new Rectangle(collisionBoxOnMap.x + entityPos.x - c.x,
				collisionBoxOnMap.y + entityPos.y - c.y,
				collisionBoxOnMap.width,
				collisionBoxOnMap.height);
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public Direction getDirection() {
		return dir;
	}
}
