package com.mygdx.game.server.model.trigger;

import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A trigger which causes a cutscene to occur (when the player enters the trigger area).
 * @author elimonent
 *
 */
public class CutsceneTrigger extends Trigger {

	public CutsceneTrigger(CollideablePolygon polygon, String name) {
		super(polygon);
		// TODO load cutscene of name
	}

	@Override
	public void act(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

}
