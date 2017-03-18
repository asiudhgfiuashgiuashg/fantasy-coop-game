package com.mygdx.game.server.model.entity.friendly;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.server.model.entity.enemy.Enemy;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.Random;

/**
 * AI for highwayman (from prototype plot in google drive)
 * Far from complete right now.
 */
public class OldMan extends Friendly {

	private float[] hitbox = {13, -3, 3, -3, 3, 5, 13, 5};
	private final static float MASS = 1000f; // affects movement

	private boolean setInitialAnimation = false;

	private long timeSinceAnimationChange = 0;
	private long directionDuration = 0; // how long to look in a direction

	private Random rand = new Random();

	public OldMan(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVertices(hitbox);
		setMass(MASS);
	}

	/**
	 * look in a random direction every once in a while
	 *
	 * @param elapsedTime
	 */
	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
		if (!setInitialAnimation) { // send the initial animation of the old man
			this.animationName = "down_facing";
			sendAnimation();
			setInitialAnimation = true;
			directionDuration = pickDirectionDuration();
		}
		timeSinceAnimationChange += elapsedTime;
		if (timeSinceAnimationChange >= directionDuration) {
			this.animationName = chooseRandomDirectionAnimation();
			sendAnimation();
			timeSinceAnimationChange = 0;
			directionDuration = pickDirectionDuration();
		}
	}

	/**
	 * choose a random direction animation for the old man to look in (1 of 4 directions possible)
	 *
	 * @return
	 */
	private String chooseRandomDirectionAnimation() {
		int max = 3;
		int min = 0;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		if (0 == randomNum) {
			return "down_facing";
		} else if (1 == randomNum) {
			return "up_facing";
		} else if (2 == randomNum) {
			return "right_facing";
		} else {
			return "left_facing";
		}
	}

	/**
	 * choose a random amount of time to look in the next direction (milliseconds)
	 *
	 * @return
	 */
	private long pickDirectionDuration() {
		long minTime = 3000;
		long maxTime = 10000;

		return (long) (rand.nextDouble() * (maxTime - minTime) + minTime);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
