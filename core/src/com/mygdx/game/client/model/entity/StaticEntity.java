package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.shared.util.CollideablePolygon;

import java.util.Iterator;

/**
 * A non-moving object visualized on the screen by a StaticTiledMapTile or an
 * AnimatedTiledMapTile (wrapped in a TiledMapTileMapObject). It may have a
 * polygon hitbox associated with it for collision.
 * The appearance and position of a StaticEntity is specified in Tiled.
 */
public class StaticEntity extends MapEntity {
	private CollideablePolygon hitbox; // used for collision checking
	private TiledMapTileMapObject tileMapObject; // used for drawing

	/**
	 *
	 * @param hitbox used for collision detection
	 * @param tileMapObject holds the tile which represents this entity
	 *                         visually. Used to obtain x and y position
	 *                         which was loaded in from tiled into
	 *                         tileMapObject.
	 */
	public StaticEntity(CollideablePolygon hitbox, TiledMapTileMapObject
			tileMapObject) {
		this.hitbox = hitbox;
		this.tileMapObject = tileMapObject;

		// the libgdx loader loaded the associated TiledMapTileMapObject from
		// the Static Entities layer for us, and in Tiled we specify the x
		// and y position of each static entity as properties, so we can
		// extract it into this static entity.
		float xPos = tileMapObject.getProperties().get("x", Float.class);
		float yPos = tileMapObject.getProperties().get("y", Float.class);
		this.position = new Vector2(xPos, yPos);
	}

	public Vector2 getPos() {
		return this.position;
	}
}
