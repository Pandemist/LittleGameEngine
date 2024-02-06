package game.entities;

import game.GameState;
import lang.LangManager;
import main.GameController;
import game.Rendering.Renderer;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;

public class EntityManager {
	private GameController gc;
	public static final String PLAYER_PATH = "sprites/player";
	public static final String NPCS_PATH = "sprites/npcs";
	public static final String NPCS_EXTENSION = "json";

	public Player player;
	public HashMap<String, NPC> npcs;

	public EntityManager(GameController gc) {
		this.gc = gc;

		npcs = new HashMap<>();

		loadPlayer(gc, PLAYER_PATH + "/" + "player.json");
		loadNPCs(gc, NPCS_PATH);
	}

	public void placeNPCs(Point tileScale) {
		if(npcs.containsKey("cat01")) {
			npcs.get("cat01").setPosition(new Point(32 * tileScale.x, 5 * tileScale.y));
			npcs.get("cat01").setDirection(Direction.SOUTH);
		}

		if(npcs.containsKey("lewis01")) {
			npcs.get("lewis01").setPosition(new Point(48 * tileScale.x, 13 * tileScale.y));
			npcs.get("lewis01").setDirection(Direction.SOUTH);
		}
	}

	private void loadPlayer(GameController gc, String path) {
		player = new Player(gc, path);
	}

	private void loadNPCs(GameController gc, String path) {
		try {
			URL url = LangManager.class.getResource("/" + NPCS_PATH);

			if (url != null) {
				// Konvertiere die URL in einen Pfad
				String folderPath = Paths.get(url.toURI()).toString();

				File folder = new File(folderPath);

				if (folder.isDirectory()) {

					File[] files = folder.listFiles((dir, name) -> name.endsWith("."+NPCS_EXTENSION));

					if (files != null) {
						for (File file : files) {
							NPC tmpNPC = new NPC(gc, NPCS_PATH + "/" + file.getName());
							npcs.put(tmpNPC.id, tmpNPC);
						}
					}
				}

			} else {
				// Wenn der Ordner nicht gefunden wird, gib eine Fehlermeldung aus
				throw new RuntimeException("Ordner nicht gefunden: " + NPCS_PATH);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public void clickOnNPCs(Point pointOnMap) {
		if(gc.getGs().equals(GameState.RUNNING_GAME)) {
			for(NPC n : npcs.values()) {

				if(n.isClicked(pointOnMap)) {
					gc.dm.startConversation(n.id);
					break;
				}
			}
		}
	}

	public void update(double timeGap) {
		player.update(timeGap);
	}

	public void draw(Renderer r) {
		for(NPC n : npcs.values()) {
			if(n.getMapPos().y <= player.getMapPos().y) {
				n.draw(r);
			}
		}

		player.draw(r);

		for(NPC n : npcs.values()) {
			if(n.getMapPos().y > player.getMapPos().y) {
				n.draw(r);
			}
		}
	}
}
