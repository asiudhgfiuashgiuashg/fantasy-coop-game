package com.mygdx.game.server.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.entity.DynamicEntity;

/**
 * Basic stuff that every player should have.
 */
public class Player extends DynamicEntity {

	public int connectionUid;

	protected Player(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}
}
