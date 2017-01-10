package com.mygdx.game.server.model;

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
	 * @param polygon
	 */
	public Boundary(CollideablePolygon polygon) {
		super(polygon, true);
	}

}
