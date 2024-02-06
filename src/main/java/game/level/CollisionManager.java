package game.level;

import game.entities.Entity;
import game.entities.Moveable;
import main.GameController;

import java.awt.*;

import static helper.Arithmetik.add;
import static helper.Arithmetik.subtract;

public class CollisionManager {
	private GameController gc;

	public CollisionManager(GameController gc) {
		this.gc = gc;
	}

	public Point checkMapBorderCollision(Moveable e, Point movement) {
		Point mapSize = gc.world.gameMap.getMapSize();

		Rectangle entityColissionOnMap = e.getColissionBoxOnMap();
		entityColissionOnMap.x += movement.x;
		entityColissionOnMap.y += movement.y;

		// Bewegen nach NORDEN
		if(movement.y < 0) {
			if(entityColissionOnMap.y <= 0) {
				int newY = movement.y + (-1 * entityColissionOnMap.y);
				movement = new Point(movement.x, newY);
			}
			// Bewegen nach SÜDEN
		}else if(movement.y > 0) {
			if((entityColissionOnMap.y + entityColissionOnMap.height) >= mapSize.y) {
				int newY = movement.y + ((mapSize.y-1) - (entityColissionOnMap.y + entityColissionOnMap.height));
				movement.y = newY;
			}
			// Bewegen nach WESTEN
		}else if(movement.x < 0) {
			if(entityColissionOnMap.x <= 0) {
				int newX = movement.x + (-1 * entityColissionOnMap.x);
				movement = new Point(newX, movement.y);
			}
			// Bewegen nach OSTEN
		}else if(movement.x > 0) {
			if((entityColissionOnMap.x + entityColissionOnMap.width) >= mapSize.x) {
				int newX = movement.x + ((mapSize.x-1) - (entityColissionOnMap.x + entityColissionOnMap.width));
				movement.x = newX;
			}
		}
		return movement;
	}

	public Point checkMapCollision(Moveable e, Point movement) {
		Rectangle entityColissionOnMap = e.getColissionBoxOnMap();
		entityColissionOnMap.x += movement.x;
		entityColissionOnMap.y += movement.y;

		Point tileSize = gc.world.gameMap.getTileSize();

		// Bewegen nach NORDEN
		if(movement.y < 0) {
			if(gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x, entityColissionOnMap.y) ||
					gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x + entityColissionOnMap.width, entityColissionOnMap.y)) {
				// Bewegung Canceln, wenn der Spieler in eine Collision läuft
				Point collisionTileBorder = new Point(0, tileSize.y - (entityColissionOnMap.y % tileSize.y));

				movement = add(movement, collisionTileBorder);
			}
			// Bewegen nach SÜDEN
		}else if(movement.y > 0) {
			if(gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x, entityColissionOnMap.y + entityColissionOnMap.height) ||
					gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x + entityColissionOnMap.width, entityColissionOnMap.y + entityColissionOnMap.height)) {
				// Bewegung Canceln, wenn der Spieler in eine Collision läuft

				Point collisionTileBorder = new Point(0, (entityColissionOnMap.y + entityColissionOnMap.height) % tileSize.y);

				movement = subtract(movement, collisionTileBorder);
			}
			// Bewegen nach WESTEN
		}else if(movement.x < 0) {
			if(gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x, entityColissionOnMap.y) ||
					gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x, entityColissionOnMap.y + entityColissionOnMap.height)) {
				// Bewegung Canceln, wenn der Spieler in eine Collision läuft

				Point collisionTileBorder = new Point(tileSize.x - (entityColissionOnMap.x % tileSize.x), 0);

				movement = add(movement, collisionTileBorder);
			}
			// Bewegen nach OSTEN
		}else if(movement.x > 0) {
			if(gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x + entityColissionOnMap.width, entityColissionOnMap.y) ||
					gc.world.gameMap.hasTileOnMapColission(entityColissionOnMap.x + entityColissionOnMap.width, entityColissionOnMap.y + entityColissionOnMap.height)) {
				// Bewegung Canceln, wenn der Spieler in eine Collision läuft

				Point collisionTileBorder = new Point((entityColissionOnMap.x + entityColissionOnMap.width) % tileSize.x, 0);

				movement = subtract(movement, collisionTileBorder);
			}
		}
		return movement;
	}

	public Point checkEntityCollision(Moveable e, Point movement, Moveable hitted) {
		Point mapSize = gc.world.gameMap.getMapSize();

		Rectangle e1ColissionOnMap = e.getColissionBoxOnMap();
		e1ColissionOnMap.x += movement.x;
		e1ColissionOnMap.y += movement.y;

		Rectangle e2ColissionOnMap = hitted.getColissionBoxOnMap();

		if (e1ColissionOnMap.intersects(e2ColissionOnMap)) {
			// Bewegen nach NORDEN
			if (movement.y < 0) {
				int newY = movement.y - (e1ColissionOnMap.y - (e2ColissionOnMap.y + e2ColissionOnMap.height));
				movement = new Point(movement.x, newY);
			// Bewegen nach SÜDEN
			} else if (movement.y > 0) {
				int newY = movement.y + (e2ColissionOnMap.y - (e1ColissionOnMap.y + e1ColissionOnMap.height));
				movement = new Point(movement.x, newY);
				// Bewegen nach WESTEN
			} else if (movement.x < 0) {
				int newX = movement.x - (e1ColissionOnMap.x - (e2ColissionOnMap.x + e2ColissionOnMap.width));
				movement = new Point(newX, movement.y);
				// Bewegen nach OSTEN
			} else if (movement.x > 0) {
				int newX = movement.x + (e2ColissionOnMap.x - (e1ColissionOnMap.x + e1ColissionOnMap.width));
				movement = new Point(newX, movement.y);
			}
		}
		return movement;
	}
}
