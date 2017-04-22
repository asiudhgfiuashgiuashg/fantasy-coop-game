package com.mygdx.game.client.model.entity;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.FlickerPointLight;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.shared.model.EntityLight;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * represents an entity on the map.
 * An entity has position and some visual component
 */
public abstract class MapEntity extends CollideablePolygon {
	protected String uid;
	private int visLayer; // affects whether the entity is drawn above or
	public List<PointLight> box2dLights;

	public MapEntity(int visLayer) {
		this();
		this.visLayer = visLayer;
	}

	public MapEntity() {
		box2dLights = new ArrayList<PointLight>();
	}


	protected void setVisLayer(int visLayer) {
		this.visLayer = visLayer;
	}

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

	/**
	 * Convert EntityLights from a network message to box2dlights
	 * @param lights
	 */
	public void setLights(List<EntityLight> lights) {
		RayHandler rayHandler = GameClient.getInstance().getRayHandler();
		for (EntityLight light: lights) {
			// lights for dynamic entities are specified relative to the entity's origin
			FlickerPointLight box2dLight = new FlickerPointLight(rayHandler, light.rays, light.color, light.distance, getX(), light.x, getY(), light.y, light.flickerRate, light.flickerDistMult, light.flickerAlphaMult);
			this.box2dLights.add(box2dLight);
		}
	}

	/**
	 * Extend setPosition() to update global light positions
	 * @param position
	 */
	@Override
	public void setPosition(Vector2 position) {
		super.setPosition(position);
		for (PointLight light: box2dLights) {
			light.setPosition(position);
		}
	}
}
