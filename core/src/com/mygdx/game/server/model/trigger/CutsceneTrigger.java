package com.mygdx.game.server.model.trigger;

import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * A trigger which causes a cutscene to occur (when the player enters the trigger area).
 * @author elimonent
 *
 */
public class CutsceneTrigger extends Trigger {

	protected CutsceneTrigger(CollideablePolygon polygon) {
		super(polygon);
		// TODO Auto-generated constructor stub
	}

}
