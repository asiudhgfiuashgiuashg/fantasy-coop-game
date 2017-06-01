package com.mygdx.game.client.model.entity.player;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.shared.model.CollideablePolygon;
import com.mygdx.game.shared.network.GameMessage;
import com.mygdx.game.client.model.map.*;

/**
 * Represents a local player
 * Basic stuff that every player should have.
 */
public abstract class Player extends DynamicEntity {

	protected static final float PLAYER_WALK_ANIM_SPEED = .08f;
	private CollideablePolygon detectionZone = new CollideablePolygon(new float[]{0, 0, 0, 20, 20, 20, 20, 0});
	protected float nonDiagSpeed = 50;
	private float diagSpeed = (float) Math.sqrt(Math.pow(nonDiagSpeed / 2, 2)); // diag speed ^ 2 = xSpeed ^ 2 + ySpeed ^2
	// whether the player is pressing any of these movement directional keys - used for movement and animation
	public boolean up = false;
	public boolean down = false;
	public boolean right = false;
	public boolean left = false;
	protected boolean attacking = false;
	protected boolean moving = false;

	// not used atm
	public enum Direction {
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
	public Direction directionFacing; // directionFacing the player is moving - used to choose an animation

	public Player(String entUid, String className, Vector2 pos, int visLayer) {
		super(entUid, className, pos, visLayer);
		setMass(1);
		this.solid = true;
		directionFacing = Direction.RIGHT;
	}


	/**
	 * Do class-specific tick stuff based on inputs
	 * @param deltaT
	 */
	@Override
	public void tick(float deltaT) {
		if (!up && !down && !right && !left) {
			moving = false;

		} else if (!up && !down && !right && left) {
			directionFacing = Direction.LEFT;
			moving = true;

		} else if (!up && !down && right && !left) {
			directionFacing = Direction.RIGHT;
			moving = true;

		} else if (!up && !down && right && left) {
			moving = false;

		} else if (!up && down && !right && !left) {
			directionFacing = Direction.DOWN;
			moving = true;

		} else if (!up && down && !right && left) {
			directionFacing = Direction.DOWN_LEFT;
			moving = true;

		} else if (!up && down && right && !left) {
			directionFacing = Direction.DOWN_RIGHT;
			moving = true;

		} else if (!up && down && right && left) {
			directionFacing = Direction.DOWN;
			moving = true;

		} else if (up && !down && !right && !left) {
			directionFacing = Direction.UP;
			moving = true;

		} else if (up && !down && !right && left) {
			directionFacing = Direction.UP_LEFT;
			moving = true;

		} else if (up && !down && right && !left) {
			directionFacing = Direction.UP_RIGHT;
			moving = true;

		} else if (up && !down && right && left) {
			directionFacing = Direction.UP;
			moving = true;

		} else if (up && down && !right && !left) {
			moving = false;

		} else if (up && down && !right && left) {
			directionFacing = Direction.LEFT;
			moving = true;
		} else if (up && down && right && !left) {
			directionFacing = Direction.RIGHT;
			moving = true;
		} else { //  if (up && down && right && left) {
			moving = false;
		}

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
	public void setAnimationWithUpdate(String animName, float frameDuration, Animation.PlayMode playMode) {
		setAnimation(animName, frameDuration, playMode);
		sendAnimationUpdate();
	}

	public void setAnimationWithUpdate(String animName, float frameDuration) {
		setAnimationWithUpdate(animName, frameDuration, Animation.PlayMode.LOOP);
	}

	/**
	 * SetAnimationWithUpdate() only if not on that animation already
	 * @param animName
	 */
	protected void setAnimationIfNotSet(String animName, float frameDuration, Animation.PlayMode playMode) {
		if (!animName.equals(getAnimationName())) {
			setAnimationWithUpdate(animName, frameDuration, playMode);
		}
	}

	protected void setAnimationIfNotSet(String animName, float frameDuration) {
		setAnimationIfNotSet(animName, frameDuration, Animation.PlayMode.LOOP);
	}

	protected void sendAnimationUpdate() {
		GameMessage.AnimationUpdateMessage animMsg = new GameMessage.AnimationUpdateMessage();
		animMsg.animationName = getAnimationName();
		animMsg.frameDuration = getAnimation().getFrameDuration();
		animMsg.playMode = getAnimation().getPlayMode();
		GameClient.getInstance().sendToServer(animMsg);
	}


	public void setAttack(boolean attack) {
		attacking = attack;
	}
	
	public void interact() {
		ClientTiledMap map = GameClient.getInstance().getMap();
		DynamicEntity closestEntity = null;
		float closestDistance = 0f;
		if (directionFacing == Direction.DOWN) {
			detectionZone.setPosition(getBoundingRectangle().getX(), getBoundingRectangle().getY() - detectionZone.getBoundingRectangle().getHeight());
		} else if (directionFacing == Direction.UP) {
			detectionZone.setPosition(getBoundingRectangle().getX(), getBoundingRectangle().getY() + getBoundingRectangle().getHeight());
		} else if (directionFacing == Direction.RIGHT || directionFacing == Direction.DOWN_RIGHT || directionFacing == Direction.UP_RIGHT) {
			detectionZone.setPosition(getBoundingRectangle().getX() + getBoundingRectangle().getWidth(), getBoundingRectangle().getY());
		} else if (directionFacing == Direction.LEFT || directionFacing == Direction.DOWN_LEFT || directionFacing == Direction.UP_LEFT) {
			detectionZone.setPosition(getBoundingRectangle().getX() - detectionZone.getBoundingRectangle().getWidth(), getBoundingRectangle().getY() - 20);
		}
		for (DynamicEntity entity: map.dynamicEntities) {
			if(!this.equals(entity) && entity.collides(detectionZone)) {
				if (closestEntity == null || this.calcDistance(entity) < closestDistance) {
					closestEntity = entity;
					closestDistance = this.calcDistance(entity);
				}
			}
		}
		
		if(closestEntity != null) {
			System.out.println("You are interacting with: " + closestEntity.getClass());
		} else {
			System.out.println("What'chu doin!? There ain't nothin here, foo!");
		}
	}

}
