package com.mygdx.game.server.model.trigger;

import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * Represents an area that triggers something to happen when the player enters it.
 * The area is specified by a polygon.
 * @author elimonent
 *
 */
public abstract class Trigger extends PolygonObject {

	protected Trigger(CollideablePolygon polygon) {
		super(polygon);
		// TODO Auto-generated constructor stub
	}

}
