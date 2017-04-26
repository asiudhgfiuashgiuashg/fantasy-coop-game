package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;

public class RangerPlayer extends Player {

	private static final float ATTACK_ANIM_SPEED = 0.1f;

	public RangerPlayer(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setAnimation("right", 1f);
	}

	/**
	 * Do class-specific tick stuff based on inputs
	 * @param deltaT
	 */
	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);

		if (directionFacing == Direction.UP) {
			if (moving) {
				setAnimationIfNotSet("up_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(0, nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("up_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("up", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.UP_RIGHT)  {
			if (moving) {
				setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(nonDiagSpeed, nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("right_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("right", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.RIGHT)  {
			if (moving) {
				setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(nonDiagSpeed, 0));
			} else if (attacking) {
				setAnimationIfNotSet("right_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("right", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.DOWN_RIGHT)  {
			if (moving) {
				setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(nonDiagSpeed, -nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("right_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("right", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.DOWN)  {
			if (moving) {
				setAnimationIfNotSet("down_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(0, -nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("down_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("down", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.DOWN_LEFT)  {
			if (moving) {
				setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(-nonDiagSpeed, -nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("left_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("left", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.LEFT)  {
			if (moving) {
				setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(-nonDiagSpeed, 0));
			} else if (attacking) {
				setAnimationIfNotSet("left_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("left", PLAYER_WALK_ANIM_SPEED);
			}
		} else if (directionFacing == Direction.UP_LEFT)  {
			if (moving) {
				setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
				setVelocityIfNotSet(new Vector2(-nonDiagSpeed, nonDiagSpeed));
			} else if (attacking) {
				setAnimationIfNotSet("left_firing", ATTACK_ANIM_SPEED);
			} else {
				setAnimationIfNotSet("left", PLAYER_WALK_ANIM_SPEED);
			}
		}

		if (!moving) {
			setVelocityIfNotSet(new Vector2(0, 0));
		}

	}

	/**
	 * Do ranger-specific on-attacking things here
	 */
	@Override
	public void setAttack(boolean attack) {
		super.setAttack(attack);
	}
}
