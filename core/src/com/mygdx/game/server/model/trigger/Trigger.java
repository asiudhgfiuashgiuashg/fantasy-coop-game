package com.mygdx.game.server.model.trigger;

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

	protected Trigger(CollideablePolygon polygon) {
		super(polygon, false);
	}

}
