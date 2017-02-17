package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * Represents a map boundary. The map's boundaries consist of many Boundary
 * instances which wrap a Polygon object used for collision. Boundaries are
 * always solid PolygonObjects.
 * 
 * @author Sawyer Harris
 *
 */
public class Boundary extends PolygonObject {

	/**
	 * Constructs a solid boundary using the given polygon
	 * 
	 * @param polygon the hitbox of the boundary
	 * @param position where to place the hitbox (in world coordinates)
	 */
	public Boundary(float[] vertices, Vector2 position) {
		super(vertices, true, position);
	}

}
