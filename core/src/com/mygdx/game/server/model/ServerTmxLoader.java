package com.mygdx.game.server.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.server.model.entity.StaticEntity;
import com.mygdx.game.server.model.entity.enemy.Enemy;
import com.mygdx.game.server.model.entity.friendly.Friendly;
import com.mygdx.game.server.model.entity.friendly.OldMan;
import com.mygdx.game.server.model.trigger.CutsceneTrigger;
import com.mygdx.game.server.model.trigger.MapLoadTrigger;
import com.mygdx.game.server.model.trigger.Trigger;
import com.mygdx.game.shared.model.TilePolygonLoader;
import com.mygdx.game.shared.model.UniqueIDAssigner;
import com.mygdx.game.shared.model.exceptions.MapLoaderException;
import com.mygdx.game.shared.model.CollideablePolygon;
import com.mygdx.game.shared.model.MapLoaderConstants;

/**
 * Loads the portions of the map that the server cares about
 * The server cares about:
 * -dynamic entities
 * -static entities (not their lights or textures though)
 * -boundaries
 * -triggers
 */
public class ServerTmxLoader {
	private static final XmlReader XML = new XmlReader();
	private static final String DEFAULT_STATIC_ENTITY_NAME = "DEFAULT_NAME";

	/**
	 * Temporary variables for use during loading
	 */
	private Map<Integer, Tile> tiles; // used to lookup tiles by gid since not
	// every tile will be loaded server
	// side,
	// only those with hitboxes needed to be
	// loaded
	private String mapName;
	private GameMap map;
	private int tileHeight;

	/**
	 * Loads a game map of given .tmx file name
	 *
	 * @param fileName .tmx Tiled map
	 * @return the loaded GameMap
	 * @throws IOException if fileName is invalid
	 * @throws various     reflection-related exceptions
	 */
	public GameMap loadMap(String fileName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		FileHandle file = Gdx.files.internal(fileName);
		if (null == file || !file.exists()) {
			throw new FileNotFoundException(fileName + " doesn't exist.");
		}

		Element root = XML.parse(file);
		tileHeight = root.getInt("tileheight");
		// need this value to convert the Tiled y values (y axis pointing
		// down) to in-game y values (y axis pointing up)
		float mapHeight = root.getFloat("height") * tileHeight;
		float mapWidth = root.getFloat("width") * tileHeight;

		// TODO: trim .tmx from mapName
		mapName = fileName;
		map = new GameMap(mapName);
		map.height = mapHeight;
		map.width = mapWidth;
		tiles = new HashMap<Integer, Tile>();

		loadTileSets(root);
		loadLayers(root, mapHeight);

		// Clear up tile memory because they have served their purpose
		tiles.clear();

		return map;
	}

	/**
	 * Load all tilesets in the given Tiled map file.
	 *
	 * @param root XML root of .tmx file
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
	 * @param tileset tileset XML element
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
	 * @param tile tile XML element
	 * @param gid  global id of tileset containing this tile
	 */
	private void loadTile(Element tile, int firstGid) {
		// Tile's local id
		int id = tile.getIntAttribute("id");

		float[] tilePolygonVertices = TilePolygonLoader.loadTilePolygon(tile);

		// Store tile in map
		int tileGid = firstGid + id;
		tiles.put(tileGid, new ServerTmxLoader.Tile(tilePolygonVertices));

	}

	/**
	 * Loads static and dynamic entity layers.
	 *
	 * @param root XML root of .tmx file
	 * @throws various reflection-related exceptions
	 */
	private void loadLayers(Element root, float mapHeight) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		for (Element layer : root.getChildrenByName("objectgroup")) {
			String name = layer.getAttribute("name");
			if (name.equals(MapLoaderConstants.STATIC_ENTITIES_LAYER_NAME)) {
				loadStaticEntities(layer, mapHeight);
			} else if (name.equals(MapLoaderConstants.DYNAMIC_ENTITIES_LAYER_NAME)) {
				loadDynamicEntities(layer, mapHeight);
			} else if (name.equals(MapLoaderConstants.TRIGGERS_LAYER_NAME)) {
				loadTriggers(layer, mapHeight);
			} else if (name.equals(MapLoaderConstants.BOUNDARIES_LAYER_NAME)) {
				loadBoundaries(layer, mapHeight);
			} else if (name.equals(MapLoaderConstants.SPAWN_LAYER_NAME)) {
				loadPlayerSpawns(layer, mapHeight);
			}
		}
	}

	/**
	 * Load player spawn
	 * A spawn is a rectangle with width and height
	 *
	 * In the future there will be multiple possible spawns (for moving between maps)
	 * @param layer
	 * @param mapHeight
	 */
	private void loadPlayerSpawns(Element layer, float mapHeight) {
		for (Element entity : layer.getChildrenByName("object")) {
			String name = entity.get("name");
			if ("Spawn".equals(name)) {
				float x = entity.getFloat("x");
				float y = mapHeight - entity.getFloat("y");
				float width  = entity.getFloat("width");
				float height = entity.getFloat("height");
				Rectangle spawn = new Rectangle(x, y, width, height);
				map.playerSpawn = spawn;
			}
		}
	}

	/**
	 * Load static entities from static entity layer.
	 *
	 * @param layer static entity layer XML element
	 */
	private void loadStaticEntities(Element layer, float mapHeight) {
		for (Element entity : layer.getChildrenByName("object")) {
			// Object id from Tiled
			int id = entity.getIntAttribute("id");
			// Global id of static entity's tile
			int tileGid = entity.getIntAttribute("gid");

			// Entity name
			String name = entity.get("name", DEFAULT_STATIC_ENTITY_NAME);

			// Entity position
			float x = entity.getFloatAttribute("x");
			float y = mapHeight - entity.getFloatAttribute("y");

			// By default visLayer is zero
			int visLayer = 0;

			// By default entity is non-solid
			boolean solid = false;
			Element properties = null;
			if ((properties = entity.getChildByName("properties")) != null) {
				for (Element prop : properties.getChildrenByName("property")) {
					if ((prop.getAttribute("name", "")).equalsIgnoreCase("visLayer")) {
						// If visLayer was explicitly set, override default
						// value
						visLayer = prop.getIntAttribute("value");
					} else if ((prop.getAttribute("name", "")).equalsIgnoreCase("solid")) {
						// if solid property was set
						solid = prop.getBoolean("value");
					}
				}
			}

			// create new StaticEntity and add it to GameMap
			String uid = UniqueIDAssigner.generateStaticEntityUID(name, mapName, id);
			Tile tile = tiles.get(tileGid);

			map.addStaticEntity(new StaticEntity(uid, new Vector2(x, y), visLayer, solid, tile.hitboxVertices));
		}
	}

	/**
	 * Load dynamic entities from dynamic entity layer.
	 *
	 * @param layer     dynamic entity layer XML element
	 * @param mapHeight
	 * @throws MapLoaderException if the entity is not a valid class type
	 * @throws various            reflection-related exceptions
	 */
	private void loadDynamicEntities(Element layer, float mapHeight) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for (Element entity : layer.getChildrenByName("object")) {
			// Entity name and type (used for reflection)
			String name = entity.get("name");
			String type = entity.get("type");

			/* Entity position - the bottom left corner of the entity will be
			in the center of the Tiled oval */
			float x = entity.getFloatAttribute("x") + entity.getFloatAttribute("width") / 2;
			float y = mapHeight - entity.getFloatAttribute("y") - entity.getFloatAttribute("height", 0) / 2;

			// By default visLayer is zero
			int visLayer = 0;

			Element properties = null;
			boolean solid = false;
			if ((properties = entity.getChildByName("properties")) != null) {
				for (Element prop : properties.getChildrenByName("property")) {
					if ((prop.getAttribute("name", "")).equalsIgnoreCase("visLayer")) {
						// If visLayer was explicitly set, override default
						// value
						visLayer = prop.getIntAttribute("value");
					} else if ((prop.getAttribute("name", "")).equalsIgnoreCase("solid")) {
						// if solid property was set
						solid = prop.getBoolean("value");
					}
				}
			}

			// Generate uid
			String uid = UniqueIDAssigner.generateDynamicEntityUID(name);

			// Use reflection to instantiate and add to game map
			if (type.equals(MapLoaderConstants.ENEMY_TYPE)) {
				Class<?> c = Class.forName(MapLoaderConstants.BASE_PACKAGE + "." + MapLoaderConstants.ENEMY_PACKAGE + "." + name);
				Constructor<?> cons = c.getDeclaredConstructor(String.class, Vector2.class, int.class, boolean.class);
				cons.setAccessible(true); // need to call this for non-public
				// constructor
				Enemy enemy = (Enemy) cons.newInstance(uid, new Vector2(x, y), visLayer, solid);
				map.addEnemy(enemy);
			} else if (type.equalsIgnoreCase(MapLoaderConstants.FRIENDLY_TYPE)) {
				Class<?> c = Class.forName(MapLoaderConstants.BASE_PACKAGE + "." + MapLoaderConstants.FRIENDLY_PACKAGE + "." + name);
				Constructor<?> cons = c.getConstructor(String.class, Vector2.class, int.class, boolean.class);
				Friendly friendly = (Friendly) cons.newInstance(uid, new Vector2(x, y), visLayer, solid);
				map.addFriendly(friendly);
			} else {
				throw new MapLoaderException();
			}
		}
	}

	/**
	 * Load triggers from trigger layer.
	 *
	 * @param layer     trigger layer XML element
	 * @param mapHeight
	 * @throws various reflection-related exceptions TODO - make it so that
	 *                 trigger
	 *                 constructors can have arbitrary parameters (which
	 *                 will be
	 *                 specified in Tiled)
	 */
	private void loadTriggers(Element layer, float mapHeight) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
			float y = mapHeight - trigger.getFloatAttribute("y");
			float width = trigger.getFloatAttribute("width");
			float height = trigger.getFloatAttribute("height");

			Trigger trig = null;

			// Triggers are rectangles
			float[] vertices = {0, 0, 0, height, width, height, width, 0};

			// Check for special classes of triggers
			// if a trigger's type is given, then use it to choose what generic
			// trigger type to instantiate
			// Use the name as an instantiation parameter for a generic tr
			if (type != null) {
				if (type.equals(MapLoaderConstants.CUTSCENE_TRIGGER_TYPE)) {
					trig = new CutsceneTrigger(vertices, name, new Vector2(x, y));
				} else if (type.equals(MapLoaderConstants.MAPLOAD_TRIGGER_TYPE)) {
					trig = new MapLoadTrigger(vertices, name, new Vector2(x, y));
				}
				// if a trigger's type is not given, then use the name field to
				// figure out what class to instantiate
			} else {
				// Use reflection to create trigger
				Class<?> c = Class.forName(MapLoaderConstants.BASE_PACKAGE + "" + "." + MapLoaderConstants.TRIGGER_PACKAGE + "." + name);
				Constructor<?> cons = c.getConstructor(CollideablePolygon.class);
				Object o;
				o = cons.newInstance(vertices);
				trig = (Trigger) o;
			}
			map.getTriggers().add(trig);
		}
	}

	/**
	 * Loads boundaries from boundaries layer.
	 *
	 * @param layer     boundaries layer XML element
	 * @param mapHeight
	 */
	private void loadBoundaries(Element layer, float mapHeight) {
		for (Element object : layer.getChildrenByName("object")) {
			// Load polygon			
			float[] vertices = TilePolygonLoader.loadPolygonVertices(object, tileHeight);

			// Boundary position
			float x = object.getFloatAttribute("x");
			float y = mapHeight - object.getFloatAttribute("y");

			Boundary b = new Boundary(vertices, new Vector2(x, y));
			map.getBoundaries().add(b);
		}
	}

	/**
	 * The server's representation of a tile contains a hitbox to be copied by
	 * the entities that refer to the given tile id.
	 *
	 * @author Sawyer Harris
	 */
	private class Tile {
		/**
		 * Tile hitbox, with no position
		 */
		public float[] hitboxVertices;

		/**
		 * Constructs a temporary Tile to store the global id and hitbox.
		 *
		 * @param gid            global id (gid of tileset + tile id)
		 * @param hitboxVertices polygonal hitbox vertices (specify counterclockwwise please)
		 */
		public Tile(float[] vertices) {
			this.hitboxVertices = vertices;
		}
	}
}
