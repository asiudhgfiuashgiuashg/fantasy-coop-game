package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.entity.DamageableEntity;

/**
 * An AI who can damage the player.
 *
 */
public abstract class Enemy extends DamageableEntity {


	protected Enemy(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		hasHealth = true; // tell the client to display a health bar
	}

}
