package gfx;

import gfx.Animation.AnimationTile;
import main.GameController;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class TileSet {
	public static final String TILES_PATH = "tiles";

	private GameController gc;
	private ImageTile it;

	private int firstgid;
	private String name = "";
	private int tilewidth = 0;
	private int tileheight = 0;
	private int tilecount = 0;
	private int collums = 0;
	private int offset = 0;

	private ArrayList<AnimationTile> animTiles = new ArrayList<>();

	private void ParameterAuswerten(NamedNodeMap attributes) {
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			switch (attribute.getNodeName()) {
				case "name": {
					this.name = attribute.getNodeValue();
					break;
				}
				case "tilewidth": {
					this.tilewidth = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "tileheight": {
					this.tileheight = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "tilecount": {
					this.tilecount = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "columns": {
					this.collums = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "offset": {
					this.offset = Integer.parseInt(attribute.getNodeValue());
					break;
				}
			}
		}
	}

	private void ImageAuswerten(NamedNodeMap attributes) {
		String filename = "";
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			if (attribute.getNodeName().equals("source")) {
				filename = attribute.getNodeValue();
			}
		}
		this.it = new ImageTile(gc, TILES_PATH + "/" + filename, tilewidth, tileheight);
	}

	private void AppendTileToAnimation(AnimationTile at, Node node) {
		int tileId = 0;
		int duration = 0;

		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			switch (attribute.getNodeName()) {
				case "tileid": {
					tileId = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "duration": {
					duration = Integer.parseInt(attribute.getNodeValue());
					break;
				}
			}
		}

		at.addAnimationEntry(tileId, duration);
	}

	private void AnimationAuswerten(Node node) {
		int defaultTile = 0;

		// Default Tile aus dem Knoten bekommen
		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			if (attribute.getNodeName().equals("id")) {
				defaultTile = Integer.parseInt(attribute.getNodeValue());
			}
		}

		AnimationTile at = new AnimationTile(this, defaultTile);

		// Kinder betreten
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName().equals("animation")) {

				NodeList animFrameEntrys = childNode.getChildNodes();
				for (int j = 0; j < animFrameEntrys.getLength(); j++) {
					Node animFrameEntry = animFrameEntrys.item(j);
					if(animFrameEntry.getNodeName().equals("frame")) {
						AppendTileToAnimation(at, animFrameEntry);
					}
				}
			}
		}

		this.animTiles.add(at);
	}

	private void iterateElements(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
		//	System.out.println("Element: " + node.getNodeName());
			switch (node.getNodeName()) {
				case "tileset": {
					ParameterAuswerten(node.getAttributes());
					break;
				}
				case "image": {
					ImageAuswerten(node.getAttributes());
					break;
				}
				case "tile": {
					AnimationAuswerten(node);
					break;
				}
			}
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				iterateElements(childNode);
			}
		}
	}

	// Ceate TileSet by TileSetDescription
	public TileSet(GameController gc, String filename, int firstgid) {
		this.firstgid = firstgid-1;
		this.gc = gc;
	//	this.dictPath = path;
		try {
			ClassLoader classLoader = TileSet.class.getClassLoader();
			URL resourceUrl = classLoader.getResource(TILES_PATH + "/" + filename);

			if (resourceUrl == null) {
				throw new RuntimeException("Die Ressource '" + TILES_PATH + "/" + filename + "' wurde nicht gefunden.");
			}

			InputStream file = resourceUrl.openStream();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);

			document.getDocumentElement().normalize();
			iterateElements(document.getDocumentElement());

		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void runAnimations(double timeGap) {
		for(int i = animTiles.size(); i > 0; i--) {
			int tileId = animTiles.get(i).runAnimation(timeGap);
			if(tileId < 0) {
				animTiles.remove(i);
			}else{
				// TODO UPDATE TILE TEXTURE
			}
		}
	}

	// Getter und Setter

	public int getScaledTilewidth() {
		return tilewidth * it.getScaledFactor();
	}

	public int getScaledTileheight() {
		return tileheight * it.getScaledFactor();
	}

	public BufferedImage getTileOfTileSet(long tileId) {
		return it.getImageTile(firstgid + tileId);
	}

	public boolean isTileOnTileSet(long tileId) {
		return (firstgid <= tileId) && (firstgid + tilecount > tileId);
	}

	public long isTileAnimated(long decode) {
		for(AnimationTile a : animTiles) {
			if(firstgid + a.getAnimatedTile() == decode) {
				return decode - firstgid;
			}
		}
		return -1;
	}

	public AnimationTile getAnimation(long tileIdOnTileSet) {
		for(AnimationTile a : animTiles) {
			if(a.getAnimatedTile() == tileIdOnTileSet) {
				return a;
			}
		}
		return null;
	}
}
