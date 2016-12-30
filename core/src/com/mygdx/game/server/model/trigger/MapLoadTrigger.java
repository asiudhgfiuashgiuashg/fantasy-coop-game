package com.mygdx.game.server.model.trigger;

import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A trigger which loads another map upon the player entering it.
 * @author elimonent
 *
 */
public class MapLoadTrigger extends Trigger {

	public MapLoadTrigger(CollideablePolygon polygon, String name) {
		super(polygon);
		// TODO load map of name
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
