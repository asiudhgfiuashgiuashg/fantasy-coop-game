package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;

/**
 * AI for highwayman (from prototype plot in google drive)
 * Far from complete right now.
 */
public class Highwayman extends Enemy {

	private float[] hitbox = {20, 0, 0, 0, 10, 20};
	private final static float MASS = 1000f; // affects movement

	protected Highwayman(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVertices(hitbox);
		setMass(MASS);
	}

	@Override
	public void act(long elapsedTime) {

	}

	@Override
	public void onBumpInto(PolygonObject other) {

	}
}
