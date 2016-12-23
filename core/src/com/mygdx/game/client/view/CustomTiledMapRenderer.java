package com.mygdx.game.client.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Extends libgdx's renderer to work with MapEntities
 */
public class CustomTiledMapRenderer extends
		OrthogonalTiledMapRenderer {
	public CustomTiledMapRenderer(TiledMap map) {
		super(map);
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
