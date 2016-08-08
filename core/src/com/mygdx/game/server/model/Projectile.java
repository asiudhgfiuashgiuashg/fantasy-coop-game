package com.mygdx.game.server.model;


public abstract class Projectile extends ActingEntity {
	
	/**
	 * Specify here the behavior for when this projectile hits things.
	 * @param entity
	 */
	protected abstract void onCollision(Entity entity);
}
