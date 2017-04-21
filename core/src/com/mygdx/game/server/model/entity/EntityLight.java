package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents a light on a dynamic entity.
 * When the client receives this it translates it to a PointLight on a client DynamicEntity.
 */
public class EntityLight {

	public final int rays;
	public final Color color;
	public final float distance;

	// x and y are in relation to the origin of the parent dynamic entity.
	public final float x;
	public final float y;

	/**
	 * Creates light shaped as a circle with given radius
	 * @param rays
	 *            number of rays - more rays make light to look more realistic
	 *            but will decrease performance, can't be less than MIN_RAYS
	 * @param color
	 *            color, set to {@code null} to use the default color
	 * @param distance
	 *            distance of light
	 * @param x
	 *            horizontal position in world coordinates
	 * @param y
	 *            vertical position in world coordinates
	 */
	public EntityLight(int rays, Color color,
					  float distance, float x, float y) {
		this.rays = rays;
		this.color = color;
		this.distance = distance;
		this.x = x;
		this.y = y;
	}
}
