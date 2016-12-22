package com.mygdx.game.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.esotericsoftware.minlog.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Loads stuff relevant to the client from a tmx (Tiled) map file.
 */
public class ClientTmxLoader extends TmxMapLoader {

    private static final XmlReader XML = new XmlReader();

    @Override
    public TiledMap load(String fileName) {
        FileHandle mapFile = Gdx.files.internal(fileName);
        TiledMap tiledMap = new TiledMap();
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

        // TODO load in polygon hitboxes associated with some tiles
        // TODO instantiate static and dynamic entities
        return tiledMap;
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