package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * An object which has a CollideablePolygon as one of its fields. A
 * PolygonObject is the base class for many server entities and objects. Solid
 * PolygonObjects are not allowed to have their hitboxes overlap and have
 * special collision checking ( see VelocityEntity ).
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public abstract class PolygonObject {
	/** The polygon */
	private CollideablePolygon polygon;

	/** If object is solid i.e. cannot overlap with another */
	private boolean solid;

	/**
	 * Constructs a PolygonObject and adds it to GameMap's list of solidObjects
	 * if it is solid.
	 * 
	 * @param polygon
	 * @param solid
	 */
	public PolygonObject(CollideablePolygon polygon, boolean solid) {
		this.polygon = polygon;
		this.solid = solid;
		if (solid) {
			GameServer.getInstance().getMap().getSolidObjects().add(this);
		}
	}

	/**
	 * Checks for a collision with another polygon object.
	 * 
	 * @param other
	 *            the other polygonobject
	 * @return true if there is a collision
	 */
	public boolean collides(PolygonObject other) {
		CollideablePolygon p1 = this.polygon;
		CollideablePolygon p2 = other.polygon;
		Vector2 diff = new Vector2(p2.getX() - p1.getX(), p2.getY() - p1.getY());
		if (diff.len() > p1.getMaxLength() + p2.getMaxLength()) {
			return false;
		}
		return p2.collides(p1);
	}

	/**
	 * Gets the polygon
	 * 
	 * @return polygon
	 */
	public CollideablePolygon getPolygon() {
		return polygon;
	}

	/**
	 * Sets the polygon
	 * 
	 * @param polygon
	 */
	public void setPolygon(CollideablePolygon polygon) {
		this.polygon = polygon;
	}

	/**
	 * Returns if the polygon is solid i.e. cannot overlap with others
	 * 
	 * @return true if solid
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Sets if the polygon is solid i.e. cannot overlap with others
	 * 
	 * @param solid
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * Destructor for PolygonObject. Removes from GameMap's solidObjects list if
	 * it is solid.
	 * 
	 * @return true if successfully destroyed, false if solid and couldn't be
	 *         removed from solidObjects list
	 */
	public boolean destroy() {
		if (solid) {
			return GameServer.getInstance().getMap().getSolidObjects().remove(this);
		}
		return true;
	}

}
