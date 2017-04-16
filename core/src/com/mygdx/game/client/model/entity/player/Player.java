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

	private static final float PLAYER_WALK_ANIM_SPEED = .08f;
	private float speed = 50;
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
		setAnimation("right", 1f);
	}


	
	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);

		if (!up && !down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && !down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-speed, 0));
		} else if (!up && !down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(speed, 0));
		} else if (!up && !down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && down && !right && !left) {
			setAnimationIfNotSet("down_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -speed));
		} else if (!up && down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-speed, -speed));
		} else if (!up && down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(speed, -speed));
		} else if (!up && down && right && left) {
			setAnimationIfNotSet("down_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -speed));
		} else if (up && !down && !right && !left) {
			setAnimationIfNotSet("up_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, speed));
		} else if (up && !down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-speed, speed));
		} else if (up && !down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(speed, speed));
		} else if (up && !down && right && left) {
			setAnimationIfNotSet("up_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, speed));
		} else if (up && down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (up && down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-speed, 0));
		} else if (up && down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(speed, 0));
		} else { //  if (up && down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		}

		sendPositionUpdate();

	}

	private void sendPositionUpdate() {
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
