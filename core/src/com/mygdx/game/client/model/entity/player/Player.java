package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.entity.DynamicEntity;

/**
 * Represents a local player
 * Basic stuff that every player should have.
 */
public abstract class Player extends DynamicEntity {

	private static final float movementForce = 1000; // how much force to apply when moving
	private Vector2 upMovementForce = new Vector2(0, movementForce);
	private Vector2 downMovementForce = new Vector2(0, -movementForce);
	private Vector2 leftMovementForce = new Vector2(-movementForce, 0);
	private Vector2 rightMovementForce = new Vector2(movementForce, 0);

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setMass(1);
	}

	public void moveUp() {
		applyForce(upMovementForce);
	}

	public void moveDown() {
		applyForce(downMovementForce);
	}

	public void moveLeft() {
		applyForce(leftMovementForce);
	}

	public void moveRight() {
		applyForce(rightMovementForce);

	}
}
