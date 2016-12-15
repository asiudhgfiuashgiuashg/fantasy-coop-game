package com.mygdx.game.server.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.server.model.entity.StaticEntity;
import com.mygdx.game.server.model.entity.enemy.Enemy;
import com.mygdx.game.server.model.entity.enemy.SpiderBoss;
import com.mygdx.game.server.model.entity.friendly.Friendly;
import com.mygdx.game.server.model.trigger.CutsceneTrigger;
import com.mygdx.game.server.model.trigger.MapLoadTrigger;
import com.mygdx.game.server.model.trigger.Trigger;
import com.mygdx.game.shared.MapLoaderConstants;
import com.mygdx.game.shared.UniqueIDAssigner;
import com.mygdx.game.shared.exceptions.MapLoaderException;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * Loads the portions of the map that the server cares about
 */
public class MapLoader {
	private static final XmlReader XML = new XmlReader();
	private static final String DEFAULT_STATIC_ENTITY_NAME = "DEFAULT_NAME";

	/** Temporary variables for use during loading */
	private Map<Integer, Tile> tiles; // used to lookup tiles by gid since not every tile will be loaded server side,
										//  only those with hitboxes needed to be loaded
	private String mapName;
	private GameMap map;

	/**
	 * Loads a game map of given .tmx file name
	 * 
	 * @param fileName
	 *            .tmx Tiled map
	 * @throws IOException
	 *             if fileName is invalid
	 * @throws various
	 *             reflection-related exceptions
	 */
	public void loadMap(String fileName) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, MapLoaderException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		FileHandle file = Gdx.files.internal(fileName);
		if (null == file || !file.exists())
			throw new FileNotFoundException(fileName + " doesn't exist.");
		Element root = XML.parse(file);

		// TODO: trim .tmx from mapName
		mapName = fileName;
		map = new GameMap(mapName);
		tiles = new HashMap<Integer, Tile>();

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
		int firstGid = tileset.getIntAttribute("firstgid");
		for (Element tile : tileset.getChildrenByName("tile")) {
			loadTile(tile, firstGid);
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
	private void loadTile(Element tile, int firstGid) {
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
					// With fix to vertices coordinates
					String[] points = polygon.getAttribute("points").split(" ");
					float[] vertices = new float[points.length * 2];
					for (int i = 0; i < points.length; i++) {
						String[] point = points[i].split(",");
						vertices[i * 2] = Float.parseFloat(point[0]) + object.getFloatAttribute("x");
						vertices[i * 2 + 1] = object.getFloatAttribute("y") - Float.parseFloat(point[1]);
					}
					CollideablePolygon p = new CollideablePolygon(vertices);

					// Store tile in map
					int tileGid = firstGid + id;
					tiles.put(tileGid, new Tile(p));
				}
			}
		}
	}

	/**
	 * Loads static and dynamic entity layers.
	 * 
	 * @param root
	 *            XML root of .tmx file
	 * @throws various
	 *             reflection-related exceptions
	 */
	private void loadLayers(Element root)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		for (Element layer : root.getChildrenByName("objectgroup")) {
			String name = layer.getAttribute("name");
			if (name.equals(MapLoaderConstants.STATIC_ENTITIES_LAYER_NAME)) {
			loadStaticEntities(layer);
			} else if (name.equals(MapLoaderConstants.DYNAMIC_ENTITIES_LAYER_NAME)) {
				loadDynamicEntities(layer);
			} else if (name.equals(MapLoaderConstants.TRIGGERS_LAYER_NAME)) {
				loadTriggers(layer);
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
			String name = entity.get("name", DEFAULT_STATIC_ENTITY_NAME);

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
			Tile tile = tiles.get(tileGid);
			CollideablePolygon hitboxPolygon = null;
			if (null != tile) {
				hitboxPolygon = new CollideablePolygon(tile.hitbox);
			}

			map.addStaticEntity(new StaticEntity(uid, new Vector2(x, y), visLayer, hitboxPolygon));
		}
	}

	/**
	 * Load dynamic entities from dynamic entity layer.
	 * 
	 * @param layer
	 *            dynamic entity layer XML element
	 * @throws MapLoaderException
	 *             if the entity is not a valid class type
	 * @throws various
	 *             reflection-related exceptions
	 */
	private void loadDynamicEntities(Element layer)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for (Element entity : layer.getChildrenByName("object")) {
			// Entity name and type (used for reflection)
			String name = entity.get("name");
			String type = entity.get("type");

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

			// Generate uid
			String uid = UniqueIDAssigner.generateDynamicEntityUID(name);

			// Use reflection to instantiate and add to game map
			if (type.equals(MapLoaderConstants.ENEMY_TYPE)) {
				Class<?> c = Class.forName(MapLoaderConstants.BASE_PACKAGE + "." + MapLoaderConstants.ENEMY_PACKAGE + "." + name);
				Constructor<?> cons = c.getDeclaredConstructor(String.class, Vector2.class, int.class);
				cons.setAccessible(true); // need to call this for non-public constructor
				Enemy enemy = (Enemy) cons.newInstance(uid, new Vector2(x, y), visLayer);
				map.addEnemy(enemy);
			} else if (type.equals(MapLoaderConstants.FRIENDLY_TYPE)) {
				Class<?> c = Class
						.forName(MapLoaderConstants.BASE_PACKAGE + MapLoaderConstants.FRIENDLY_PACKAGE + name);
				Constructor<?> cons = c.getConstructor(String.class, Vector2.class, int.class);
				Friendly friendly = (Friendly) cons.newInstance(uid, new Vector2(x, y), visLayer);
				map.addFriendly(friendly);
			} else {
				throw new MapLoaderException();
			}
		}
	}

	/**
	 * Load dynamic entities from trigger layer.
	 * 
	 * @param layer
	 *            trigger layer XML element
	 * @throws various
	 *             reflection-related exceptions
	 */
	private void loadTriggers(Element layer) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Element trigger : layer.getChildrenByName("object")) {
			// Trigger name and type (used for reflection, instantiation)
			String name = trigger.get("name");
			String type;
			try {
				type = trigger.get("type");
			} catch (GdxRuntimeException e) {
				type = null;
			}

			// Trigger position, width, height
			float x = trigger.getFloatAttribute("x");
			float y = trigger.getFloatAttribute("y");
			float width = trigger.getFloatAttribute("width");
			float height = trigger.getFloatAttribute("height");

			Trigger trig = null;
			float[] vertices = { x, y, x + width, y, x + width, y + height, x, y + height };
			CollideablePolygon hitbox = new CollideablePolygon(vertices);

			// Check for special classes of triggers
			// if a trigger's type is given, then use it to choose what generic trigger type to instantiate
			if (type != null) {
				if (type.equals(MapLoaderConstants.CUTSCENE_TRIGGER_TYPE)) {
					trig = new CutsceneTrigger(hitbox, name);
				} else if (type.equals(MapLoaderConstants.MAPLOAD_TRIGGER_TYPE)) {
					trig = new MapLoadTrigger(hitbox, name);
				}
			// if a trigger's type is not given, then use the name field to figure out what class to instantiate
			} else {
				// Use reflection to create trigger
				Class<?> c = Class.forName(MapLoaderConstants.BASE_PACKAGE + "." + MapLoaderConstants.TRIGGER_PACKAGE + "." + name);
				Constructor<?> cons = c.getConstructor(CollideablePolygon.class);
				Object o;
				o = cons.newInstance(hitbox);
				trig = (Trigger) o;
			}
			if (null == trigger) {
				throw new IllegalArgumentException("Invalid Trigger: name: " + trigger.get("name") + " type: " + trigger.get("type"));
			} else {
				map.addTrigger(trig);
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
		public Tile(CollideablePolygon hitbox) {
			this.hitbox = hitbox;
		}
	}
}
