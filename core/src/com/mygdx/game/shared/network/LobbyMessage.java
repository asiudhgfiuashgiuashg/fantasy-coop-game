package com.mygdx.game.shared.network;

import com.mygdx.game.shared.model.lobby.PlayerClass;

public class LobbyMessage extends Message {
	public static class ChatMessage extends LobbyMessage {
		public String message;
	}
	
	public static class ChooseUsernameMessage extends LobbyMessage {
		public String username;
	}
	
	public static class ClassAssignmentMessage extends LobbyMessage {
		public PlayerClass playerClass;
	}
	
	public static class OtherClassAssignmentMessage extends LobbyMessage {
		public PlayerClass playerClass;
	}
	
	public static class GameStartMessage extends LobbyMessage {
	}
	
	public static class LobbyPlayerInfoMessage extends LobbyMessage {
	    public String username;
	    public PlayerClass playerClass;
	}
	
	public static class ReadyStatusMessage extends LobbyMessage {
	    public boolean ready;
	}
}
