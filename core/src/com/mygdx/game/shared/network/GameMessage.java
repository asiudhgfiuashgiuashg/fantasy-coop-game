package com.mygdx.game.shared.network;

import com.badlogic.gdx.math.Vector2;

public class GameMessage extends Message {
	public static class DrawMessage extends GameMessage {
		public String entityUID;
		public Vector2 position;
		public String spriteName;
		public int visLayer;
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
	
	// and many more to come! These are just some examples
}
