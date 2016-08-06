package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Something which has a position and an associated sprite name.
 * @author elimonent
 * 
 */
public abstract class Entity implements Drawable {
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
