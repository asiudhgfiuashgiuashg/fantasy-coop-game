package com.mygdx.game.server.model.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class TestEnemy extends Enemy {

	protected TestEnemy(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	@Override
	public void act() {
		//position.add(0, -1);
		draw();
	}

}
