package com.mygdx.game.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Loads stuff relevant to the client from a tmx (Tiled) map file.
 */
public class ClientTmxLoader {

    private static final XmlReader XML = new XmlReader();


    public TiledMap loadMap(String fileName) throws IOException {
        FileHandle file = Gdx.files.internal(fileName);
        if (null == file || !file.exists()) {
            throw new FileNotFoundException(fileName + " doesn't exist.");
        }
        XmlReader.Element xmlRoot = XML.parse(file);


        TiledMap map = new TiledMap(); // initially the map is empty so it must be filled with stuff
        MapLayers mapLayers = map.getLayers(); // add the loaded layers to this
        TiledMapTileSets tileSets = map.getTileSets(); // add the loaded tilesets to this

        loadTileSets(xmlRoot, tileSets);



        return map;
    }

    /**
     * load every tile set specified in the map xml.
     * Xml elements representing tile sets are labeled "tileset"
     * @param xmlRoot the root of the map xml
     * @param tileSets the collection of tile sets belonging to the map we are populating/loading
     */
    private void loadTileSets(Element xmlRoot, TiledMapTileSets tileSets) {
        for (Element tilesetXml : xmlRoot.getChildrenByName("tileset")) {
            tileSets.addTileSet(loadTileSet(tilesetXml));
        }
    }

    private TiledMapTileSet loadTileSet(Element tilesetXml) {
        return null;
    }
}
