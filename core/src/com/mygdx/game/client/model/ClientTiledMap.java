package com.mygdx.game.client.model;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.client.model.entity.StaticEntity;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard TiledMap with the following extras:
 * - keeps track of StaticEntities and ActiveEntities
 * - keeps a mapping of tile gid -> polygon hitbox (since in Tiled we specify
 * hitboxes on some tiles that are later used for StaticEntities)
 * - keeps a mapping of tile gid -> lights list (since in Tiled some tiles
 * have lights associated with them)
 */
public class ClientTiledMap extends TiledMap {
	protected final Map<Integer, CollideablePolygon> gidToPolygonMap = new
	HashMap<Integer, CollideablePolygon>();
	protected final Map<Integer, List<PointLight>> gidToLightsMap = new HashMap
			<Integer, List<PointLight>>();
	public final List<StaticEntity>  staticEntities = new
			ArrayList<StaticEntity>();

}
