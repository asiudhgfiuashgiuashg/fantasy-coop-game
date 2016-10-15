package com.mygdx.game.server.model.lobby;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.shared.model.LobbyPlayer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a player in a lobby.
 * Created by elimonent on 8/24/16.
 */
public class ServerLobbyPlayer extends LobbyPlayer {
	public Connection connection; //uniquely identifies the player and used to send messages to them


	private static int uidIncrementor = 0; //used to assign a uid to lobbyPlayer


	 public ServerLobbyPlayer(Connection connection) {
	 	 this.connection = connection;
		 this.uid = uidIncrementor;
		 uidIncrementor++;
	 }
}
