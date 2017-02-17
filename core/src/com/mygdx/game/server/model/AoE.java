package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
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
	 * @param polygon the hitbox of the aoe
	 * @param position where the place the aoe hitbox
	 */
	public AoE(float[] vertices, Vector2 position) {
		super(vertices, false, position);
	}
}
