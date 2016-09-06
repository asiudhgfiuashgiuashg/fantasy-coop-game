package com.mygdx.game.server.model.lobby;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logic and state for player's connecting, chatting, choosing classes, readying up.
 * Should directly modify the view of the lobby based on lobby state changes.
 * When the game state transitions from lobby to in-game, all of the lobbyPlayers will transform into Players.
 * Created by elimonent on 8/24/16.
 */
public class LobbyManager {
	private List<LobbyPlayer> lobbyPlayers;

	public LobbyManager() {
		lobbyPlayers = new ArrayList<LobbyPlayer>();
	}

	/**
	 * add a new player to the list of lobbyPlayers and do related things
	 */
	public void addLobbyPlayer(LobbyPlayer player) {
		lobbyPlayers.add(player);
	}

	/**
	 * See if all the players in the lobby are ready to start the game
	 * @return
	 */
	public boolean getReady() {
		for (LobbyPlayer lobbyPlayer: lobbyPlayers) {
			if (!lobbyPlayer.ready.get()) {
				return false;
			}
		}
		return true;
	}

    public boolean classNotTakenYet(PlayerClassEnum requestedClass) {
    	for (LobbyPlayer player: lobbyPlayers) {
			if (player.playerClass == requestedClass) {
				return false;
			}
		}
		return true;
    }

	public LobbyPlayer getPlayerByConnection(Connection connection) {
		for (LobbyPlayer player: lobbyPlayers) {
			if (player.connection.equals(connection)) {
				return player;
			}
		}
		return null;
	}

	public List<LobbyPlayer> getLobbyPlayers() {
		return Collections.unmodifiableList(lobbyPlayers);
	}
}
