package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.shared.network.GameMessage;

/**
 * Represents a local player
 * Basic stuff that every player should have.
 */
public abstract class Player extends DynamicEntity {

	private static final float movementForce = 1000; // how much force to apply when moving
	private float speed = 50;
	public boolean up = false;
	public boolean down = false;
	public boolean right = false;
	public boolean left = false;

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setMass(1);
	}
	
	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);
		if (up && !down) {
			setVelocity(new Vector2(getVelocity().x, speed));
			if (!"up_walk".equals(getAnimationName())) { // setting the animation will restart it, so avoid that
				setAnimation("up_walk");
				sendAnimationUpdate();
			}
		} else if (!up && down) {
			setVelocity(new Vector2(getVelocity().x, -speed));
			if (!"down_walk".equals(getAnimationName())) {
				setAnimation("down_walk");
				sendAnimationUpdate();
			}
		} else {
			setVelocity(new Vector2(getVelocity().x, 0));
		}
		if (right && !left) {
			setVelocity(new Vector2(speed, getVelocity().y));
			if (!"right_walk".equals(getAnimationName())) {
				setAnimation("right_walk");
				sendAnimationUpdate();
			}
		} else if (!right && left) {
			setVelocity(new Vector2(-speed, getVelocity().y));
			if (!"left_walk".equals(getAnimationName())) {
				setAnimation("left_walk");
				sendAnimationUpdate();
			}
		} else {
			setVelocity(new Vector2(0, getVelocity().y));
		}
		sendPositionUpdate();

	}


	private void sendAnimationUpdate() {
		GameMessage.AnimationUpdateMessage animMsg = new GameMessage.AnimationUpdateMessage();
		animMsg.animationName = getAnimationName();
		GameClient.getInstance().sendToServer(animMsg);
	}

	private void sendPositionUpdate() {
		GameMessage.PosUpdateMessage posMsg = new GameMessage.PosUpdateMessage();
		posMsg.position = getPosition();
		posMsg.velocity = getVelocity();
		GameClient.getInstance().sendToServer(posMsg);
	}
}
