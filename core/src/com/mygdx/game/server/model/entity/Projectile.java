package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class Projectile extends ActingEntity {
	
	protected Projectile(String uid, Vector2 position, int visLayer) {
		super(uid, position, visLayer);
	}

	/**
	 * Specify here the behavior for when this projectile hits things.
	 * @param entity
	 */
	protected abstract void onCollision(Entity entity);
}
