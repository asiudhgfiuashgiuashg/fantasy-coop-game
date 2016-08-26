package com.mygdx.game.server.model.lobby;

/**
 * Represents a player in a lobby.
 * Created by elimonent on 8/24/16.
 */
public class LobbyPlayer {

	private PlayerClass playerClass;

	protected enum PlayerClass {
		MAGE,
		RANGER,
		SHIELD,
	}
}
