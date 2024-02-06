package gfx.Animation;

import gfx.TileSet;

public class AnimationTile extends Animation{

	private int animatedTile;

	public AnimationTile(TileSet ts, int animatedTile) {
		super(ts);
		this.animatedTile = animatedTile;
		isAnimStarted = true;
		isAnimLooped = true;
	}

	public int runAnimation(double timeGap) {
		int superReturn = super.runAnimation(timeGap);
		if(superReturn < 0) {
			return animatedTile;
		}else{
			return superReturn;
		}
	}

	public int getAnimatedTile() {
		return animatedTile;
	}
}
