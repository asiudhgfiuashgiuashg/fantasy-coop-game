package com.mygdx.game.client.model;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.client.model.entity.StaticEntity;
import com.mygdx.game.client.view.CustomTiledMapRenderer;
import com.mygdx.game.shared.model.TilePolygonLoader;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.client.model.GameClient.console;


/**
 * Loads stuff relevant to the client from a tmx (Tiled) map file.
 */
public class ClientTmxLoader extends TmxMapLoader {

	private static final XmlReader XML = new XmlReader();
	private int tileHeight;
	private static final float DEFAULT_LIGHT_DISTANCE = 200;

	public ClientTiledMap load(String fileName, RayHandler rayHandler) {
		FileHandle mapFile = Gdx.files.internal(fileName);
		ClientTiledMap tiledMap = new ClientTiledMap();
		try {
			this.root = XML.parse(mapFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tileHeight = root.getInt("tileheight");

		// load tilesets (tile polygons not loaded)
		ImageResolver imageResolver = getImageResolver(mapFile);

		for (Element tilesetElement : root.getChildrenByName("tileset")) {
			console.log("loading tileset " + tilesetElement.getAttribute
					("name"));
			loadTileSet(tiledMap, tilesetElement, mapFile, imageResolver);
		}
		// load tile layer
		// "Tile Layer 1" is what Tiled names the tile layer
		loadTileLayer(tiledMap, root.getChildByName("layer"));


		loadPolygonsAndLights(tiledMap, rayHandler);

		loadEntities(tiledMap, rayHandler);
		return tiledMap;
	}

	/**
	 * @param tiledMap
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private void loadEntities(ClientTiledMap tiledMap, RayHandler rayHandler) {
		loadStaticEntities(tiledMap, rayHandler);
	}

	/**
	 * @param tiledMap
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private void loadStaticEntities(ClientTiledMap tiledMap, RayHandler
			rayHandler) {
		Array<Element> objectGroups = root.getChildrenByName("objectgroup");

		// look for the Static Entities layer
		boolean foundStaticEntitiesLayer = false;
		for (int i = 0; i < objectGroups.size && !foundStaticEntitiesLayer;
			 i++) {
			Element objectGroup = objectGroups.get(i);
			if (objectGroup.getAttribute("name").equals("Static Entities")) {
				foundStaticEntitiesLayer = true;
				// load the TiledMapTileMapObjects for the StaticEntities
				loadObjectGroup(tiledMap, objectGroup);
				// Create the StaticEntities.
				// There'll be a static entity for each TiledMapTileMapObject
				// since a TiledMapTileMapObject is the visual component of a
				// static entity.
				// Get the TiledMapTlieMapObjects layer we just loaded.
				MapLayer objectsLayer = tiledMap.getLayers().get("Static " +
						"Entities");
				for (MapObject mapObject : objectsLayer.getObjects()) {
					TiledMapTileMapObject tileMapObject =
							(TiledMapTileMapObject) mapObject; // I know
					// casting is gross but we need to do it here if we want
					// to use libgdx's included objectgroup loading instead
					// of doing it ourselves.
					float mapHeight = root.getFloat("height") * tileHeight;

					CollideablePolygon hitboxPolygon = tiledMap
							.gidToPolygonMap.get(tileMapObject.getTile().getId
									());

					List<FlickerPointLight> lights = tiledMap.gidToLightsMap
							.get(tileMapObject.getTile().getId());

					StaticEntity staticEntity = new StaticEntity
							(hitboxPolygon, lights, tileMapObject, mapHeight,
									tileHeight, rayHandler);
					tiledMap.staticEntities.add(staticEntity);
				}
			}
		}
	}

	/**
	 * Tiles can contain polygons and lights. The reason for this is that
	 * tiles may be used to create static entities, and a static entity might
	 * need a polygon hitbox and lights associated with it.
	 *
	 * @param tiledMap map to load (tile gid) -> (polygon and light) maps into
	 */
	private void loadPolygonsAndLights(ClientTiledMap tiledMap, RayHandler
			rayHandler) {
		for (Element tilesetXml : root.getChildrenByName("tileset")) {
			int firstGid = tilesetXml.getInt("firstgid", 1);
			for (Element tileXml : tilesetXml.getChildrenByName("tile")) {
				int localId = tileXml.getInt("id"); // local to the tileset
				int tileGid = firstGid + localId;
				// extract the hitbox
				CollideablePolygon tileHitbox = TilePolygonLoader
						.loadTilePolygon(tileXml);
				List<FlickerPointLight> tileLights = loadLights(tileXml,
						rayHandler);
				tiledMap.gidToPolygonMap.put(tileGid, tileHitbox);
				tiledMap.gidToLightsMap.put(tileGid, tileLights);
			}
		}
	}

	/**
	 * load the lights associated with this tile so that entities which use
	 * this tile can use the lights
	 *
	 * @param tileXml
	 * @return
	 */
	private List<FlickerPointLight> loadLights(Element tileXml, RayHandler
			rayHandler) {
		List<FlickerPointLight> lights = new ArrayList<FlickerPointLight>();
		int tileHeight = tileXml.getChildByName("image").getInt("height");
		for (Element xmlObj : tileXml.getChildrenByNameRecursively("object")) {
			String name = xmlObj.getAttribute("name", null);
			// if found a light on this tile
			if (name != null && name.equals("light")) {
				// create a light at the center of the oval that was drawn
				float x = xmlObj.getFloatAttribute("x");
				float y = tileHeight - xmlObj.getFloatAttribute("y");
				float width = xmlObj.getFloatAttribute("width");
				x = x + width / 2;
				float height = xmlObj.getFloatAttribute("height");
				y = y - height / 2;
				// load stuff like light distance and color from the additional
				// properties
				float distance = DEFAULT_LIGHT_DISTANCE;
				Element properties = xmlObj.getChildByName("properties");
				Color color = null;
				float flickerRate = 0;
				if (properties != null) {
					for (Element property : properties.getChildrenByName
							("property")) {
						String propName = property.getAttribute("name");
						// light distance
						if (propName.equals("distance")) {
							distance = property.getFloatAttribute("value");
						} else if (propName.equals("color")) {
							String colorHexStr = property.getAttribute
									("value");
							Long rgba8888l = Long.parseLong(colorHexStr, 16);
							int rgba8888 = rgba8888l.intValue();
							color = new Color(rgba8888);
						} else if (propName.equals("flicker")) {
							flickerRate = property.getFloatAttribute("value");
						}
					}
				}


				FlickerPointLight tempLight = new FlickerPointLight
						(rayHandler, CustomTiledMapRenderer.NUM_RAYS, color,
								distance, x, y, flickerRate);
				tempLight.remove(); // don't render this temporary light.
				// It'll be used to create the actual entity lights that DO
				// get rendered
				lights.add(tempLight);
			}
		}
		return lights;
	}

	/**
	 * borrowed from superclass
	 * An ImageResolver takes in an image name and returns a texture region
	 * for that image.
	 */
	private ImageResolver getImageResolver(FileHandle mapFile) {
		Array<FileHandle> textureFiles = null;
		try {
			textureFiles = loadTilesets(root, mapFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectMap<String, Texture> textures = new ObjectMap<String, Texture>();


		for (FileHandle textureFile : textureFiles) {
			Texture texture = new Texture(textureFile);
			textures.put(textureFile.path(), texture);
		}
		return new ImageResolver.DirectImageResolver(textures);

	}
}