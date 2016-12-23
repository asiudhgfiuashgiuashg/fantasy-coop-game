package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.math.Vector2;

/**
 * represents an entity on the map.
 * An entity has position and some visual component
 */
public abstract class MapEntity {
	protected String uid;
	protected Vector2 position; // map position
	protected int visLayer; // affects whether the entity is drawn above or
	// below other entities
}
