package com.mygdx.game.server.model.lobby;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.shared.model.LobbyManager;
import com.mygdx.game.shared.model.LobbyPlayer;
import com.mygdx.game.shared.util.network.messages.lobby.GameStartMsg;

import java.util.ArrayList;

/**
 * Logic and state for player's connecting, chatting, choosing classes, readying up.
 * Should directly modify the view of the lobby based on lobby state changes.
 * When the game state transitions from lobby to in-game, all of the lobbyPlayers will transform into Players.
 * Created by elimonent on 8/24/16.
 */
public class ServerLobbyManager extends LobbyManager<ServerLobbyPlayer> {

	private Server server;
	public ServerLobbyManager(Server server) {
		lobbyPlayers = new ArrayList<ServerLobbyPlayer>();
		this.server = server;
	}

	/**
	 * See if all the players in the lobby are ready to start the game
	 * @return
	 */
	public boolean getReady() {
		for (ServerLobbyPlayer lobbyPlayer: lobbyPlayers) {
			if (!lobbyPlayer.ready.get() || null == lobbyPlayer.playerClass) {
				return false;
			}
		}
		return true;
	}

	public ServerLobbyPlayer getPlayerByConnection(Connection connection) {
		for (ServerLobbyPlayer player: lobbyPlayers) {
			if (player.connection.equals(connection)) {
				return player;
			}
		}
		return null;
	}

	public void onAllReady() {
		server.sendToAllTCP(new GameStartMsg());
	}
}
