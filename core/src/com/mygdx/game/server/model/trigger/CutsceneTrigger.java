package com.mygdx.game.server.model.trigger;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * A trigger which causes a cutscene to occur (when the player enters the trigger area).
 * @author elimonent
 *
 */
public class CutsceneTrigger extends Trigger {

	public CutsceneTrigger(float[] vectors, String name, Vector2
			position) {
		super(vectors, position);
		// TODO load cutscene of name
	}

	@Override
	public void act(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}

}
