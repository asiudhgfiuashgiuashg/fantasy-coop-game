package com.mygdx.game.client.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.shared.util.CollideablePolygon;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard TiledMap with the following extras:
 * - keeps track of StaticEntities and ActiveEntities
 * - keeps a mapping of tile gid -> polygon hitbox (since in Tiled we specify
 * hitboxes on some tiles that are later used for StaticEntities)
 */
public class ClientTiledMap extends TiledMap {
	protected final Map<Integer, CollideablePolygon> gidToPolygonMap = new
	HashMap<Integer, CollideablePolygon>();
}
