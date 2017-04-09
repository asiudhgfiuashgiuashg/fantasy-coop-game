package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.entity.DynamicEntity;

/**
 * Represents a local player
 * Basic stuff that every player should have.
 */
public abstract class Player extends DynamicEntity {

	private static final float movementForce = 1000; // how much force to apply when moving
	private float speed = 20;
	public boolean up = false;
	public boolean down = false;
	public boolean right = false;
	public boolean left = false;

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setMass(1);
	}
	
	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);
		if (up && !down) {
			setVelocity(new Vector2(getVelocity().x, speed));
		} else if (!up && down) {
			setVelocity(new Vector2(getVelocity().x, -speed));
		} else {
			setVelocity(new Vector2(getVelocity().x, 0));
		}
		if (right && !left) {
			setVelocity(new Vector2(speed, getVelocity().y));
		} else if (!right && left) {
			setVelocity(new Vector2(-speed, getVelocity().y));
		} else {
			setVelocity(new Vector2(0, getVelocity().y));
		}
	}
}
