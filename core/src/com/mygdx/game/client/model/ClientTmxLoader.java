package com.mygdx.game.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.esotericsoftware.minlog.Log;

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


        TiledMap map = new TiledMap(); // initially the map is empty so it
        // must be filled with stuff
        MapLayers mapLayers = map.getLayers(); // add the loaded layers to this
        TiledMapTileSets tileSets = map.getTileSets(); // add the loaded
        // tilesets to this

        loadTileSets(xmlRoot, tileSets);


        return map;
    }

    /**
     * load every tile set specified in the map xml.
     * Xml elements representing tile sets are labeled "tileset"
     *
     * @param xmlRoot  the root of the map xml
     * @param tileSets the collection of tile sets belonging to the map we
     *                 are populating/loading
     */
    private void loadTileSets(Element xmlRoot, TiledMapTileSets tileSets) {
        for (Element tilesetXml : xmlRoot.getChildrenByName("tileset")) {
            tileSets.addTileSet(loadTileSet(tilesetXml));
        }
    }

    /**
     * loads  a tileset - including tile hitboxes and textures
     * @param tilesetXml
     * @return
     */
    private TiledMapTileSet loadTileSet(Element tilesetXml) {
        // the image for this tileset is in an element called "image" within
        // the tileset element
        String imageSrcFilename = tilesetXml.getChildByName("image").get
                ("source");
        FileHandle imgSrcFile = Gdx.files.internal(imageSrcFilename);

        TiledMapTileSet tileSet = new TiledMapTileSet();
        tileSet.setName(tilesetXml.getAttribute("name"));
        Log.debug("Loading tileset with name" + tileSet.getName());


        //load info needed to instantiate tiles

        // tiles have a globally unique id (gid). The gid of each tile in a
        // set is firstgid of the set + tile index. So the first tile in the
        // set will have a gid of firstgid, the second a gid of firstgid + 1,
        // etc.
        int firstGid = tilesetXml.getInt("firstgid");

        // width and height of tiles in pixels (needed to create
        // TextureRegions for each tile)
        int tileWidth = tilesetXml.getInt("tilewidth");
        int tileHeight = tilesetXml.getInt("tileheight");

        int tileCount = tilesetXml.getInt("tilecount");
        int numCols = tilesetXml.getInt("columns");
        int numRows = ((int) Math.ceil(((float) tileCount / numCols)));

        Texture tilesetTexture = new Texture(imgSrcFile); //TODO garbage
        // collect this

        // TODO detect if a tile should be animated and instantiate an
        // AnimatedTiledMapTile instead of a StaticTiledMapTile
        for (int gid = firstGid; gid < firstGid + tileCount; gid++) {
            int localTileIndex = gid - firstGid;
            int textureRegionX = (localTileIndex % numCols) * tileWidth;
            int textureRegionY = (localTileIndex / numCols) * tileHeight;

            TextureRegion tileTextureRegion = new TextureRegion
                    (tilesetTexture, textureRegionX, textureRegionY,
                            tileWidth, tileHeight);

            StaticTiledMapTile mapTile = new StaticTiledMapTile
                    (tileTextureRegion);
            mapTile.setId(gid);
        }
        return null;
    }
}
