package com.mygdx.game.server.model;

import com.mygdx.game.util.CollideablePolygon;

/**
 * An object which has a collideablepolygon as one of its fields.
 * @author elimonent
 *
 */
public abstract class PolygonObject {
	protected CollideablePolygon polygon;
	
	PolygonObject(CollideablePolygon polygon) {
		this.polygon = polygon;
	}
	
	protected boolean collides(PolygonObject other) {
		return other.polygon.collides(this.polygon);
	}
}
