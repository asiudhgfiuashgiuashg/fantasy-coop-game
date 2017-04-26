package com.mygdx.game.shared.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents a light on a dynamic entity.
 * When the client receives this it translates it to a PointLight on a client DynamicEntity.
 */
public class EntityLight {

	public float flickerDistMult;
	public float flickerAlphaMult;
	public int rays;
	public Color color;
	public float distance;

	// x and y are in relation to the origin of the parent dynamic entity.
	public float x;
	public float y;
	public float flickerRate;

	/**
	 * Creates light shaped as a circle with given radius
	 *
	 * @param rays     number of rays - more rays make light to look more realistic
	 *                 but will decrease performance, can't be less than MIN_RAYS
	 * @param color    color, set to {@code null} to use the default color
	 * @param distance distance of light
	 * @param x        horizontal position in world coordinates
	 * @param y        vertical position in world coordinates
	 * @param flickerRate how many times per second the light shhoudl flicker
	 *                    @param flickerDistMult a multiplier < 1 to determine how much the light should shrink when flickering
	 *                                           @param flickerAlphaMult a multiplier to determine how much the light's alpha should decrease as it shrinks
	 *
	 *                                                                   See FlickerPointLight.java (clientside version of lighhts for rendering) for more info on these vars
	 */
	public EntityLight(int rays, Color color, float distance, float x, float y, float flickerRate, float flickerDistMult, float flickerAlphaMult) {
		this.rays = rays;
		this.color = color;
		this.distance = distance;
		this.x = x;
		this.y = y;
		this.flickerRate = flickerRate;
		this.flickerDistMult = flickerDistMult;
		this.flickerAlphaMult = flickerAlphaMult;
	}

	/**
	 * no-arg constructor for serialization
	 */
	public EntityLight() {
		this(100, Color.CLEAR, 100, 0, 0, 0, 1, 1);
	}
}
