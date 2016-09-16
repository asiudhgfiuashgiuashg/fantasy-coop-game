package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * A static entity on the server. A static entity's sprite is defined in Tiled
 * and loaded on the client, and it cannot be changed. The server's only
 * knowledge of the entity is its uid and hitbox, so it can tell the client that a
 * static entity has moved or disappeared.
 * 
 * @author Sawyer Harris
 *
 */
public class StaticEntity extends Entity {

	public StaticEntity(String uid, Vector2 position, int visLayer, CollideablePolygon polygon) {
		super(uid, position, null, visLayer, polygon);
	}

	@Override
	public DrawMessage draw() {
		// TODO not sure how to handle this yet
		return null;
	}

}
