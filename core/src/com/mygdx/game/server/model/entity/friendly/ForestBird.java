package com.mygdx.game.server.model.entity.friendly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.server.model.entity.enemy.Enemy;
import com.mygdx.game.shared.model.CollideablePolygon;
import com.mygdx.game.shared.model.EntityLight;

import java.util.Random;

/**
 * sits in a tree and looks around and moves between left and right positions
 * Could be modified to have more complicated behavior
 */
public class ForestBird extends Friendly {


	private boolean setInitialAnimation = false;

	private long timeSinceAnimationChange = 0;
	private long directionDuration = 0; // how long to look in a direction

	Vector2 oldPos;

	private enum BirdState {
		LEFT_FACING_ON_LEFT,
		RIGHT_FACING_ON_LEFT,
		DISAPPEARED_FROM_LEFT,
		RIGHT_FACING_ON_RIGHT,
		LEFT_FACING_ON_RIGHT,
		DISAPPEARED_FROM_RIGHT,
	}

	private BirdState state = BirdState.LEFT_FACING_ON_LEFT;

	private Random rand = new Random();

	public ForestBird(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		oldPos = new Vector2(getPosition());
	}

	/**
	 * look in different direction every once in a while
	 *
	 * TODO: make bird disappear, change location, and reappear
	 *
	 * @param elapsedTime
	 */
	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
		if (!setInitialAnimation) { // send the initial animation of the old man
			this.animationName = "left_facing";
			sendAnimation(1f);
			setInitialAnimation = true;
			directionDuration = pickDirectionDuration();
		}
		timeSinceAnimationChange += elapsedTime;
		if (timeSinceAnimationChange >= directionDuration) {
			if (BirdState.LEFT_FACING_ON_LEFT == state) {
				animationName = "right_facing";
				state = BirdState.RIGHT_FACING_ON_LEFT;
			} else if (BirdState.RIGHT_FACING_ON_LEFT == state) {
				animationName = "invisible";
				state = BirdState.DISAPPEARED_FROM_LEFT;
				setPosition(new Vector2(getPosition()).add(5, 5));
			} else if (BirdState.DISAPPEARED_FROM_LEFT == state) {
				animationName = "right_facing";
				state = BirdState.RIGHT_FACING_ON_RIGHT;
			} else if (BirdState.RIGHT_FACING_ON_RIGHT == state) {
				animationName = "left_facing";
				state = BirdState.LEFT_FACING_ON_RIGHT;
			} else if (BirdState.LEFT_FACING_ON_RIGHT == state) {
				animationName = "invisible";
				state = BirdState.DISAPPEARED_FROM_RIGHT;
				setPosition(oldPos);
			} else if (BirdState.DISAPPEARED_FROM_RIGHT == state) {
				animationName = "left_facing";
				state = BirdState.LEFT_FACING_ON_LEFT;
			}

			sendAnimation(1f);

			timeSinceAnimationChange = 0;
			directionDuration = pickDirectionDuration();
		}
	}

	/**
	 * choose a random amount of time to look in the next direction (milliseconds)
	 *
	 * @return
	 */
	private long pickDirectionDuration() {
		long minTime = 1000;
		long maxTime = 3000;

		return (long) (rand.nextDouble() * (maxTime - minTime) + minTime);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
