package com.mygdx.game.server.model.entity.player.Ranger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.friendly.Friendly;
import com.mygdx.game.shared.model.CollideablePolygon;
import com.mygdx.game.shared.model.EntityLight;


import java.util.Random;

/**
 * sits in a tree and looks around and moves between left and right positions
 * Could be modified to have more complicated behavior
 */
public class RangerArrow extends Friendly {

	private Random rand = new Random();

	private float[] hitbox = {12, 1, 2, 1, 2, 6, 12, 6};

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
		setRotation(180f + (float) Math.toDegrees(angle));
		setVelocity(new Vector2(xVel, yVel));
		setOrigin(7, 7);

		setVerticesNoUpdate(hitbox);

		//TODO rotate lights with entity clientside
/*		Color color = Color.GOLDENROD;
		color.a = 0.34f;
		EntityLight light = new EntityLight(50, color, 10, 0, 7f, 0, 1, 1);
		lights.add(light);*/
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
