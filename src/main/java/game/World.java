package game;

import game.entities.Direction;
import game.entities.EntityManager;
import game.entities.NPC;
import game.environment.EnviManager;
import game.level.CollisionManager;
import game.level.DayNightCycle;
import game.level.GameMap;
import game.level.MapTransition;
import helper.Option;
import main.GameController;
import game.Rendering.Renderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static helper.Arithmetik.*;
import static helper.Option.*;

public class World {

	private GameController gc;
	private CollisionManager cm;
	public GameMap gameMap;
	public EntityManager entityManager;
	private DayNightCycle dnc;
	private EnviManager envi;
	private Option<MapTransition> inUseTransition = None();
	public double ingameTime = 0;
	public int ingameWeekday = 0;
	public int ingameWeeks = 0;

	public World(GameController gc) {
		this.gc = gc;

		dnc = new DayNightCycle();
		cm = new CollisionManager(gc);
		envi = new EnviManager(gc);
		entityManager = new EntityManager(gc);
		loadNewMap("outdoor", new Point(32, 16), Direction.SOUTH);

		entityManager.placeNPCs(gameMap.getTileSize());

		ingameWeeks = 5;
		ingameWeekday = 2;
		ingameTime = (double) 12 / 24;
	}

	public void update(double timeGap) {
		if(gc.getGs().equals(GameState.RUNNING_GAME)) {
			ingameTime += ((timeGap) / 60 / 60 / 24);
		}
		if(ingameTime >= 1) {
			ingameTime -= 1;
			ingameWeekday++;
		}
		if(ingameWeekday > 7) {
			ingameWeekday -= 7;
			ingameWeeks++;
		}
		if(ingameWeeks > 52) {
			ingameWeeks -= 52;
		}

		boolean playerMoveLocked = false;
		if(inUseTransition.isSome()) {
			MapTransition m = inUseTransition.unwrap();
			playerMoveLocked = m.movementLocked();

			if(m.runTransition(timeGap)) {
				loadNewMap(m.getDestinationMapName(), m.getDestinationTile(), m.getDestinationDirection());
			}
			if(m.isTransitionDone()) {
				inUseTransition = None();
			}
		}

		playerMoveLocked = playerMoveLocked || gc.getGs().equals(GameState.IN_DIALOG);

		if(!playerMoveLocked) {
			gameMap.update(timeGap);
			boolean moved = worldMovement(timeGap);
			entityManager.update(timeGap);
			if (moved) {
				eventTracker();
			}
		}else{
			entityManager.player.resetMovement();
		}

		envi.update(timeGap);
	}

	public void render(Renderer r) {
		// Prepare Map
		gameMap.draw(r, 0);
		gameMap.draw(r, 1);

		entityManager.draw(r);

		gameMap.draw(r, 2);

		// Prepare Lights
		envi.draw(r);

		// Prepare Night
		r.setNightColor(dnc.getNightValue(ingameTime));

		// Prepare Transition
		if(inUseTransition.isSome()) {
			r.setTransitionColor(inUseTransition.unwrap().drawTransitionColor());
		}else{
			r.setTransitionColor(None());
		}
	}

	// Getter und Setter

	public Point getMapShift() {
		return gameMap.getMapShift();
	}

	public boolean isMovedToLowerHalf() {
		return gameMap.isMovedToLowerHalf();
	}

	// Eventmanagement

	private void eventTracker() {
		Rectangle playerColissionOnMap = entityManager.player.getColissionBoxOnMap();
		Point tileSize = gameMap.getTileSize();
		for(MapTransition m : gameMap.getMapTransitions()) {
			Rectangle conflictTile = new Rectangle(m.getTransitionTile().x * tileSize.x, m.getTransitionTile().y * tileSize.y, tileSize.x, tileSize.y);

			if(playerColissionOnMap.intersects(conflictTile) &&
					entityManager.player.getDirection().equalsTo(m.getTransitionDirection())) {
				inUseTransition = Some(m);
			}
		}
	}

	// Movement

	public void clickManager(MouseEvent e) {
		int btn = e.getButton(); // 1 = links, 2 = mitte, 3 = rechts
		int x = e.getX();
		int y = e.getY();
		Point mapShift = gameMap.getMapShift();
		Point tileSize = gameMap.getTileSize();

		Point player = divide(subtract(entityManager.player.getMapPos(), entityManager.player.getCenter()), tileSize);

		Point clickedPos = new Point(x - mapShift.x, y - mapShift.y);
		Point clickedTile = new Point((x - mapShift.x) / tileSize.x, (y - mapShift.y) / tileSize.y);

		double distToPlayerTiles = distance(player, clickedTile);

		if(btn == 1) {
			switch (gc.getGs()) {
				case RUNNING_GAME: {
					if(distToPlayerTiles < 4) {
						entityManager.clickOnNPCs(clickedPos);
					}
				}
			}
		}
	}

	public void movementManager(KeyEvent e, boolean value) {
		// Player Movement
		entityManager.player.playerMovement(e, value);
	}

	private boolean worldMovement(double timeGap) {
		Point playerMovement = new Point(0, 0);

		if(entityManager.player.isPlayerMovingUp() ||
				entityManager.player.isPlayerMovingDown() ||
				entityManager.player.isPlayerMovingLeft() ||
				entityManager.player.isPlayerMovingRight()) {
			double playerSpeed = entityManager.player.getSpeed();

			if (entityManager.player.isPlayerMovingUp()) {
				playerMovement.y -= (int) (playerSpeed * timeGap);
			} else if (entityManager.player.isPlayerMovingDown()) {
				playerMovement.y += (int) (playerSpeed * timeGap);
			} else if (entityManager.player.isPlayerMovingLeft()) {
				playerMovement.x -= (int) (playerSpeed * timeGap);
			} else if (entityManager.player.isPlayerMovingRight()) {
				playerMovement.x += (int) (playerSpeed * timeGap);
			}

			// Test for Collision

			playerMovement = cm.checkMapBorderCollision(entityManager.player, playerMovement);
			playerMovement = cm.checkMapCollision(entityManager.player, playerMovement);

			for(NPC n : entityManager.npcs.values()) {
				playerMovement = cm.checkEntityCollision(entityManager.player, playerMovement, n);
			}
		}

		entityManager.player.movement(timeGap, playerMovement);
		gameMap.adjustMapShiftToPlayerPos(entityManager.player.getMapPos());
		return !playerMovement.equals(new Point(0, 0));
	}

	// Map Loading

	public void loadNewMap(String path, Point player, Direction d) {
		loadNewMap(path, player);
		entityManager.player.setDirection(d);
	}

	public void loadNewMap(String path, Point player) {
		loadNewMap(path);

		Point mapScale = gameMap.getTileSize();
		player = multiply(player, mapScale);

		player.x -= entityManager.player.getCenter().x;
		player.y -= entityManager.player.getSize().y;

		entityManager.player.setPosition(player);
		gameMap.adjustMapShiftToPlayerPos(entityManager.player.getMapPos());
	}

	public void loadNewMap(String path) {
		gameMap = new GameMap(this.gc, path);
	}
}
