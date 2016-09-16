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
	/**
	 * Name of entity's sprite
	 */
	protected String spriteName;
	/**
	 * the layer that this Entity resides in (affects how it is drawn on client)
	 */
	protected int visLayer;

	/**
	 * Gets the entity's position
	 * @return
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Sets the entity's position along with its hitbox's position
	 * 
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;

		// Bind hitbox position to entity position
		this.polygon.setPosition(position.x, position.y);
	}

	/**
	 * Gets the name of the entity's sprite
	 * 
	 * @return spriteName
	 */
	public String getSpriteName() {
		return spriteName;
	}

	/**
	 * Sets the name of the entity's sprite
	 * 
	 * @param spriteName
	 */
	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}

	/**
	 * Gets entity's current visibility layer
	 * 
	 * @return visLayer
	 */
	public int getVisLayer() {
		return visLayer;
	}

	/**
	 * Sets entity's visibility layer
	 * 
	 * @param visLayer
	 *            -1, 0, or 1
	 */
	public void setVisLayer(int visLayer) {
		this.visLayer = visLayer;
	}

	/**
	 * Gets entity's uid
	 * 
	 * @return uid
	 */
	public String getUid() {
		return uid;
	}

}
