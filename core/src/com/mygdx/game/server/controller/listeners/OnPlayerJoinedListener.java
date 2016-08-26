package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.server.model.lobby.LobbyManager;
import com.mygdx.game.server.model.lobby.LobbyPlayer;

/**
 * Does the things that need to be done in the lobby when a new player connects.
 * Created by elimonent on 8/26/16.
 */
public class OnPlayerJoinedListener extends Listener {
	private LobbyManager lobbyManager;
	public OnPlayerJoinedListener(LobbyManager lobbyManager) {
		this.lobbyManager = lobbyManager;
	}

	@Override
	public void connected(Connection connection) {
		LobbyPlayer newLobbyPlayer = new LobbyPlayer(connection);
		lobbyManager.addLobbyPlayer(newLobbyPlayer);
	}
}
