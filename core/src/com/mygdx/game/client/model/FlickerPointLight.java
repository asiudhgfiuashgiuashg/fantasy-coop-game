package com.mygdx.game.client.model;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;

/**
 * A box2d pointlight that flickers at some rate by changing distance and color
 */
public class FlickerPointLight extends PointLight {
	/*
	 * how many times to flicker per second - zero to not flicker
	 */
	private float flickerRate;
	/*
	 * when flickering, the lowest distance of the light =
	 * MIN_RADIUS_MULTIPLIER * (distance specified in Tiled)
	 */
	private static final float MIN_RADIUS_MULTIPLIER = .8f;

	public FlickerPointLight(RayHandler rayHandler, int rays, Color color,
							 float distance, float x, float y, float
									 flickerRate) {
		super(rayHandler, rays, color, distance, x, y);
		this.flickerRate = flickerRate;
	}
	@Override
	public void update() {
		super.update();

	}
}
