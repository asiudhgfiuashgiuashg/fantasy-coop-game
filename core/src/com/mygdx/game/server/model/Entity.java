package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * Something which has a position and an associated sprite name.
 * 
 * @author elimonent
 * @author Sawyer Harris
 * 
 */
public abstract class Entity extends PolygonObject implements Drawable {

	protected Entity(String uid, Vector2 position, String spriteName, int visLayer, CollideablePolygon polygon) {
		super(polygon);
		this.uid = uid;
		this.position = position;
		this.spriteName = spriteName;
		this.visLayer = visLayer;
	}

	/**
	 * unique identifier synchronized between client and server
	 */
	protected String uid;
	/**
	 * position in global coordinates
	 */
	protected Vector2 position;
	protected String spriteName;
	/**
	 * the layer that this Entity resides in (affects how it is drawn on client)
	 */
	protected int visLayer;
}
