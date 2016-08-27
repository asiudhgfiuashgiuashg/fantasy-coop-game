package com.mygdx.game.server.model.lobby;

import com.esotericsoftware.kryonet.Connection;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a player in a lobby.
 * Created by elimonent on 8/24/16.
 */
public class LobbyPlayer {
	private Connection connection; //uniquely identifies the player and used to send messages to them
	private PlayerClassEnum playerClass;
	private String username;

	public final AtomicBoolean ready = new AtomicBoolean(false); // Whether or not this player is ready to start.
	                                                             // This is Atomic because the Server thread will access it
	                                                             //  and also the listeners on the Kryo Server thread, so 2 different threads.

	 public LobbyPlayer(Connection connection) {
	 	this.connection = connection;
	 }
}
