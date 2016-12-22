package com.mygdx.game.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.game.shared.model.TilePolygonLoader;
import com.mygdx.game.shared.util.CollideablePolygon;

import java.io.IOException;

/**
 * Loads stuff relevant to the client from a tmx (Tiled) map file.
 */
public class ClientTmxLoader extends TmxMapLoader {

    private static final XmlReader XML = new XmlReader();

    @Override
    public ClientTiledMap load(String fileName) {
        FileHandle mapFile = Gdx.files.internal(fileName);
        ClientTiledMap tiledMap = new ClientTiledMap();
        try {
            this.root = XML.parse(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load tilesets (tile polygons not loaded)
        ImageResolver imageResolver = getImageResolver(mapFile);

        for (Element tilesetElement : root.getChildrenByName("tileset")) {
            loadTileSet(tiledMap, tilesetElement, mapFile, imageResolver);
        }
        // load tile layer
        // "Tile Layer 1" is what Tiled names the tile layer
        loadTileLayer(tiledMap, root.getChildByName("layer"));


        loadPolygons(tiledMap);

        // TODO instantiate static and dynamic entities
        return tiledMap;
    }

    private void loadPolygons(ClientTiledMap tiledMap) {
        for (Element tilesetXml: root.getChildrenByName("tileset")) {
            int firstGid = tilesetXml.getInt("firstgid", 1);
            for (Element tileXml: tilesetXml.getChildrenByName("tile")) {
                int localId = tileXml.getInt("id"); // local to the tileset
                int tileGid = firstGid + localId;
                // extract the hitbox
                CollideablePolygon tileHitbox = TilePolygonLoader.loadTilePolygon
                        (tileXml);
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