package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.shared.util.CollideablePolygon;

import java.util.Iterator;

import static com.mygdx.game.client.model.GameClient.console;
import static com.mygdx.game.client.view.CustomTiledMapRenderer
		.DEFAULT_VISLAYER;

/**
 * A non-moving object visualized on the screen by a StaticTiledMapTile or an
 * AnimatedTiledMapTile (wrapped in a TiledMapTileMapObject). It may have a
 * polygon hitbox associated with it for collision.
 * The appearance and position of a StaticEntity is specified in Tiled.
 */
public class StaticEntity extends MapEntity {
	private CollideablePolygon hitbox; // used for collision checking
	public TiledMapTileMapObject tileMapObject; // used for drawing

	/**
	 *
	 * @param hitbox used for collision detection
	 * @param tileMapObject holds the tile which represents this entity
	 *                         visually. Used to obtain x and y position
	 *                         which was loaded in from tiled into
	 *                         tileMapObject.
	 * @param mapHeight needed to flip Tiled y axis (points down) to match
	 *                     libgdx y axis (points up)
	 */
	public StaticEntity(CollideablePolygon hitbox, TiledMapTileMapObject
			tileMapObject, float mapHeight) {
		this.hitbox = hitbox;
		this.tileMapObject = tileMapObject;
		String visLayerStr = (String) (tileMapObject.getProperties()
				.get("visLayer"));
		this.visLayer = null == visLayerStr ? DEFAULT_VISLAYER : Integer.valueOf
				(visLayerStr); // get the vislayer that was loaded for us by
		// libgdx

		// the libgdx loader loaded the associated TiledMapTileMapObject from
		// the Static Entities layer for us, and in Tiled we specify the x
		// and y position of each static entity as properties, so we can
		// extract it into this static entity.
		float xPos = tileMapObject.getProperties().get("x", Float.class);
		// flip y axis
		float yPos = mapHeight + tileMapObject.getProperties()
				.get("y", Float.class);
		//for some reason yPos needs negated or the y values are flipped..
		this.position = new Vector2(xPos, yPos);
	}


	/**
	 * Used by CustomTiledMapRenderer
	 *
	 * @return the textureregion that the renderer should draw do NOT return
	 * tileMapObject.getTextureRegion() - this won't work if the tile is
	 * animated
	 */
	@Override
	public TextureRegion getTextureRegion() {
		return tileMapObject.getTile().getTextureRegion();
	}
}
