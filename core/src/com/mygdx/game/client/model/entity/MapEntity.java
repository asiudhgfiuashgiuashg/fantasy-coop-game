package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * represents an entity on the map.
 * An entity has position and some visual component
 */
public abstract class MapEntity {
	protected String uid;
	protected Vector2 position; // map position
	protected int visLayer; // affects whether the entity is drawn above or
	protected CollideablePolygon hitbox; // specified in Tiled for static
	// entities. Specified in java code for dynamic entities.

	/**
	 * according to design doc vislayer shouldn't be modifiable, so there is
	 * a getter for visLayer but no setter. VisLayer can be set once using
	 * the constructor
	 * @return
	 */
	public int getVisLayer() {
		return visLayer;
	}

	public Vector2 getPos() {
		return this.position;
	}

	/**
	 * give the renderer something to draw
	 * @return
	 */
	public abstract TextureRegion getTextureRegion();

	public CollideablePolygon getHitbox() {
		return hitbox;
	}

	/**
	 * a value used for rendering order in layer zero
	 * @return
	 */
	public float getCutOffY() {
		return hitbox == null ? getPos().y : hitbox.getTransformedCutoffY();
	}

}
