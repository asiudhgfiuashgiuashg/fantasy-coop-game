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
	public RangerArrow(String uid, Vector2 position, int visLayer, boolean solid, Vector2 destination) {
		super(uid, position, visLayer, solid);
		setVelocity(new Vector2(20, 0));
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
