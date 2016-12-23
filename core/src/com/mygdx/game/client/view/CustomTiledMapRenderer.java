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
import com.mygdx.game.client.model.entity.StaticEntity;

import static com.mygdx.game.client.model.GameClient.console;

/**
 * Extends libgdx's renderer to work with MapEntities
 */
public class CustomTiledMapRenderer extends
		OrthogonalTiledMapRenderer {

	/**
	 * renders:
	 * - tile layer
	 * - static entities
	 * - dynamic entities
	 * doesn't render:
	 * - gui
	 * - developer console
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
