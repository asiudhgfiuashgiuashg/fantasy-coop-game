package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.model.LobbyPlayer;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.mygdx.game.shared.util.network.messages.lobby.LobbyPlayerInfoMsg;

/**
 * Does the things that need to be done in the lobby when a new player connects.
 * Created by elimonent on 8/26/16.
 */
public class OnPlayerJoinedListener extends Listener {
	private ServerLobbyManager serverLobbyManager;
	private Server server;
	private final SingletonGUIConsole console = SingletonGUIConsole.getInstance();

	public OnPlayerJoinedListener(ServerLobbyManager serverLobbyManager, Server server) {
		this.serverLobbyManager = serverLobbyManager;
		this.server = server;
	}

	@Override
	public void connected(Connection connection) {
		//send the new player the info of every other currently-connected player.
		for (LobbyPlayer lobbyPlayer: serverLobbyManager.getLobbyPlayers()) {
			LobbyPlayerInfoMsg playerInfoMsg = new LobbyPlayerInfoMsg(lobbyPlayer.getUid(), lobbyPlayer.username, lobbyPlayer.playerClass);
			server.sendToTCP(connection.getID(), playerInfoMsg);
		}
		ServerLobbyPlayer newLobbyPlayer = new ServerLobbyPlayer(connection);
		serverLobbyManager.addLobbyPlayer(newLobbyPlayer);
		//Send everyone else the uid of the player who just connected to the lobby.
		//This uid will be referenced when providing info updates like username in the lobby's future
		LobbyPlayerInfoMsg playerInfoMsg = new LobbyPlayerInfoMsg(newLobbyPlayer.getUid(), null, null);
		server.sendToAllExceptTCP(connection.getID(), playerInfoMsg);
	}
}
