package com.mygdx.game.shared.network;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.DialogueLine;
import com.mygdx.game.shared.model.EntityLight;

import java.util.List;

public class GameMessage extends Message {
	public String entityUID; // many messages use this field to identify the target of the message

	public static class PosUpdateMessage extends GameMessage {
		public Vector2 position;
		public int visLayer;
		public Vector2 velocity;
	}

	public static class AnimationUpdateMessage extends GameMessage {
		public String animationName;
		public float frameDuration;
		public Animation.PlayMode playMode;
	}
	
	public static class MoveMessage extends GameMessage {
		public MoveDirection direction;
		
		public enum MoveDirection {
			RIGHT, UP, LEFT, DOWN
		}
	}
	
	public static class AttackMessage extends GameMessage {
		public Vector2 destination = new Vector2(0, 0);
	}
	
	public static class SpellCastMessage extends GameMessage {
		public int spellSlot;
	}

	/** tells client to initialize a dynamic entity (spawn it on the map) */
	public static class InitDynamicEntityMsg extends GameMessage {
		public String className;
		public Vector2 pos;
		public boolean solid;
		public float[] vertices;
		public int visLayer;
		public boolean isLocalPlayer = false; // whether the client should treat this as local player
		public float mass;
		public List<EntityLight> entityLightList;
		public float rotation;
		public float originX;
		public float originY;
		public int health;
		public int maxHealth;
		public boolean hasHealth; // whether the client should care about the health of this thing
	}
	
	public static class HealthUpdateMsg extends GameMessage {
		public int health; // new health
	}

	public static class HitboxUpdateMessage extends GameMessage {
		public float[] vertices;
	}

	// tell the client to remove a dynamic entity from the game
	public static class RemoveDynamicEntityMsg extends GameMessage {

	}

	// tells the server that a client has interacted with a dynamic entity
	public static class InteractionMsg extends GameMessage {

	}

	// used to tell the client that an NPC is saying something that should be displayed.
	public static class SayMsg extends GameMessage {
		public DialogueLine dialogueLine;
	}

	// and many more to come! These are just some examples
}
