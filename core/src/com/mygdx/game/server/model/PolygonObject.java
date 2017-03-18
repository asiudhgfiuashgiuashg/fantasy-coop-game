package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.Arrays;

/**
 * A
 * PolygonObject is the base class for many server entities and objects. Solid
 * PolygonObjects are not allowed to have their hitboxes overlap and have
 * special collision checking ( see VelocityEntity ).
 *
 * @author elimonent
 * @author Sawyer Harris
 */
public abstract class PolygonObject extends CollideablePolygon {


	/**
	 * Constructs a PolygonObject and adds it to GameMap's list of solidObjects
	 * if it is solid.
	 *
	 * @param polygon
	 * @param solid
	 * @param position
	 */
	public PolygonObject(float[] vertices, boolean solid, Vector2 position) {
		super(vertices);
		this.solid = solid;
		setPosition(position);
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
	 * removed from solidObjects list
	 */
	public boolean destroy() {
		if (solid) {
			return GameServer.getInstance().getMap().getSolidObjects().remove(this);
		}
		return true;
	}


}
