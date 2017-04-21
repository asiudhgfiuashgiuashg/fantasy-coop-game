package com.mygdx.game.client.model.entity.player;


import com.badlogic.gdx.math.Vector2;

public class MagePlayer extends Player {

	public MagePlayer(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setAnimation("right_dark", 1f);
	}

	@Override
	public void tick(float deltaT) {
		super.tick(deltaT);
		if (!up && !down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk_dark", "_dark"), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && !down && !right && left) {
			setAnimationIfNotSet("left_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, 0));
		} else if (!up && !down && right && !left) {
			setAnimationIfNotSet("right_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, 0));
		} else if (!up && !down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk_dark", "_dark"), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (!up && down && !right && !left) {
			setAnimationIfNotSet("down_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -nonDiagSpeed));
		} else if (!up && down && !right && left) {
			setAnimationIfNotSet("left_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, -nonDiagSpeed));
		} else if (!up && down && right && !left) {
			setAnimationIfNotSet("right_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, -nonDiagSpeed));
		} else if (!up && down && right && left) {
			setAnimationIfNotSet("down_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, -nonDiagSpeed));
		} else if (up && !down && !right && !left) {
			setAnimationIfNotSet("up_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, nonDiagSpeed));
		} else if (up && !down && !right && left) {
			setAnimationIfNotSet("left_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, nonDiagSpeed));
		} else if (up && !down && right && !left) {
			setAnimationIfNotSet("right_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, nonDiagSpeed));
		} else if (up && !down && right && left) {
			setAnimationIfNotSet("up_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, nonDiagSpeed));
		} else if (up && down && !right && !left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk_dark", "_dark"), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		} else if (up && down && !right && left) {
			setAnimationIfNotSet("left_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(-nonDiagSpeed, 0));
		} else if (up && down && right && !left) {
			setAnimationIfNotSet("right_walk_dark", PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(nonDiagSpeed, 0));
		} else { //  if (up && down && right && left) {
			setAnimationWithUpdate(getAnimationName().replace("_walk_dark", "_dark"), PLAYER_WALK_ANIM_SPEED);
			setVelocityIfNotSet(new Vector2(0, 0));
		}

		sendPositionUpdate();

	}
}
