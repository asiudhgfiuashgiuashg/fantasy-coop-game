package com.mygdx.game.server.model;

import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A temporary area of effect possibly used for checking sword swings, spell
 * hitboxes, etc. AoE is always non-solid and has the lifespan of a local
 * variable.
 * 
 * @author Sawyer Harris
 *
 */
public class AoE extends PolygonObject {
	/**
	 * Constructs an AoE as a non-solid PolygonObject
	 * 
	 * @param polygon
	 */
	public AoE(CollideablePolygon polygon) {
		super(polygon, false);
	}
}
