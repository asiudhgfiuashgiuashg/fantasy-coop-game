package com.mygdx.game.server.model.lobby;

import com.esotericsoftware.kryonet.Connection;

/**
 * Represents a player in a lobby.
 * Created by elimonent on 8/24/16.
 */
public class LobbyPlayer {
	private Connection connection; //uniquely identifies the player and used to send messages to them
	private PlayerClass playerClass;

	 public LobbyPlayer(Connection connection) {
	 	this.connection = connection;
	 }

	protected enum PlayerClass {
		MAGE,
		RANGER,
		SHIELD,
	}
}
