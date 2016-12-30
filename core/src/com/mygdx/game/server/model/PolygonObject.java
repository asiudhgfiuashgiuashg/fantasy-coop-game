package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * An object which has a collideablepolygon as one of its fields.
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public abstract class PolygonObject {
	protected CollideablePolygon polygon;

	protected PolygonObject(CollideablePolygon polygon) {
		this.polygon = polygon;
	}

	protected boolean collides(PolygonObject other) {
		return other.polygon.collides(this.polygon, new Intersector.MinimumTranslationVector());
	}
}
