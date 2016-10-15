package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.util.CollideablePolygon;

public class SpiderBoss extends Enemy {

	protected SpiderBoss(String uid, Vector2 position, String spriteName, int visLayer, CollideablePolygon polygon) {
		super(uid, position, spriteName, visLayer, polygon);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DrawMessage draw() {
		// TODO Auto-generated method stub
		return null;
	}

}
