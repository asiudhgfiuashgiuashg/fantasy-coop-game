package com.mygdx.game.server.model.entity.player;


import com.badlogic.gdx.math.Vector2;

public class ShieldPlayer extends Player {
	public ShieldPlayer(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	@Override
	public void attack(Vector2 destination) {

	}

}
