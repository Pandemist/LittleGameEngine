package gfx;

import org.json.JSONObject;

public class SpriteAnimation {
	private String name;
	private int xOffset;
	private int yOffset;
	private int frames;

	public SpriteAnimation(String name, JSONObject node) {
		this.name = name;
		this.xOffset = node.getInt("xOffset");
		this.yOffset = node.getInt("yOffset");
		this.frames = node.getInt("frames");
	}

	public SpriteAnimation(String name, int xOffset, int yOffset, int frames) {
		this.name = name;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.frames = frames;
	}

	// Getter und Setter

	public String getName() {
		return name;
	}

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public int getFrames() {
		return frames;
	}
}
