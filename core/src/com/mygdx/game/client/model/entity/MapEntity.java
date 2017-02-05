package com.mygdx.game.client.model.entity;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.ClientTiledMap;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.List;

/**
 * represents an entity on the map.
 * An entity has position and some visual component
 */
public abstract class MapEntity {
	protected String uid;
	protected Vector2 position; // map position
	protected int visLayer; // affects whether the entity is drawn above or
	public CollideablePolygon hitbox; // specified in Tiled for static
	// entities. Specified in java code for dynamic entities.

	public List<PointLight> box2dLights;

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

	/**
	 * a value used for rendering order in layer zero
	 * @return
	 */
	public float getCutOffY() {
		return hitbox == null ? getPos().y : hitbox.getTransformedCutoffY();
	}

	public String getUid() {
		return uid;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		//hitbox position should be the same as entity position
		hitbox.setPosition(position.x, position.y);
	}
}
