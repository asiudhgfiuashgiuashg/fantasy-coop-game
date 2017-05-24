package com.mygdx.game.client.model.entity.player;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class ShieldPlayer extends Player {


	public ShieldPlayer(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setAnimation("right", 1f, Animation.PlayMode.LOOP);
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

	/**
	 * Shield player specific on-attack stuff here.
	 * @param attack
	 */
	@Override
	public void setAttack(boolean attack) {
		super.setAttack(attack);
	}
}
