package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.server.controller.ServerCommunicator;
import com.mygdx.game.server.model.entity.DynamicEntity;
import com.mygdx.game.server.model.entity.player.MagePlayer;
import com.mygdx.game.server.model.entity.player.Player;
import com.mygdx.game.server.model.entity.player.Ranger.RangerPlayer;
import com.mygdx.game.server.model.entity.player.ShieldPlayer;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.server.model.exceptions.ServerNotInitializedException;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.model.UniqueIDAssigner;
import com.mygdx.game.shared.model.exceptions.MapLoaderException;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.GameMessage;
import com.mygdx.game.shared.network.LobbyMessage;
import com.mygdx.game.shared.network.Message;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The GameServer singleton acts as a hub for the model that manages the in-game
 * server data and updates the model. First, it processes client messages from
 * the buffer, then it calls the act() method on all Actables stored in the
 * current GameMap and sends any responses to clients.
 * <p>
 * The GameServer runs on its own thread and constructs the ServerCommunicator
 * which initializes the KryoNet networking server which also runs on its own
 * thread. The GameServer is created by a client who chooses to host a game.
 *
 * @author elimonent
 * @author Sawyer Harris
 */
public class GameServer implements Runnable {
	/**
	 * Tickrate of game server i.e. how often the model is updated
	 */
	public static final float TICKRATE = .02f;

	/**
	 * Time conversions
	 */
	private static final float NANOSECONDS = 1000000000;
	private static final long MILLISECONDS = 1000;

	/**
	 * Singleton instance
	 */
	private static GameServer instance = new GameServer();

	/**
	 * The current game state
	 */
	private GameState state;

	private final ServerTmxLoader mapLoader = new ServerTmxLoader();

	/**
	 * The current in-game map
	 */
	private GameMap map;

	/**
	 * The current cutscene being played
	 */
	private Cutscene cutscene;


	/**
	 * Whether the server has been initialized
	 */
	private boolean initialized;

	/**
	 * Whether the server is running. Set to false to stop server
	 */
	private AtomicBoolean running = new AtomicBoolean();

	/**
	 * Communicator handles Kryo server
	 */
	private ServerCommunicator communicator;

	/**
	 * Lobby manager
	 */
	private ServerLobbyManager lobbyManager;


	private boolean flase = false;

	/**
	 * Private constructor enforces singleton pattern. Actual server
	 * initialization occurs when the host client calls init()
	 */
	private GameServer() {
		initialized = false;
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
	 * @param port server port
	 * @throws ServerAlreadyInitializedException
	 */
	public void init(int port, String mapName) throws
			ServerAlreadyInitializedException, IllegalAccessException,
			InstantiationException, IOException, NoSuchMethodException,
			InvocationTargetException, ClassNotFoundException {
		if (initialized) {
			throw new ServerAlreadyInitializedException(port);
		}
		lobbyManager = new ServerLobbyManager();
		// Set lobby state
		setState(GameState.LOBBY);

		// Start running game server
		initialized = true;
		communicator = new ServerCommunicator(port);
		loadMap(mapName);
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
		long prevTime = TimeUtils.millis();
		while (running.get()) {
			// Update times
			long currTime = TimeUtils.millis();
			long elapsedTime = TimeUtils.timeSinceMillis(prevTime);
			prevTime = currTime;

			// TODO process buffered client messages
			communicator.readMessages();
			// TODO update game simulation using elapsedTime here
			/* if in-game (not in lobby or paused etc) */
			if (GameState.GAME == state) {
				for (DynamicEntity entity : map.getDynamicEntities()) {
					entity.act(elapsedTime);
				}
			}

			// Now sleep until the next tick (approximately).
			long timeTaken = TimeUtils.millis() - prevTime;
			long tickTimeRemaining = (long) (TICKRATE * MILLISECONDS) -
					timeTaken;
			try {
				Thread.sleep(tickTimeRemaining);
			} catch (InterruptedException e) {
				SingletonGUIConsole.getInstance().log(e.getMessage(), LogLevel
						.ERROR);
			} catch (IllegalArgumentException e) { // negative number
				SingletonGUIConsole.getInstance().log("Issue with system time " +
						"" + "" + "resulted in a negative delta t", LogLevel
						.ERROR);
			}
		}
		communicator.cleanup();
		SingletonGUIConsole.getInstance().log("Stopped Server", LogLevel.SUCCESS);
	}

	/**
	 * change the GameState and do other things that need to be done when
	 * transitioning from lobby to in-game
	 */
	public void startGame() {
		setState(GameState.GAME);
		createPlayerInstances();
		communicator.sendToAll(new LobbyMessage.GameStartMessage());
		initDynamicEntitiesOnClients();
	}

	private void createPlayerInstances() {
		ServerLobbyManager manager = getLobbyManager();
		List<ServerLobbyPlayer> lobbyPlayers = manager.getLobbyPlayers();
		for (ServerLobbyPlayer lobbyPlayer: lobbyPlayers) {
			PlayerClass clazz = lobbyPlayer.getPlayerClass();
			String uid;
			Vector2 position = new Vector2();
			getMap().playerSpawn.getPosition(position);
			Player player;
			if (PlayerClass.MAGE == clazz) {
				// Generate uid
				uid = UniqueIDAssigner.generateDynamicEntityUID(MagePlayer.class.getSimpleName());
				player = new MagePlayer(uid, position, 0, flase);
			} else if (PlayerClass.RANGER == clazz) {
				uid = UniqueIDAssigner.generateDynamicEntityUID(RangerPlayer.class.getSimpleName());
				player = new RangerPlayer(uid, position, 0, flase);
			} else { // SHIELD
				uid = UniqueIDAssigner.generateDynamicEntityUID(ShieldPlayer.class.getSimpleName());
				player = new ShieldPlayer(uid, position, 0, flase);
			}
			player.connectionUid = lobbyPlayer.getUid();
			getMap().addPlayer(player);
		}
	}

	/**
	 * when the game starts, tell the clients what dynamic entities they
	 * should intantiate/spawn initially
	 * <p>
	 * right now, every dynamic entity on the map will be instantiated, but
	 * in the future we may want to only tell clients about nearby dynamic
	 * entities
	 */
	private void initDynamicEntitiesOnClients() {
		for (DynamicEntity entity : map.getDynamicEntities()) {
			GameMessage.InitDynamicEntityMsg entInitMsg = getDynamicEntityInitMsg(entity);

			if (entity == getMap().magePlayer || entity == getMap().rangerPlayer || entity == getMap().shieldPlayer) {
				entInitMsg.isLocalPlayer = true;
				Player player = (Player) entity;
				sendTo(entInitMsg, player.connectionUid);
				entInitMsg.isLocalPlayer = false;
				sendToAllExcept(entInitMsg, player.connectionUid);
			} else {
				communicator.sendToAll(entInitMsg);
			}

		}
	}


	public static GameMessage.InitDynamicEntityMsg getDynamicEntityInitMsg(DynamicEntity entity) {
		GameMessage.InitDynamicEntityMsg entInitMsg = new GameMessage
				.InitDynamicEntityMsg();
		entInitMsg.className = entity.getClass().getSimpleName();
		entInitMsg.pos = entity.getPosition();
		entInitMsg.entUid = entity.getUid();
		entInitMsg.vertices = entity.getVertices();
		entInitMsg.visLayer = entity.getVisLayer();
		entInitMsg.mass = entity.getMass();
		entInitMsg.entityLightList = entity.lights;
		entInitMsg.rotation = entity.getRotation();
		entInitMsg.originX = entity.getOriginX();
		entInitMsg.originY = entity.getOriginY();

		return entInitMsg;
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
	 * @param mapName name of .tmx map file
	 * @throws various map loading exceptions
	 */
	public void loadMap(String mapName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			MapLoaderException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
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
	 * send a message to everyone
	 * @param msg
	 */
	public void sendToAll(Message msg) {
		communicator.sendToAll(msg);
	}

	/**
	 * send a message to everyone except a certain uid
	 * @param msg
	 * @param exceptId
	 */
	public void sendToAllExcept(Message msg, int exceptId) {
		communicator.sendToAllExcept(msg, exceptId);
	}

	/**
	 * send a message to a single connection
	 * @param msg
	 * @param recipientId
	 */
	public void sendTo(Message msg, int recipientId) {
		communicator.sendTo(msg, recipientId);
	}
}
