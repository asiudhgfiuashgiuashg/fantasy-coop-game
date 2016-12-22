package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * A non-moving object visualized on the screen by a StaticTiledMapTile or an
 * AnimatedTiledMapTile (wrapped in a TiledMapTileMapObject). It may have a
 * polygon hitbox associated with it for collision.
 * The appearance and position of a StaticEntity is specified in Tiled.
 */
public class StaticEntity extends MapEntity {
	private CollideablePolygon hitbox;
	private TiledMapTileMapObject tileMapObject;
}
