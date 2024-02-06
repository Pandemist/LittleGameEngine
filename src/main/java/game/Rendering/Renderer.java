package game.Rendering;

import game.GameState;
import helper.Option;
import main.GameController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static helper.Option.None;
import static helper.Utilities.drawLightsourceOnLightmap;

public class Renderer {
	private final GameController gc;

	ArrayList<RenderEntry> gameLayer;
	ArrayList<RenderEntry> lightSources;

	Color nightColor;
	Option<Color> transitionColor;

	public Renderer(GameController gc) {
		this.gc = gc;

		gameLayer = new ArrayList<>();
		lightSources = new ArrayList<>();

		nightColor = new Color(0, 0, 0);
		transitionColor = None();
	}

	public void addGameLayer(BufferedImage im, int posX, int posY) {
		gameLayer.add(new RenderEntry(im, posX, posY));
	}

	public void addLightSources(BufferedImage im, int posX, int posY) {
		lightSources.add(new RenderEntry(im, posX, posY));
	}

	public void setNightColor(Color c) {
		this.nightColor = c;
	}

	public void setTransitionColor(Option<Color> c) {
		this.transitionColor = c;
	}

	private void prepareDrawLists() {
		gameLayer.clear();
		lightSources.clear();

		if(gc.getGs() != GameState.START_SCREEN) {
			gc.world.render(this);
		}
	}

	public void render(Graphics2D g2) {
		prepareDrawLists();

		// Draw World
		for(RenderEntry entry : gameLayer) {
			g2.drawImage(entry.getIm(), entry.getPosX(), entry.getPosY(), null);
		}

		BufferedImage lightMapLayer = new BufferedImage(gc.screenWidth, gc.screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dLightLayer = lightMapLayer.createGraphics();

		g2dLightLayer.setColor(nightColor);
		g2dLightLayer.fillRect(0, 0, gc.screenWidth, gc.screenHeight);

		g2dLightLayer.dispose();

		BufferedImage mergedLights = new BufferedImage(gc.screenWidth, gc.screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dmergedLights = lightMapLayer.createGraphics();

		for(RenderEntry entry : lightSources) {
			g2dmergedLights.drawImage(entry.getIm(), entry.getPosX(), entry.getPosY(), null);
		}
		g2dmergedLights.dispose();

	//	g2.drawImage(mergedLights, 0, 0, null);

		drawLightsourceOnLightmap(lightMapLayer, mergedLights);

		g2.drawImage(lightMapLayer, 0, 0, null);

		if(transitionColor.isSome()) {
			g2.setColor(transitionColor.unwrap());
			g2.fillRect(0, 0, gc.screenWidth, gc.screenHeight);
		}

		gc.ui.render(g2);
	}
}
