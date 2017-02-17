package com.mygdx.game.server.model.trigger;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A trigger which loads another map upon the player entering it.
 * @author elimonent
 *
 */
public class MapLoadTrigger extends Trigger {

	public MapLoadTrigger(float[] vertices, String name, Vector2
			position) {
		super(vertices, position);
		// TODO load map of name
	}

	@Override
	public void act(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

}
