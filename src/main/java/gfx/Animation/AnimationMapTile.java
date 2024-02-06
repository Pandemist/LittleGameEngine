package gfx.Animation;

import gfx.TilePosition;
import gfx.TileSet;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimationMapTile {
	private int lastTileId;
	private int currentId;
	private AnimationTile at;
	private BufferedImage currentTile;

	private ArrayList<TilePosition> usages = new ArrayList<>();

	public AnimationMapTile(AnimationTile at) {
		this.at = at;
		currentId = at.getAnimatedTile();
		currentTile = at.getTileSet().getTileOfTileSet(currentId);
	}

	public boolean isAnimationUpdated(double timeGap) {
		currentId = at.runAnimation(timeGap);

		if (currentId != lastTileId) {
			currentTile = at.getTileSet().getTileOfTileSet(currentId);
			lastTileId = currentId;
			return true;
		}
		return false;
	}


	public void addUsages(TilePosition usage) {
		this.usages.add(usage);
	}

	public ArrayList<TilePosition> getUsages() {
		return usages;
	}

	public BufferedImage getCurrentTile() {
		return currentTile;
	}
}
