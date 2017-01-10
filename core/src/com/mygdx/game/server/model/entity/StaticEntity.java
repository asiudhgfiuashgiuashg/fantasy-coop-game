package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A static entity on the server. A static entity's sprite is defined in Tiled
 * and loaded on the client, and it cannot be changed. The server's only
 * knowledge of the entity is its uid and hitbox, so it can tell the client that
 * a static entity has moved or disappeared.
 * 
 * @author Sawyer Harris
 *
 */
public class StaticEntity extends Entity {

	/**
	 * Constructs a static entity. Unlike dynamic entities, a polygonal hitbox
	 * is required.
	 * 
	 * @param uid
	 * @param position
	 * @param visLayer
	 * @param solid
	 * @param polygon
	 */
	public StaticEntity(String uid, Vector2 position, int visLayer, boolean solid, CollideablePolygon polygon) {
		super(uid, position, visLayer, solid);
		setPolygon(polygon);
	}

}
