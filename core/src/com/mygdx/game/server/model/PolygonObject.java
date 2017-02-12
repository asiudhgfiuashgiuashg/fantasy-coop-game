package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.Arrays;

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
	 * Position in global coordinates
	 * polygon hitbox is here
	 */
	protected Vector2 position;

	/* Since hitboxes are stored with origin at the object's position, need
	to translate the hitboxes between two objects into these two
	variables before collision checking.*/

	private static final CollideablePolygon thisTranslatedPolygon = new
			CollideablePolygon();
	private static final CollideablePolygon otherTranslatedPolygon = new
			CollideablePolygon();

	/**
	 * Constructs a PolygonObject and adds it to GameMap's list of solidObjects
	 * if it is solid.
	 *  @param polygon
	 * @param solid
	 * @param position
	 */
	public PolygonObject(CollideablePolygon polygon, boolean solid, Vector2
			position) {
		this.polygon = polygon;
		this.solid = solid;
		this.position = position;
		if (polygon != null) {
			polygon.setPosition(position);
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
		CollideablePolygon p1 = new CollideablePolygon(this.polygon);
		CollideablePolygon p2 = new CollideablePolygon(other.polygon);
		p1.setPosition(this.position);
		p2.setPosition(other.position);
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

	/**
	 * Returns a copy of the entity's position. All modifications must be done
	 * via setPosition()
	 *
	 * @return copy of position
	 */
	public Vector2 getPosition() {
		return new Vector2(position);
	}

}
