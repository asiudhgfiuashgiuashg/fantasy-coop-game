package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * An example/test-bed enemy
 * Current capabilities:
 * - alternates between a 4-frame animation and a single-frame animation
 * every four seconds
 */
public class TestEnemy extends Enemy {

	boolean sentAnimationName = false;
	long timeSinceAnimationChange = 0;
	private float[] hitbox = {20, 0, 0, 0, 10, 20};
	private boolean movedUp = false;

	protected TestEnemy(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVertices(hitbox);
		setMass(1000f);
		frameDuration = 1f;
	}

	/**
	 * act out one game tick (animation changes, velocity changes etc)
	 *
	 * @param elapsedTime - how much time has passed since last frame
	 *                    (unit is milliseconds)
	 */
	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
		if (!movedUp) {
			//applyForce(new Vector2(0, 0.0015f));
			applyForce(new Vector2(0.0015f, 0));
			movedUp = true;
		}

		if (!sentAnimationName) { // only send this test animation name one
			// time so that client doesn't restart the animation over and over
			if (this.animationName == null || this.animationName.equals("blueAnimation")) {
				this.animationName = "1234Animation";
			} else {
				this.animationName = "blueAnimation";
			}

			sendAnimation();
			sentAnimationName = true;
		}
		if (timeSinceAnimationChange > 4 * 1000) {
			sentAnimationName = false; // use the other animation
			timeSinceAnimationChange = 0;
		}

		timeSinceAnimationChange += elapsedTime;
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {
		// NOTHIN!
	}

}
