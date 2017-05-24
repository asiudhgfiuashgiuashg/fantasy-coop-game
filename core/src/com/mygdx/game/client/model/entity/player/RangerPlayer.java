package com.mygdx.game.client.model.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.shared.network.GameMessage;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

public class RangerPlayer extends Player {

	private static final float ATTACK_ANIM_SPEED = 0.1f;
	private static final long TIME_BETWEEN_ARROWS = 800; // auto attack time in milliseconds
	long timeLastShotArrow = 0;

	public RangerPlayer(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setAnimation("right", 1f, Animation.PlayMode.LOOP);
	}

	/**
	 * Do class-specific tick stuff based on inputs
	 * @param deltaT
	 */
	@Override
	public void tick(float deltaT) {
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

		if (attacking) {
			if (timeSinceMillis(timeLastShotArrow) > TIME_BETWEEN_ARROWS) {
				GameMessage.AttackMessage atkMsg = new GameMessage.AttackMessage();
				Vector3 unprojected3 = GameClient.getInstance().getRenderer().camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				Vector2 destination = new Vector2(unprojected3.x, unprojected3.y);
				atkMsg.destination = destination;
				GameClient.getInstance().sendToServer(atkMsg);
				timeLastShotArrow = millis();
			}
		}

		super.tick(deltaT);

	}

	/**
	 * Do ranger-specific on-attacking things here
	 */
	@Override
	public void setAttack(boolean attack) {
		super.setAttack(attack);
	}
}
