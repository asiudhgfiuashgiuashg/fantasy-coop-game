package com.mygdx.game.server.model.entity.player.Ranger;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.friendly.Friendly;
import com.mygdx.game.shared.model.CollideablePolygon;


import java.util.Random;

/**
 * sits in a tree and looks around and moves between left and right positions
 * Could be modified to have more complicated behavior
 */
public class RangerArrow extends Friendly {

	private Random rand = new Random();

	/**
	 *
	 * @param uid
	 * @param position
	 * @param visLayer
	 * @param solid
	 * @param destination the position to which the arrow is travelling
	 */
	public RangerArrow(String uid, Vector2 position, int visLayer, boolean solid, Vector2 destination, Vector2 source) {
		super(uid, position, visLayer, solid);
		float arrowSpeed = 150;
		float angle = (float) Math.atan2((destination.y - source.y), (destination.x - source.x));
		float yVel = (float) Math.sin(angle) * arrowSpeed;
		float xVel = (float) Math.cos(angle) * arrowSpeed;

		setVelocity(new Vector2(xVel, yVel));
	}

	/*
	 *
	 * @param elapsedTime
	 */
	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);

	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
