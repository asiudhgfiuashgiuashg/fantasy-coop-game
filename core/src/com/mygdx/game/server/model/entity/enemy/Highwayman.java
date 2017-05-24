package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * AI for highwayman (from prototype plot in google drive)
 * Far from complete right now.
 */
public class Highwayman extends Enemy {

	private float[] hitbox = {16, 8, 16, 17, 36, 16, 36, 9};
	private final static float MASS = 1f; // affects movement
	private boolean dying; // whether the death sequence has begun (happens when health falls below zero)
	private long dyingStartTime; // when the death sequence was begun

	protected Highwayman(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVerticesNoUpdate(hitbox);
		animationName = "up_facing_swing";
		frameDuration = .1f;
		animationPlayMode = Animation.PlayMode.NORMAL;
		setMass(MASS);
	}

	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);

		if (getHealth() <= 0 && !dying) {
			dying = true;
			dyingStartTime = TimeUtils.millis();
			animationName = "death";
			frameDuration = .1f;
			sendAnimation();
		}
		if (dying) {

		}
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
