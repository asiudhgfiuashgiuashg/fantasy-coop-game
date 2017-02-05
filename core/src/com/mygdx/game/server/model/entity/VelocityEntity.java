package com.mygdx.game.server.model.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.server.model.GameServer;

/**
 * A VelocityEntity is an entity with a mass and velocity-based movement. Forces
 * can be applied to it to accelerate the entity, and solid VelocityEntities
 * check for collisions with other solid objects to ensure no overlap occurs.
 * 
 * @author Sawyer Harris
 *
 */
public abstract class VelocityEntity extends DynamicEntity {
	/** Mass affects acceleration of forces via Newton's Law F = ma */
	private float mass;
	/** Velocity of entity in units of pixels/tick */
	private Vector2 velocity = new Vector2(0, 0);
	/** Sum of all forces acting on entity */
	private Vector2 netForce = new Vector2(0, 0);

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
	public void act(long elapsedTime) {
		// Time elapsed in units of ticks
		float dt = elapsedTime / GameServer.TICKRATE;


		// Update velocity based on forces (dv = 1/m F dt)
		Vector2 dv = netForce.scl(dt).scl(1 / mass);

		velocity.add(dv);

		netForce.set(0, 0);
		
		// Compute displacement (dx = v dt)
		Vector2 dx = getVelocity().scl(dt);
		if (isSolid()) {
			// Number of iterations to achieve sub pixel precision
			int N = MathUtils.ceil(MathUtils.log2(dx.len()));
			MathUtils.clamp(N, 1, N); // N >= 1

			// Collision lists
			ArrayList<PolygonObject> currList = new ArrayList<PolygonObject>();
			ArrayList<PolygonObject> prevList = new ArrayList<PolygonObject>();

			// Iteratively check for collisions
			for (int i = 0; i < N; i++) {
				// Update lists
				ArrayList<PolygonObject> temp = prevList;
				prevList = currList;
				temp.clear();
				currList = temp;

				// Starting position
				float startX = getPolygon().getX();
				float startY = getPolygon().getY();

				// Try moving in direction
				Vector2 pos = new Vector2(startX, startY);
				Vector2 addVec = new Vector2(dx);
				addVec.scl((float) Math.pow(2, -i));
				pos.add(addVec);
				//getPolygon().setPosition(pos.x, pos.y);
				setPosition(new Vector2(pos.x, pos.y));

				// Check for collisions with other solid objects
				for (PolygonObject solidObj : GameServer.getInstance().getMap().getSolidObjects()) {
					if (this.collides(solidObj)) {
						// Revert to starting position of this iteration
						//getPolygon().setPosition(startX, startY);
						setPosition(new Vector2(startX, startY));

						// Add to collision list
						currList.add(solidObj);
					}
				}

				// Last iteration
				if (i == N - 1) {
					// If last iteration had no collisions, use previous list
					if (currList.isEmpty()) {
						currList = prevList;
					}

					// Call onBumpInto() for each collision
					for (PolygonObject obj : currList) {
						// Call on this entity
						onBumpInto(obj);

						// Call on the other if it is a VelocityEntity
						if (obj instanceof VelocityEntity) {
							((VelocityEntity) obj).onBumpInto(this);
						}
					}
				}
			}
		} else {
			// Non-solid, no need to check
			Vector2 pos = getPosition();
			pos.add(dx);
			setPosition(pos);
		}
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
	 * Returns a copy of the entity's velocity vector
	 * 
	 * @return copy of velocity
	 */
	public Vector2 getVelocity() {
		return new Vector2(velocity);
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
