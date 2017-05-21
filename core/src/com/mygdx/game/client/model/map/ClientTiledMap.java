package com.mygdx.game.client.model.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.client.model.FlickerPointLight;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.client.model.entity.MapEntity;
import com.mygdx.game.client.model.entity.StaticEntity;
import com.mygdx.game.client.model.entity.player.Player;
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
	protected final Map<Integer, float[]> gidToPolygonVerticesMap = new
			HashMap<Integer, float[]>();
	protected final Map<Integer, List<FlickerPointLight>> gidToLightsMap = new
			HashMap<Integer, List<FlickerPointLight>>();
	public final List<StaticEntity> staticEntities = new
			ArrayList<StaticEntity>();
	public final List<DynamicEntity> dynamicEntities = new ArrayList
			<DynamicEntity>();

	public final List<CollideablePolygon> solidEntities = new ArrayList<CollideablePolygon>();

	public Player localPlayer;

	public DynamicEntity getDynamicEntityByUid(String entityUID) {
		for (DynamicEntity entity: dynamicEntities) {
			if (entity.getUid().equals(entityUID)) {
				return entity;
			}
		}
		return null;
	}

	public void addDynamicEntity(DynamicEntity entity) {
		dynamicEntities.add(entity);
		addEntity(entity);
	}

	public void addStaticEntity(StaticEntity entity) {
		staticEntities.add(entity);
		addEntity(entity);
	}

	/**
	 * Put entity in solid entities list if it is solid
	 * @param entity
	 */
	private void addEntity(MapEntity entity) {
		if (entity.solid) {
			solidEntities.add(entity);
		}
	}

	public void addLocalPlayer(Player player) {
		localPlayer = player;
		addDynamicEntity(player);
	}

	private void removeDynamicEntity(DynamicEntity entity) {
		dynamicEntities.remove(entity);
		removeEntity(entity);
		GameClient.getInstance().getRenderer().deregisterEntity(entity);
	}

	public void removeDynamicEntityByUid(String entityUid) {
		DynamicEntity entity = getDynamicEntityByUid(entityUid);
		removeDynamicEntity(entity);
	}

	private void removeEntity(MapEntity entity) {
		if (solidEntities.contains(entity)) {
			solidEntities.remove(entity);
		}
	}

}
