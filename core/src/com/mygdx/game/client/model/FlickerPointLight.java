package com.mygdx.game.client.model;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * A box2d pointlight that flickers at some rate by changing distance and color
 */
public class FlickerPointLight extends PointLight {

	// how many times to flicker per second - zero to not flicker
	private float flickerRate;
	// default for flickerDistMult
	static final float DEFAULT_MIN_RADIUS_MULTIPLIER = .9f;
	// default for flickerAlphaMult
	static final float DEFAULT_MIN_ALPHA_MULTIPLIER = .8f;
	// lowest distance that the light will flicker to
	private float minDistance;
	// whether the light is currently shrinking or expanding
	private boolean shrinking;
	// the original distance specified in Tiled and the maximum distance this
	// light will expand to while flickering
	private float maxDistance;
	/*
	 * when flickering, the lowest distance of the light =
	 * flickerDistMult * (distance specified in Tiled)
	 */
	private float flickerDistMult;
	private float flickerAlphaMult;
	/* the original light color alpha specified in Tiled as the last 8 bits
	 * (2 characters) of the hex color code. The most alpha the light will
	 * have when flickering.
	 */
	private float maxAlpha;
	// the lowest the alpha will go when flickering (alpha decreases as the
	// light radius is contracting)
	private float minAlpha;
	private static final Random rand = new Random();

	public FlickerPointLight(RayHandler rayHandler, int rays, Color color,
							 float distance, float x, float y, float
									 flickerRate, float flickerDistMult, float
									 flickerAlphaMult) {
		super(rayHandler, rays, color, distance, x, y);
		this.flickerRate = flickerRate;
		this.minDistance = distance * flickerDistMult;
		this.maxDistance = distance;
		shrinking = true;
		this.flickerDistMult = flickerDistMult;
		this.flickerAlphaMult = flickerAlphaMult;
		this.maxAlpha = color.a;
		this.minAlpha = flickerAlphaMult * maxAlpha;
		setStaticLight(true); // optimization
		setXray(true); // optimization
		chooseRandomStartingFlicker();
	}

	/**
	 * choose a random distance and alpha so that light flickering isn't
	 * synced between lights
	 */
	private void chooseRandomStartingFlicker() {
		float multiplier = rand.nextFloat();
		distance = maxDistance - (multiplier * (maxDistance - minDistance));
		float alpha = maxAlpha - (multiplier * (maxAlpha - minAlpha));
		setColor(color.r, color.g, color.b, alpha);
	}


	/**
	 * perform flickering in addition to the usual updates
	 * flickering involves changing the distance and alpha of the light over
	 * time
	 */
	@Override
	public void update() {
		super.update();
		// shrink or grow the light distance to help simulate flickering
		float distDiff = Gdx.graphics.getDeltaTime() * flickerRate *
				(maxDistance - minDistance);
		distDiff = shrinking ? -distDiff : distDiff;
		setDistance(distance + distDiff);

		float alphaDiff = Gdx.graphics.getDeltaTime() * flickerRate *
				(maxAlpha - minAlpha);
		alphaDiff = shrinking ? -alphaDiff : alphaDiff;
		setColor(new Color(color.r, color.g, color.b, color.a + alphaDiff));

		if (shrinking && (distance <= minDistance || color.a <= minAlpha)) {
			shrinking = false;
			//distance and alpha can become out of sync at low fps for some
			// reason, so sync them up here.
			distance = minDistance;
			setColor(new Color(color.r, color.g, color.b , minAlpha));
		} else if (!shrinking && (distance >= maxDistance || color.a >=
				maxAlpha)) {
			shrinking = true;
			//distance and alpha can become out of sync at low fps for some
			// reason, so sync them up here.
			distance = maxDistance;
			setColor(new Color(color.r, color.g, color.b , maxAlpha));
		}
	}

	public float getFlickerRate() {
		return flickerRate;
	}

	public float getFlickerDistMult() {
		return flickerDistMult;
	}

	public float getFlickerAlphaMult() {
		return flickerAlphaMult;
	}
}
