package game.level;

import gfx.Animation.AnimationMapTile;
import gfx.Animation.AnimationTile;
import gfx.TilePosition;
import gfx.TileSet;
import helper.ReadFromFile;
import helper.Utilities;
import main.GameController;
import game.Rendering.Renderer;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static helper.Arithmetik.*;
import static helper.Utilities.*;

public class GameMap {
	public static final String MAP_PATH = "maps";

	private final GameController gc;
	private ArrayList<TileSet> tileSets = new ArrayList<>();
	private HashMap<String, String> parameter = new HashMap<>();
	private ArrayList<BufferedImage> worldMapLayer = new ArrayList<>();;
	private HashMap<Integer, AnimationMapTile> animationsOnMap = new HashMap<>();
	private ArrayList<MapTransition> transitions = new ArrayList<>();
	private boolean[] collisionTiles;
	private Point mapShift = new Point();
	private boolean movedToLowerHalf = false;

	// ---

	public GameMap(GameController gc, String path) {
		this.gc = gc;

		try {
			JSONObject node = ReadFromFile.getJsonObject(GameMap.class, MAP_PATH + "/" + path + ".json");

			assert node != null;
			String mapFileName = node.getString("map-source");

			JSONObject transitions = node.getJSONObject("map-change");

			// Transition Nodes auswerten
			for (Iterator<String> it = transitions.keys(); it.hasNext(); ) {
				String setName = it.next();
				JSONObject transitionNode = transitions.getJSONObject(setName);
				this.transitions.add(new MapTransition(transitionNode));
			}

			InputStream file = ReadFromFile.getFile(GameMap.class, MAP_PATH + "/" + mapFileName);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);

			document.getDocumentElement().normalize();
			iterateElements(document.getDocumentElement());

			mapShift = new Point(0, 0);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(double timeGap) {
		Collection<AnimationMapTile> values = animationsOnMap.values();
		for (AnimationMapTile animMapTile : values) {
			// Tile wurde geändert
			if (animMapTile.isAnimationUpdated(timeGap)) {
				ArrayList<TilePosition> posis = animMapTile.getUsages();
				BufferedImage newTile = animMapTile.getCurrentTile();
				int tileW = newTile.getWidth();
				int tileH = newTile.getHeight();

				Map<Integer, List<Integer>> layerPositionMap = new HashMap<>();
				for (TilePosition pos : posis) {
					int layer = pos.getLayer() - 1;

					layerPositionMap.putIfAbsent(layer, new ArrayList<>());
					List<Integer> positions = layerPositionMap.get(layer);

					if (!positions.contains((int) pos.getId())) {
						positions.add((int) pos.getId());
					}
				}
				for (int layer : layerPositionMap.keySet()) {
					List<Integer> positions = layerPositionMap.get(layer);

					BufferedImage bf = replaceAreaWithTiles(worldMapLayer.get(layer), positions, newTile, tileW, tileH);
					worldMapLayer.set(layer, bf);
				}
			}
		}
	}

	public void draw(Renderer r, int layer) {
		if(worldMapLayer.size()>layer) {
			r.addGameLayer(worldMapLayer.get(layer), mapShift.x, mapShift.y);
	//		g2.drawImage(worldMapLayer.get(layer), mapShift.x, mapShift.y, null);
	//		g2.drawImage(worldMapLayer.get(layer),
	//				0, 0, gc.screenWidth, gc.screenHeight, mapShift.x*-1, mapShift.y*-1,
	//				(mapShift.x*-1) + gc.screenWidth, (mapShift.y*-1) + gc.screenHeight, null);
		}
	}

	// Getter und Setter

	public Point getTileSize() {
		return new Point(Integer.parseInt(parameter.get("tilewidth")) * gc.scale, Integer.parseInt(parameter.get("tileheight")) * gc.scale);
	}

	public Point getMapSize() {
		if(!worldMapLayer.isEmpty()) {
			return new Point(worldMapLayer.get(0).getWidth(), worldMapLayer.get(0).getHeight());
		}else{
			return new Point(0,0);
		}
	}

	public Point getMapShift() {
		return mapShift;
	}

	public void adjustMapShiftToPlayerPos(Point playerPos) {
		Point mapSize = getMapSize();
		Point screenSize = new Point(gc.screenWidth, gc.screenHeight);
		Point screenCenter = divide(screenSize, new Point(2, 2));

		int newX;

		if(mapSize.x >= gc.screenWidth) {
			newX = (playerPos.x - screenCenter.x) * -1;
			newX = Math.max(-1 * (mapSize.x - screenSize.x), Math.min(newX, 0));
		}else{
			newX = screenCenter.x - (mapSize.x / 2);
		}

		int newY;

		if(mapSize.x >= gc.screenHeight) {
			newY = (playerPos.y - screenCenter.y) * -1;

			// Flag Setzen, dass der Spieler sich nach unten aus der mitten raus bewegt
			movedToLowerHalf = (newY <= (-1 * (mapSize.y - screenSize.y)));

			newY = Math.max(-1 * (mapSize.y - screenSize.y), Math.min(newY, 0));
		}else{
			newY = screenCenter.y - (mapSize.y / 2);
		}

		mapShift = new Point(newX, newY);
	}

	public boolean hasTileOnMapColission(int x, int y) {
		int width = worldMapLayer.get(0).getWidth();
		int kachelW = Integer.parseInt(parameter.get("tilewidth")) * gc.scale;
		int kachelH = Integer.parseInt(parameter.get("tileheight")) * gc.scale;

		int kachelNummer = (y / kachelH) * (width / kachelW) + (x / kachelW);

	//	System.out.println("X: " + x + " Y: " + y + " W: " + width + " kachelW: " + kachelW + " kachelH: " + kachelH + " Pos: " + kachelNummer);

		return collisionTiles[kachelNummer];
	}

	public ArrayList<MapTransition> getMapTransitions() {
		return this.transitions;
	}

	// Utils

	private BufferedImage replaceAreaWithTiles(BufferedImage sourceImage, List<Integer> tileIds,
	                                           BufferedImage newTile, int newTileWidth, int newTileHeight) {
		int tilesPerRow = sourceImage.getWidth() / newTileWidth;

		Graphics2D g2d = sourceImage.createGraphics();
		g2d.drawImage(sourceImage, 0, 0, null);

		for(int tileId : tileIds) {
			int x = (tileId % tilesPerRow) * newTileWidth;
			int y = (tileId / tilesPerRow) * newTileWidth;

			g2d.drawImage(newTile, x, y, newTileWidth, newTileHeight, null);
		}
		g2d.dispose();
		return sourceImage;
	}

	// Evaluate .tmx Map File

	private void ParameterAuswerten(NamedNodeMap attributes) {
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			parameter.put(attribute.getNodeName(), attribute.getNodeValue());
		}
	}

	private void TileSetAuswerten(NamedNodeMap attributes) {
		int firstgid = 0;
		String source = "";

		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			switch (attribute.getNodeName()) {
				case "firstgid": {
					firstgid = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "source": {
					source = attribute.getNodeValue().replaceAll("\\.\\./", "");
					break;
				}
			}
		}

		String filename = getFileOfSource(source);

		tileSets.add(new TileSet(gc, filename, firstgid));
	}

	private void LayerAuswerten(Node node) {
		String name = "";
		int width = 0;
		int height = 0;

		HashMap<String, String> encodeCompression = new HashMap<>();

		NamedNodeMap attributes = node.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			switch (attribute.getNodeName()) {
				case "name": {
					name = attribute.getNodeValue();
					break;
				}
				case "width": {
					width = Integer.parseInt(attribute.getNodeValue());
					break;
				}
				case "height": {
					height = Integer.parseInt(attribute.getNodeValue());
					break;
				}
			}
		}
		if (collisionTiles == null) {
			collisionTiles = new boolean[width * height];
		}

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node dataNode = childNodes.item(i);
			if(dataNode.getNodeName().equals("data")) {
				// Data encoding lesen
				NamedNodeMap dataAttributes = dataNode.getAttributes();
				for (int j = 0; j < dataAttributes.getLength(); j++) {
					Node dataAttribute = dataAttributes.item(j);
					encodeCompression.put(dataAttribute.getNodeName(), dataAttribute.getNodeValue());
				}

				String encoded = dataNode.getTextContent().replaceAll("[\r\t\n) ]", "");

				byte[] decoded = encoded.getBytes();
				if (encodeCompression.containsKey("encoding")) {
					switch (encodeCompression.get("encoding")) {
						case "base64": {
							decoded = base64Decoder(encoded);
							break;
						}
						case "csv": {
							decoded = csvDecoder(encoded);
							break;
						}
					}
				}

				byte[] decompressed = decoded;
				if (encodeCompression.containsKey("compression")) {
					switch (encodeCompression.get("compression")) {
						case "gzip": {
							decompressed = gzipDecompress(decoded);
							break;
						}
						case "zlib": {
							decompressed = lzipDecompress(decoded);
							break;
						}
					}
				}

				boolean hasCollision = worldMapLayer.size() == 1;

				worldMapLayer.add(prepareImageLayerByEncoded(decompressed, width, height, hasCollision));
			}
		}
	}

	private BufferedImage prepareImageLayerByEncoded(byte[] decoded, int imageW, int imageH, boolean hasCollision) {
		ArrayList<BufferedImage> tiles = new ArrayList<>();

		for (int i = 0; i < decoded.length/4; i++) {
			long decode = (((decoded[i * 4 + 3] & 0xFFL) << 24) |
					((decoded[i * 4 + 2] & 0xFFL) << 16) |
					((decoded[i * 4 + 1] & 0xFFL) << 8) |
					(decoded[i * 4] & 0xFFL))-1;

			tiles.add(getTileById(decode));
			if(hasCollision) {
				collisionTiles[tiles.size()-1] = isTileByIdThere(decode);
			}

			TileSet ts = getTileSetById(decode);
			if(ts != null) {
				long tileIdOnTileSet = ts.isTileAnimated(decode);
				if(tileIdOnTileSet >= 0) {
					int layer = worldMapLayer.size()+1;

					if (animationsOnMap.containsKey((int) tileIdOnTileSet)) {
						animationsOnMap.get((int) tileIdOnTileSet).addUsages(new TilePosition(layer, tiles.size()-1));

					}else{
						AnimationTile at = ts.getAnimation(tileIdOnTileSet);
						AnimationMapTile amt = new AnimationMapTile(at);

						amt.addUsages(new TilePosition(layer, tiles.size()-1));

						animationsOnMap.put((int) tileIdOnTileSet, amt);
					}
				}
			}
		}

		if((tiles.size() != decoded.length / 4) || (tiles.isEmpty())) {
			throw new RuntimeException("Length of TileArray does not match.");
		}

		int tileWidth = tiles.get(0).getWidth();
		int tileHeight = tiles.get(0).getHeight();

		int combinedWidth = tileWidth * imageW;
		int combinedHeight = tileHeight * imageH;

		BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = combinedImage.createGraphics();
		int currentX = 0;
		int currentY = 0;

		for (BufferedImage image : tiles) {
			g2d.drawImage(image, currentX, currentY, null);
			currentX += tileWidth;

			if (currentX >= combinedWidth) {
				currentX = 0;
				currentY += tileHeight;
			}
		}

		g2d.dispose();

		return combinedImage;
	}

	private TileSet getTileSetById(long tileId) {

		for (TileSet ts : tileSets) {
			if (ts.isTileOnTileSet(tileId)) {
				return ts;
			}
		}
		return null;
	}

	private BufferedImage getTileById(long tileId) {

		for (TileSet ts : tileSets) {
			if (ts.isTileOnTileSet(tileId)) {
				return ts.getTileOfTileSet(tileId);
			}
		}
		return new BufferedImage(16 * gc.scale, 16 * gc.scale, BufferedImage.TYPE_INT_ARGB);
	}

	private boolean isTileByIdThere(long tileId) {

		for (TileSet ts : tileSets) {
			if (ts.isTileOnTileSet(tileId)) {
				return true;
			}
		}
		return false;
	}

	private void iterateElements(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			//	System.out.println("Element: " + node.getNodeName());
			switch (node.getNodeName()) {
				case "map": {
					ParameterAuswerten(node.getAttributes());
					break;
				}
				case "tileset": {
					TileSetAuswerten(node.getAttributes());
					break;
				}
				case "layer": {
					LayerAuswerten(node);
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

	public boolean isMovedToLowerHalf() {
		return movedToLowerHalf;
	}
}
