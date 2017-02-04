package com.mygdx.game.server.model.lobby;


import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.GameState;
import com.mygdx.game.shared.model.lobby.LobbyManager;
import com.mygdx.game.shared.network.LobbyMessage.GameStartMessage;

import java.util.ArrayList;

import static com.mygdx.game.client.model.GameClient.console;

/**
 * Logic and state for player's connecting, chatting, choosing classes, readying up.
 * Should directly modify the view of the lobby based on lobby state changes.
 * When the game state transitions from lobby to in-game, all of the lobbyPlayers will transform into Players.
 * Created by elimonent on 8/24/16.
 */
public class ServerLobbyManager extends LobbyManager<ServerLobbyPlayer> {

	private static final GameServer server = GameServer.getInstance();
	
	public ServerLobbyManager() {
		lobbyPlayers = new ArrayList<ServerLobbyPlayer>();
	}
	
	public void checkForGameStart() {
		for (ServerLobbyPlayer player : lobbyPlayers) {
			if (!player.isReady() || null == player.getPlayerClass()) {
				return;
			}
		}
		System.out.println("starting game");
		server.startGame();
	}
}
