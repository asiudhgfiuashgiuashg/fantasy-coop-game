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

	protected static final float PLAYER_WALK_ANIM_SPEED = .08f;
	protected float nonDiagSpeed = 50;
	private float diagSpeed = (float) Math.sqrt(Math.pow(nonDiagSpeed / 2, 2)); // diag speed ^ 2 = xSpeed ^ 2 + ySpeed ^2
	// whether the player is pressing any of these movement directional keys - used for movement and animation
	public boolean up = false;
	public boolean down = false;
	public boolean right = false;
	public boolean left = false;

	// not used atm
	private enum Direction {
		UP,
		UP_RIGHT,
		RIGHT,
		DOWN_RIGHT,
		DOWN,
		DOWN_LEFT,
		LEFT,
		UP_LEFT,
	}

	// not used atm
	private Direction direction; // direction the player is moving - used to choose an animation

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setMass(1);
		this.solid = true;
	}


	
	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);


		sendPositionUpdate();

	}

	void sendPositionUpdate() {
		GameMessage.PosUpdateMessage posMsg = new GameMessage.PosUpdateMessage();
		posMsg.position = getPosition();
		posMsg.velocity = getVelocity();
		GameClient.getInstance().sendToServer(posMsg);
	}


	/**
	 * Set animation and send the animation name to server (used for local player)
	 * @param animName
	 */
	public void setAnimationWithUpdate(String animName, float frameDuration) {
		setAnimation(animName, frameDuration);
		sendAnimationUpdate();
	}

	/**
	 * SetAnimationWithUpdate() only if not on that animation already
	 * @param animName
	 */
	protected void setAnimationIfNotSet(String animName, float frameDuration) {
		if (!animName.equals(getAnimationName())) {
			setAnimationWithUpdate(animName, frameDuration);
		}
	}

	protected void sendAnimationUpdate() {
		GameMessage.AnimationUpdateMessage animMsg = new GameMessage.AnimationUpdateMessage();
		animMsg.animationName = getAnimationName();
		animMsg.frameDuration = getAnimation().getFrameDuration();
		GameClient.getInstance().sendToServer(animMsg);
	}

}
