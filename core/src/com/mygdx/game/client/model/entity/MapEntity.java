package com.mygdx.game.client.model.entity;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.List;

/**
 * represents an entity on the map.
 * An entity has position and some visual component
 */
public abstract class MapEntity extends CollideablePolygon {
	protected String uid;
	protected int visLayer; // affects whether the entity is drawn above or

	public List<PointLight> box2dLights;

	/**
	 * according to design doc vislayer shouldn't be modifiable, so there is
	 * a getter for visLayer but no setter. VisLayer can be set once using
	 * the constructor
	 *
	 * @return
	 */
	public int getVisLayer() {
		return visLayer;
	}

	public Vector2 getPos() {
		return new Vector2(getX(), getY());
	}

	/**
	 * give the renderer something to draw
	 *
	 * @return
	 */
	public abstract TextureRegion getTextureRegion();

	/**
	 * a value used for rendering order in layer zero
	 *
	 * @return
	 */
	public float getCutOffY() {
		return getVertices() == null || getVertices().length == 0 ? getPos().y : getTransformedCutoffY();
	}

	public String getUid() {
		return uid;
	}
}
