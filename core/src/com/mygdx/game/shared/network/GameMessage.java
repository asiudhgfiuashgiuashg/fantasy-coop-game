package com.mygdx.game.shared.network;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.entity.DynamicEntity;
import com.mygdx.game.shared.model.CollideablePolygon;

public class GameMessage extends Message {
	public static class PosUpdateMessage extends GameMessage {
		public String entityUID;
		public Vector2 position;
		public int visLayer;
		public Vector2 velocity;
	}

	public static class AnimationUpdateMessage extends GameMessage {
		public String animationName;
		public String entityUID;
	}
	
	public static class MoveMessage extends GameMessage {
		public MoveDirection direction;
		
		public enum MoveDirection {
			RIGHT, UP, LEFT, DOWN
		}
	}
	
	public static class AttackMessage extends GameMessage {
	}
	
	public static class SpellCastMessage extends GameMessage {
		public int spellSlot;
	}

	/** tells client to initialize a dynamic entity (spawn it on the map) */
	public static class InitDynamicEntityMsg extends GameMessage {
		public String className;
		public Vector2 pos;
		public String entUid;
		public boolean solid;
		public float[] vertices;
		public int visLayer;
		public boolean isLocalPlayer = false; // whether the client should treat this as local player
		public float mass;
	}

	public static class HitboxUpdateMessage extends GameMessage {
		public String entityUID;
		public float[] vertices;
	}
	// and many more to come! These are just some examples
}
