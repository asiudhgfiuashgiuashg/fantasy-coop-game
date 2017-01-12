package com.mygdx.game.client.model;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
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
	static final float DEFAULT_MIN_RADIUS_MULTIPLIER = .9f;
	// lowest distance that the light will flicker to
	private float minDistance;
	// whether the light is currently shrinking or expanding
	private boolean shrinking;
	// the original distance specified in Tiled and the maximum distance this
	// light will expand to while flickering
	private float maxDistance;
	private float flickerDistMult;

	public FlickerPointLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y, float flickerRate, float flickerDistMult) {
		super(rayHandler, rays, color, distance, x, y);
		this.flickerRate = flickerRate;
		this.minDistance = distance * flickerDistMult;
		this.maxDistance = distance;
		shrinking = true;
		this.flickerDistMult = flickerDistMult;
	}

	@Override
	public void update() {
		super.update();
		// shrink or grow the light distance to help simulate flickering
		float distDiff = Gdx.graphics.getDeltaTime() * flickerRate *
				(maxDistance - minDistance);
		distDiff = shrinking ? -distDiff : distDiff;
		setDistance(distance + distDiff);
		if (shrinking && distance <= minDistance) {
			shrinking = false;
		} else if (!shrinking && distance >= maxDistance) {
			shrinking = true;
		}
	}

	public float getFlickerRate() {
		return flickerRate;
	}

	public float getFlickerDistMult() {
		return flickerDistMult;
	}
}
