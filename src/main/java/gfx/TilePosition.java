package gfx;

public class TilePosition {
	private int layer;
	private long id;

	public TilePosition(int layer, long id) {
		this.layer = layer;
		this.id = id;
	}

	public int getLayer() {
		return layer;
	}

	public long getId() {
		return id;
	}
}
