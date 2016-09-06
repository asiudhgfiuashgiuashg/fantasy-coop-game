package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.server.model.lobby.LobbyManager;
import com.mygdx.game.server.model.lobby.LobbyPlayer;
import com.mygdx.game.util.SingletonGUIConsole;
import com.mygdx.game.util.network.messages.lobby.LobbyPlayerInfoMsg;

/**
 * Does the things that need to be done in the lobby when a new player connects.
 * Created by elimonent on 8/26/16.
 */
public class OnPlayerJoinedListener extends Listener {
	private LobbyManager lobbyManager;
	private Server server;
	private final SingletonGUIConsole console = SingletonGUIConsole.getInstance();

	public OnPlayerJoinedListener(LobbyManager lobbyManager, Server server) {
		this.lobbyManager = lobbyManager;
		this.server = server;
	}

	@Override
	public void connected(Connection connection) {
		//send the new player the info of every other currently-connected player.
		for (LobbyPlayer lobbyPlayer: lobbyManager.getLobbyPlayers()) {
			LobbyPlayerInfoMsg playerInfoMsg = new LobbyPlayerInfoMsg(lobbyPlayer.uid, lobbyPlayer.username, lobbyPlayer.playerClass);
			server.sendToTCP(connection.getID(), playerInfoMsg);
		}
		LobbyPlayer newLobbyPlayer = new LobbyPlayer(connection);
		lobbyManager.addLobbyPlayer(newLobbyPlayer);
		//Send everyone the uid of the player who just connected to the lobby.
		//This uid will be referenced when providing info updates like username in the lobby's future
		LobbyPlayerInfoMsg playerInfoMsg = new LobbyPlayerInfoMsg(newLobbyPlayer.uid, null, null);
		server.sendToAllExceptTCP(connection.getID(), playerInfoMsg);
	}
}
