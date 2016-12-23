package com.mygdx.game.client.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.client.model.ClientTiledMap;
import com.mygdx.game.client.model.entity.MapEntity;
import com.mygdx.game.client.model.entity.StaticEntity;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.client.model.GameClient.console;

/**
 * Extends libgdx's renderer to work with MapEntities and respect visLayers
 *
 * visLayers: TODO implement
 *     -1 = always render below other entities
 *     0 = render according to y-position derived from hitbox and location --
 *     DEFAULT VISLAYER
 *     1 = always render above other entities
 *
 *     Rendering order: Tile Layer -> visLayer -1 entities -> visLayer 0
 *     entities -> visLayer 1 entities
 */
public class CustomTiledMapRenderer extends
	OrthogonalTiledMapRenderer {
	public static final int DEFAULT_VISLAYER = 0;
	private final List<MapEntity> layerNegOneEntities = new
			ArrayList<MapEntity>();
	private final List<MapEntity> layerZeroEntities = new
			ArrayList<MapEntity>();
	private final List<MapEntity> layerOneEntities = new ArrayList<MapEntity>();

	/**
	 * renders:
	 * - tile layer
	 * - static entities
	 * - dynamic entities
	 * doesn't render:
	 * - gui
	 * - developer console
	 *  TODO respect vislayers when rendering
	 */
	@Override
	public void render() {
		beginRender();
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers()
				.get("Tile Layer 1");
		renderTileLayer(tileLayer);
		renderStaticEntities();
		// TODO render dynamic entities
		endRender();
	}

	private void renderStaticEntities() {
		for (StaticEntity staticEntity: ((ClientTiledMap) map).staticEntities) {
			TiledMapTileMapObject tileMapObject = staticEntity.tileMapObject;
			TextureRegion toDraw = tileMapObject.getTile().getTextureRegion();

			batch.draw(toDraw, staticEntity.getPos().x * unitScale, staticEntity
					.getPos().y * unitScale, toDraw.getRegionWidth() *
					unitScale, toDraw.getRegionHeight() * unitScale);
		}

	}

	public CustomTiledMapRenderer(ClientTiledMap map) {
		super(map);
		populateEntitiesLists();
	}

	public CustomTiledMapRenderer(TiledMap map, Batch batch) {
		super(map, batch);
		populateEntitiesLists();
	}

	public CustomTiledMapRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
		populateEntitiesLists();
	}

	public CustomTiledMapRenderer(TiledMap map, float unitScale,
								  Batch batch) {
		super(map, unitScale, batch);
		populateEntitiesLists();
	}

	/**
	 * used by constructors to initially populate lists of entities in each
	 * visLayer for easier rendering.
	 *
	 * Initially there will only be static entities, so don't worry about
	 * dynamic entities. These will be inserted into the lists when
	 * communication with the server starts a moment after the map loads.
	 */
	private void populateEntitiesLists() {
		for (MapEntity entity: ((ClientTiledMap) map).staticEntities) {
			int visLayer = entity.getVisLayer();
		}
	}


}
