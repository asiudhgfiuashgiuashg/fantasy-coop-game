package com.mygdx.game.server.model;

import com.mygdx.game.server.controller.ServerCommunicator;
import com.mygdx.game.server.model.exceptions.AlreadyInitializedException;
import com.mygdx.game.server.model.exceptions.ServerNotInitializedException;
import com.mygdx.game.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author elimonent
 *
 * A singleton which keeps track of the server's state
 *  and simulates the world (ticks)
 *  
 * A server instance is created by a client using a button in the client's menu.
 * The player who creates a server instance with their client is the Host.
 */
public class Server implements Runnable {

	private final List<Player> players = new ArrayList<Player>();
	private GameMap map; // The current game map.
	private final ServerCommunicator communicator = new ServerCommunicator();
	private final float TICKRATE = .05f; //How often the server updates the game world.
	private GameState state;
	private Cutscene currCutscene;

	private static Server instance;
	private int port;
	public static final int DEFAULT_PORT = 63332;
	private boolean initialized; //whether or not the server has been initialized

	/**
	 * No one can call this since it is private.
	 * Helps enforce singleton pattern.
	 */
	private Server() {
		initialized = false;
	}



	/**
	 * The server loop
	 */
	@Override
	public void run() {
		/*
		 * Check if the server is initialized, and if it's not, throw an exception.
		 */
		try {
			if (!initialized) {
				throw new ServerNotInitializedException();
			}
		} catch (ServerNotInitializedException e) {
			Thread t = Thread.currentThread();
			t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
	}




	public void setState(GameState state) {
		this.state = state;
	}

	/**
	 * Instantiate/access the Server instance using this method.
	 * @return the Server instance
	 */
	public static Server getInstance() {
		if (null == instance) {
			instance = new Server();
		}
		return instance;
	}

	/**
	 * Setup and start the server instance.
	 * @param port
	 */
	public void init(int port) throws AlreadyInitializedException {
		if (!initialized) {
			this.port = port;
			initialized = true;
		} else {
			System.out.println("server was already initialized");
			throw new AlreadyInitializedException(port);
		}
	}

}
