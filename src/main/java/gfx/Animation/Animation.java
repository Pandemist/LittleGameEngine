package gfx.Animation;

import gfx.TileSet;

import java.util.LinkedList;
import java.util.Queue;

public class Animation {
	private double timer;
	private Queue<AnimationEntry> animEntry = new LinkedList<>();
	protected TileSet ts;
	protected boolean isAnimStarted = false;
	protected boolean isAnimLooped = false;

	public Animation(TileSet ts) {
		this.ts = ts;
		timer = 0;
	}

	public void addAnimationEntry(int tileId, double duration) {
		animEntry.add(new AnimationEntry(tileId, duration));
	}
	public int runAnimation(double timeGap) {
		if((animEntry.peek() != null) && isAnimStarted) {
			timer += timeGap;
			if(timer > animEntry.peek().duration) {
				if(isAnimLooped) {
					animEntry.add(animEntry.poll());
				}
				timer = 0;
			}
			if(animEntry.peek() != null) {
				return animEntry.peek().tileId;
			}else{
				return -1;
			}
		}else {
			return -1;
		}
	}

	public void startAnimation() {
		this.isAnimStarted = true;
	}

	public void stopAnimation() {
		this.isAnimStarted = false;
	}

	public void loopAnimation() {
		this.isAnimLooped = true;
	}

	public TileSet getTileSet() {
		return ts;
	}
}
