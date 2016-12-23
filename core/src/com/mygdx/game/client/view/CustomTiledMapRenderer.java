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
	private static final int DEFAULT_VISLAYER = 0;

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
		//map = (ClientTiledMap) super.map;
	}

	public CustomTiledMapRenderer(TiledMap map, Batch batch) {
		super(map, batch);
	}

	public CustomTiledMapRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
	}

	public CustomTiledMapRenderer(TiledMap map, float unitScale,
								  Batch batch) {
		super(map, unitScale, batch);
	}
}
