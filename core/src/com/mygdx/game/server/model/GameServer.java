package com.mygdx.game.server.model;

import com.mygdx.game.server.controller.ServerCommunicator;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.server.model.exceptions.ServerNotInitializedException;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.server.model.player.Player;
import com.mygdx.game.shared.model.exceptions.MapLoaderException;
import com.mygdx.game.shared.network.Message;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mygdx.game.client.model.GameClient.console;

/**
 * The GameServer singleton acts as a hub for the model that manages the in-game
 * server data and updates the model. First, it processes client messages from
 * the buffer, then it calls the act() method on all Actables stored in the
 * current GameMap and buffers any responses to be sent to the clients. Finally
 * it sends those responses.
 * 
 * The GameServer runs on its own thread and constructs the ServerCommunicator
 * which initializes the KryoNet networking server which also runs on its own
 * thread. The GameServer is created by a client who chooses to host a game.
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public class GameServer implements Runnable {
	/** Tickrate of game server i.e. how often the model is updated */
	private static final float TICKRATE = .05f;

	/** Time conversions */
	private static final float NANOSECONDS = 1000000000;
	private static final long MILLISECONDS = 1000000;

	/** Singleton instance */
	private static GameServer instance = new GameServer();

	/** The current game state */
	private GameState state;

	private final ServerTmxLoader mapLoader = new ServerTmxLoader();

	/** The current in-game map */
	private GameMap map;

	/** The current cutscene being played */
	private Cutscene cutscene;

	/** Player models */
	private final List<Player> players = new ArrayList<Player>();

	/** Whether the server has been initialized */
	private boolean initialized;

	/** Whether the server is running. Set to false to stop server */
	private AtomicBoolean running = new AtomicBoolean();

	/** Communicator handles Kryo server */
	private ServerCommunicator communicator;

	/** Lobby manager */
	private ServerLobbyManager lobbyManager;

	public int instanceNum;
	public static int instanceInc;

	/**
	 * Private constructor enforces singleton pattern. Actual server
	 * initialization occurs when the host client calls init()
	 */
	private GameServer() {
		initialized = false;
		instanceNum = instanceInc;
		instanceInc++;
	}

	/**
	 * Access the GameServer instance using this method.
	 * 
	 * @return the GameServer instance
	 * @throws ServerNotInitializedException
	 */
	public static synchronized GameServer getInstance() {
		return instance;

	}

	/**
	 * Initializes the server using the port provided.
	 * 
	 * @param port
	 *            server port
	 * @throws ServerAlreadyInitializedException
	 */
	public void init(int port) throws ServerAlreadyInitializedException {
		if (initialized) {
			throw new ServerAlreadyInitializedException(port);
		}
		lobbyManager = new ServerLobbyManager();
		// Set lobby state
		setState(GameState.LOBBY);

		// Start running game server
		initialized = true;
		communicator = new ServerCommunicator(port);
		(new Thread(this)).start();
	}

	/**
	 * Start of GameServer thread, contains main server loop
	 */
	@Override
	public void run() {
		// Check if the server is initialized; if not, throw an exception
		try {
			if (!initialized) {
				throw new ServerNotInitializedException();
			}
		} catch (ServerNotInitializedException e) {
			Thread t = Thread.currentThread();
			t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}

		running.set(true);

		// Main server loop
		long prevTime = System.nanoTime();
		while (running.get()) {
			// Update times
			long currTime = System.nanoTime();
			long elapsedTime = currTime - prevTime;
			prevTime = currTime;

			// TODO process buffered client messages
			communicator.readMessages();
			// TODO update game simulation using elapsedTime here

			// Now sleep until the next tick (approximately).
			long timeTaken = System.nanoTime() - prevTime;
			long tickTimeRemaining = (long) (TICKRATE * NANOSECONDS) - timeTaken;
			try {
				Thread.sleep(tickTimeRemaining / MILLISECONDS);
			} catch (InterruptedException e) {
				SingletonGUIConsole.getInstance().log(e.getMessage(), LogLevel.ERROR);
			} catch (IllegalArgumentException e) { // negative number
				SingletonGUIConsole.getInstance().log("Issue with system time resulted in a negative delta t",
						LogLevel.ERROR);
			}
		}
	}

	/**
	 * Stop the server. Its thread will terminate.
	 */
	public void stop() {
		initialized = false;
		running.set(false); // will cause run loop to stop
	}

	/**
	 * Returns whether the GameServer is running
	 * 
	 * @return true if running
	 */
	public boolean isRunning() {
		return running.get();
	}


	/**
	 * Gets the lobby manager
	 * 
	 * @return lobbyManager
	 */
	public ServerLobbyManager getLobbyManager() {
		return lobbyManager;
	}

	/**
	 * Gets the current game state
	 * 
	 * @return state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Sets game state
	 * 
	 * @param state
	 */
	public void setState(GameState state) {
		this.state = state;
	}

	/**
	 * Gets the current game map
	 * 
	 * @return
	 */
	public GameMap getMap() {
		return map;
	}

	/**
	 * Loads and sets the game map
	 * 
	 * @param mapName
	 *            name of .tmx map file
	 * @throws various
	 *             map loading exceptions
	 */
	public void loadMap(String mapName)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, MapLoaderException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		// TODO: Handle rather than throw exceptions
		this.map = mapLoader.loadMap(mapName);
	}

	/**
	 * Gets the current cutscene
	 * 
	 * @return cutscene
	 */
	public Cutscene getCutscene() {
		return cutscene;
	}

	/**
	 * Sets and starts the current cutscene
	 * 
	 * @param cutscene
	 */
	public void loadCutscene(Cutscene cutscene) {
		this.cutscene = cutscene;
		// TODO: start cutscene
	}

	/**
	 * Gets the list of player models.
	 * 
	 * @return list of Player instances
	 */
	public List<Player> getPlayers() {
		return players;
	}

	public ServerCommunicator getCommunicator() {
		return communicator;
	}
}
