package com.mygdx.game.server.model;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.server.model.trigger.Trigger;
import com.mygdx.game.shared.MapLoaderConstants;
import com.mygdx.game.shared.UniqueIDAssigner;
import com.mygdx.game.shared.exceptions.MapLoaderException;
import com.mygdx.game.shared.util.CollideablePolygon;

public class MapLoader {
	private static final XmlReader XML = new XmlReader();

	/** Temporary variables for use during loading */
	private Array<Tile> tiles;
	private String mapName;
	private GameMap map;

	/**
	 * Loads a game map of given .tmx file name
	 * 
	 * @param fileName
	 *            .tmx Tiled map
	 * @throws IOException
	 *             if fileName is invalid
	 */
	public void loadMap(String fileName) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, MapLoaderException {

		Element root = XML.parse(new FileHandle(fileName));

		// TODO: trim .tmx from mapName
		mapName = fileName;
		map = new GameMap(mapName);
		tiles = new Array<Tile>();

		loadTileSets(root);
		loadLayers(root);

		// Set current game map
		Server.getInstance().setMap(map);

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
	private void loadLayers(Element root)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException {

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
			int tileGid = entity.getIntAttribute("gid");

			// Entity name
			String name = entity.get("name");

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
			String uid = UniqueIDAssigner.generateStaticEntityUID(name, mapName, id);

			CollideablePolygon hitbox = new CollideablePolygon(tiles.get(tileGid).hitbox);

			map.addStaticEntity(new StaticEntity(uid, new Vector2(x, y), visLayer, hitbox));
		}
	}

	/**
	 * Load dynamic entities from static entity layer.
	 * 
	 * @param layer
	 *            static entity layer XML element
	 * @throws ClassNotFoundException
	 *             if dynamic entity name is not a Java class
	 * @throws IllegalAccessException
	 *             if there is a problem with reflection
	 * @throws InstantiationException
	 *             if the entity cannot be instantiated
	 * @throws MapLoaderException
	 *             if the entity is not a valid class type
	 */
	private void loadDynamicEntities(Element layer)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException {
		for (Element entity : layer.getChildrenByName("object")) {
			// Entity name (used for reflection)
			String name = entity.get("name");

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

			// Use reflection to create dynamic entity
			Class c = Class.forName(name);
			Object o = c.newInstance();

			// Add it to the game map
			if (o instanceof Enemy) {
				Enemy enemy = (Enemy) o;
				map.addEnemy(enemy);
			} else if (o instanceof NonEnemyCharacter) {
				NonEnemyCharacter nec = (NonEnemyCharacter) o;
				map.addNonEnemyCharacter(nec);
			} else if (o instanceof ActiveSpell) {
				ActiveSpell spell = (ActiveSpell) o;
				map.addActiveSpell(spell);
			} else if (o instanceof Projectile) {
				Projectile proj = (Projectile) o;
				map.addProjectile(proj);
			} else if (o instanceof Trigger) {
				Trigger trig = (Trigger) o;
				map.addTrigger(trig);
			} else if (o instanceof StaticEntity) {
				StaticEntity ent = (StaticEntity) o;
				map.addStaticEntity(ent);
			} else {
				throw new MapLoaderException();
			}
		}
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
		 * tileset plus its local id.
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
