package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;

public abstract class VelocityEntity extends DynamicEntity {
	/** Mass affects acceleration of forces via Newton's Law F = ma */
	private float mass;
	/** Velocity of entity */
	private Vector2 velocity;
	/** Sum of all forces acting on entity */
	private Vector2 netForce;

	/**
	 * Constructs a VelocityEntity with a given mass
	 * 
	 * @param uid
	 * @param position
	 * @param visLayer
	 * @param solid
	 * @param mass
	 */
	public VelocityEntity(String uid, Vector2 position, int visLayer, boolean solid, float mass) {
		super(uid, position, visLayer, solid);
		this.mass = mass;
	}

	@Override
	public void act() {
		// TODO implement position update, collision checking
	}

	/**
	 * Applies a force to the entity, which will be resolved in the next act().
	 * The force is added to the netForce vector, so multiple forces may act on
	 * an entity.
	 * 
	 * @param force
	 */
	public void applyForce(Vector2 force) {
		netForce.add(force);
	}

	/**
	 * Returns the entity's mass
	 * 
	 * @return mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Sets the entity's mass
	 * 
	 * @param mass
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * Returns the entity's velocity vector
	 * 
	 * @return velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * Sets the entity's velocity vector. Should only be used when applying a
	 * force is not appropriate.
	 * 
	 * @param velocity
	 */
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns the net force vector acting on the entity.
	 * 
	 * @return netForce
	 */
	public Vector2 getNetForce() {
		return netForce;
	}

	/**
	 * Sets the net force vector. Should only be used when previous forces
	 * applied to entity should be ignored!
	 * 
	 * @param netForce
	 */
	public void setNetForce(Vector2 netForce) {
		this.netForce = netForce;
	}

	/**
	 * Called when two solid objects collide with each other.
	 * 
	 * @param other
	 *            other polygon object
	 */
	public abstract void onBumpInto(PolygonObject other);

}
