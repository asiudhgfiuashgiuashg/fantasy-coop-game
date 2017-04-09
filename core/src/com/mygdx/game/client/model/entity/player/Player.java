package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.entity.DynamicEntity;

/**
 * Represents a local player
 * Basic stuff that every player should have.
 */
public abstract class Player extends DynamicEntity {

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
	}
}
