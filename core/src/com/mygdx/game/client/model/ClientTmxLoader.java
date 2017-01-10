package com.mygdx.game.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.mygdx.game.shared.model.TilePolygonLoader;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.io.IOException;

import static com.mygdx.game.client.model.GameClient.console;


/**
 * Loads stuff relevant to the client from a tmx (Tiled) map file.
 */
public class ClientTmxLoader extends TmxMapLoader {

    private static final XmlReader XML = new XmlReader();
    private int tileHeight;

    @Override
    public ClientTiledMap load(String fileName) {
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


        loadPolygons(tiledMap);

        loadEntities(tiledMap);
        return tiledMap;
    }

    private void loadEntities(ClientTiledMap tiledMap) {
        loadStaticEntities(tiledMap);
    }

    private void loadStaticEntities(ClientTiledMap tiledMap) {
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
                for (MapObject mapObject: objectsLayer.getObjects()) {
                    TiledMapTileMapObject tileMapObject =
                            (TiledMapTileMapObject) mapObject; // I know
                    // casting is gross but we need to do it here if we want
                    // to use libgdx's included objectgroup loading instead
                    // of doing it ourselves.
                    float mapHeight = root.getFloat("height") * tileHeight;

                    CollideablePolygon hitboxPolygon = tiledMap
                            .gidToPolygonMap.get(tileMapObject.getTile()
                                    .getId());

                    StaticEntity staticEntity = new StaticEntity(hitboxPolygon,
                            tileMapObject, mapHeight, tileHeight);
                    tiledMap.staticEntities.add(staticEntity);
                }
            }
        }
    }

    private void loadPolygons(ClientTiledMap tiledMap) {
        for (Element tilesetXml: root.getChildrenByName("tileset")) {
            int firstGid = tilesetXml.getInt("firstgid", 1);
            for (Element tileXml: tilesetXml.getChildrenByName("tile")) {
                int localId = tileXml.getInt("id"); // local to the tileset
                int tileGid = firstGid + localId;
                // extract the hitbox
                CollideablePolygon tileHitbox = TilePolygonLoader.loadTilePolygon
                        (tileXml, tileHeight);
                tiledMap.gidToPolygonMap.put(tileGid, tileHitbox);
            }
        }
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