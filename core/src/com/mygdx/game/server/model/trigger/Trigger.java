package com.mygdx.game.server.model.trigger;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Actable;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * Represents an area that triggers something to happen when the player enters
 * it. The area is specified by a polygon. A Trigger is always non-solid.
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public abstract class Trigger extends PolygonObject implements Actable {

	/**
	 *
	 * @param polygon the hitbox of the trigger
	 * @param position where to place the hitbox (the position of the trigger)
	 */
	protected Trigger(float[] vertices, Vector2 position) {
		super(vertices, false, position);
	}

}
