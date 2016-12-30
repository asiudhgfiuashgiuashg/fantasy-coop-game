package com.mygdx.game.server.model;

import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * Represents a map boundary. The map's boundaries consist of many Boundary
 * instances which wrap a Polygon object used for collision.
 * 
 * @author Sawyer Harris
 *
 */
public class Boundary extends PolygonObject {

	/**
	 * Constructs a boundary using the given polygon
	 * 
	 * @param p
	 *            polygon
	 */
	public Boundary(CollideablePolygon p) {
		super(p);
	}

}
