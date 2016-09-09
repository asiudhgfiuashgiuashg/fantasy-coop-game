package com.mygdx.game.server.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.shared.MapLoaderConstants;
import com.mygdx.game.shared.util.CollideablePolygon;

public class MapLoader {
	private static final XmlReader XML = new XmlReader();
	private Array<Tile> tiles;

	public void loadMap(String fileName) {
		tiles = new Array<Tile>();

		// TODO: make sure file is valid and read the XML root
		
		// create GameMap
		// loadTileSets(root);
		// loadLayers(root);

		// Clear up tile memory because they have served their purpose
		tiles.clear();
	}

	/**
	 * Load all tilesets in the given Tiled map file.
	 * 
	 * @param root
	 *            XML root of .tmx file
	 */
	private void loadTileSets(Element root) {
		for (Element tileset : root.getChildrenByName("tileset")) {
			loadTileSet(tileset);
		}
	}

	/**
	 * Loads the tiles from the given tileset as instances of the private inner
	 * Tile class.
	 * 
	 * @param tileset
	 *            tileset XML element
	 */
	private void loadTileSet(Element tileset) {
		int gid = tileset.getIntAttribute("firstgid");
		for (Element tile : tileset.getChildrenByName("tile")) {
			loadTile(tile, gid);
		}
	}

	/**
	 * Loads tile's hitbox and id and instantiates a new Tile instance.
	 * 
	 * @param tile
	 *            tile XML element
	 * @param gid
	 *            global id of tileset containing this tile
	 */
	private void loadTile(Element tile, int gid) {
		// Tile's local id
		int id = tile.getIntAttribute("id");

		// Load tiles that have polygon hitboxes
		for (Element objGroup : tile.getChildrenByName("objectgroup")) {
			// The first child named "object" is considered the hitbox
			Element object = null;
			if ((object = objGroup.getChildByName("object")) != null) {
				// The first child named "polygon" is the actual polygon hitbox
				Element polygon = null;
				if ((polygon = object.getChildByName("polygon")) != null) {
					// Borrowed code from libgdx BaseTmxMapLoader.java
					String[] points = polygon.getAttribute("points").split(" ");
					float[] vertices = new float[points.length * 2];
					for (int i = 0; i < points.length; i++) {
						String[] point = points[i].split(",");
						vertices[i * 2] = Float.parseFloat(point[0]);
						vertices[i * 2 + 1] = Float.parseFloat(point[1]);
					}
					CollideablePolygon p = new CollideablePolygon(vertices);

					// Append the tile to the list
					tiles.add(new Tile(gid + id, p));
				}
			}
		}
	}

	/**
	 * Loads static and dynamic entity layers.
	 * 
	 * @param root
	 *            XML root of .tmx file
	 */
	private void loadLayers(Element root) {
		for (Element layer : root.getChildrenByName("objectgroup")) {
			String name = layer.getName();
			if (name.equals(MapLoaderConstants.STATIC_ENTITIES_LAYER_NAME)) {
				loadStaticEntities(layer);
			} else if (name.equals(MapLoaderConstants.DYNAMIC_ENTITIES_LAYER_NAME)) {
				loadDynamicEntities(layer);
			}
		}
	}

	/**
	 * Load static entities from static entity layer.
	 * 
	 * @param layer
	 *            static entity layer XML element
	 */
	private void loadStaticEntities(Element layer) {
		for (Element entity : layer.getChildrenByName("object")) {
			// Object id from Tiled
			int id = entity.getIntAttribute("id");
			// Global id of static entity's tile
			int gid = entity.getIntAttribute("gid");

			// Entity position
			float x = entity.getFloatAttribute("x");
			float y = entity.getFloatAttribute("y");
			
			// By default visLayer is zero
			int visLayer = 0;

			Element properties = null;
			if ((properties = layer.getChildByName("properties")) != null) {
				Element visLayerProp = null;
				if ((visLayerProp = properties.getChildByName("visLayer")) != null) {
					// If visLayer was explicitly set, override default value
					visLayer = visLayerProp.getIntAttribute("value");
				}
			}
			
			// create new StaticEntity and add it to GameMap
		}
	}

	private void loadDynamicEntities(Element layer) {

	}

	/**
	 * The server's representation of a tile contains a hitbox to be copied by
	 * the entities that refer to the given tile id.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	private class Tile {
		/**
		 * As specified by Tiled, a tile's global id (gid) is the gid of its
		 * tilset plus its local id.
		 */
		public int gid;
		/** Tile hitbox, with no position */
		public CollideablePolygon hitbox;

		/**
		 * Constructs a temporary Tile to store the global id and hitbox.
		 * 
		 * @param gid
		 *            global id (gid of tileset + tile id)
		 * @param hitbox
		 *            polygonal hitbox
		 */
		public Tile(int gid, CollideablePolygon hitbox) {
			this.gid = gid;
			this.hitbox = hitbox;
		}
	}
}
