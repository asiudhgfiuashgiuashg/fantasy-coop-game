package com.mygdx.game.server.model;

import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.server.controller.listeners.NewConnectionReporter;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.server.model.exceptions.ServerNotInitializedException;
import com.mygdx.game.server.model.lobby.LobbyManager;
import com.mygdx.game.server.model.player.Player;
import com.mygdx.game.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

	private LobbyManager lobbyManager;

	private final List<Player> players = new ArrayList<Player>();
	private GameMap map; // The current game map.
	private com.esotericsoftware.kryonet.Server server; //used for sending and receiving.
	private final float TICKRATE = .05f; //How often the server updates the game world (approximate) in seconds.
	private GameState state;
	private Cutscene currCutscene;

	private static Server instance;
	private int port;
	public static final int DEFAULT_PORT = 63332;
	private boolean initialized; //whether or not the server has been initialized
	private float ONE_BILLION = 1000000000;
	private long ONE_MILLION = 1000000;

	private AtomicBoolean running = new AtomicBoolean(); //whether the server is running or not.
							 //flip this boolean to false to stop the server.

	/**
	 * No one can call this since it is private.
	 * Helps enforce singleton pattern.
	 */
	private Server() {
		initialized = false;
	}



	/**
	 * The server loop is in this function
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

		running.set(true) ;

		/*
		 * Main server loop
		 */
		long prevTime = System.nanoTime();
		while (running.get()) {
			long currTime = System.nanoTime();
			long elapsedTime = currTime - prevTime;
			prevTime = currTime;

			//TODO update game simulation using elapsedTime here
			//TODO send outgoing updates to the ServerCommunicator here

			/*
			 * Now sleep until the next tick (approximately).
			 */
			long timeTaken = System.nanoTime() - prevTime;
			long tickTimeRemaining = (long) (TICKRATE * ONE_BILLION) - timeTaken;
			try {
				Thread.sleep(tickTimeRemaining / ONE_MILLION);
			} catch (InterruptedException e) {
				SingletonGUIConsole.getInstance().log(e.getMessage(), LogLevel.ERROR);
			} catch (IllegalArgumentException e) { //negative number
				SingletonGUIConsole.getInstance().log("Issue with system time resulted in a negative delta t", LogLevel.ERROR);
			}
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
	public void init(int port) throws ServerAlreadyInitializedException {
		if (!initialized) {
			this.port = port;
			initialized = true;
		} else {
			throw new ServerAlreadyInitializedException(port);
		}
		server = new com.esotericsoftware.kryonet.Server();
		server.start();
		try {
			server.bind(port);
		} catch (IOException e) {
			SingletonGUIConsole.getInstance().log(e.getMessage(), LogLevel.ERROR);
		}
		setupLobby();
	}

	/**
	 * Stop the server. Its thread will terminate.
	 */
	public void stop() {
		initialized = false;
		running.set(false); //will cause run loop to stop
	}


	/**
	 * get the server ready to receive players into its lobby.
	 */
	private void setupLobby() {
		setState(GameState.LOBBY);
		lobbyManager = new LobbyManager();
		addKryoServerLobbyListeners();
	}

	/**
	 * add the listeners that will process the various network updates from clients
	 */
	private void addKryoServerLobbyListeners() {
		server.addListener(new NewConnectionReporter());
	}

	/**
	 * get the kryonet server (used for all network comms)
	 * @return
	 */
	public com.esotericsoftware.kryonet.Server getServer() {
		return server;
	}

	public boolean isRunning() {
		return running.get();
	}

}
