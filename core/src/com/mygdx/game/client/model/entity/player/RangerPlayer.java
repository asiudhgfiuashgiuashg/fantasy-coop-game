package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.math.Vector2;

public class RangerPlayer extends Player {

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

		if (!up && !down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && !down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, 0));
		} else if (!up && !down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, 0));
		} else if (!up && !down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && down && !right && !left) {
			setAnimationIfNotSet("down_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -nonDiagSpeed));
		} else if (!up && down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, -nonDiagSpeed));
		} else if (!up && down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, -nonDiagSpeed));
		} else if (!up && down && right && left) {
			setAnimationIfNotSet("down_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -nonDiagSpeed));
		} else if (up && !down && !right && !left) {
			setAnimationIfNotSet("up_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, nonDiagSpeed));
		} else if (up && !down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, nonDiagSpeed));
		} else if (up && !down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, nonDiagSpeed));
		} else if (up && !down && right && left) {
			setAnimationIfNotSet("up_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, nonDiagSpeed));
		} else if (up && down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (up && down && !right && left) {
			setAnimationIfNotSet("left_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, 0));
		} else if (up && down && right && !left) {
			setAnimationIfNotSet("right_walk", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, 0));
		} else { //  if (up && down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk", ""), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		}

		super.tick(deltaT);

	}
}
