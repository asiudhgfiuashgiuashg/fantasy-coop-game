package com.mygdx.game.server.model.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

/**
 * represents a player of the mage class
 * @author elimonent
 *
 */
public class MagePlayer extends Player {

	protected MagePlayer(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}
}
