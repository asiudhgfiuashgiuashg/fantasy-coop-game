package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.model.CollideablePolygon;

public class SpiderBoss extends Enemy {

	protected SpiderBoss(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {
		//(NOTHIN);
	}

}
