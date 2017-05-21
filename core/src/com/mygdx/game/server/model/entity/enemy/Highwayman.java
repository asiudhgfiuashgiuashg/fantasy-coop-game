package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * AI for highwayman (from prototype plot in google drive)
 * Far from complete right now.
 */
public class Highwayman extends Enemy {

	private float[] hitbox = {13, -3, 3, -3, 3, 5, 13, 5};
	private final static float MASS = 1f; // affects movement

	protected Highwayman(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVerticesNoUpdate(hitbox);
		setMass(MASS);
	}

	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
