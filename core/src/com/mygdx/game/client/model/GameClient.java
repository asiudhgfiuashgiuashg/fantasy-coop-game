package com.mygdx.game.client.model;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.view.CustomTiledMapRenderer;
import com.mygdx.game.client.view.GameScreen;
import com.mygdx.game.client.view.MenuScreen;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.shared.network.Message;
import com.mygdx.game.shared.util.ConcreteCommandExecutor;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;
import com.mygdx.game.client.controller.ClientCommunicator;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;

/**
 * The main game loop for the client application. Delegates rendering to one of
 * three screens.
 * 
 * @author elimonent
 *
 */
public class GameClient extends Game {
	public static SingletonGUIConsole console;

	private static GameClient instance;

	private ClientCommunicator communicator;

	private ClientLobbyManager lobbyManager;
	private TiledMap clientMap;
	public SpriteBatch batch;
	private CustomTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private static final float MAP_SCALE = 4f; // how much to scale polygons,
	// tiles, etc. ex) A scale of 2.0 means that every pixel in a loaded image
	// will take up 2 pixels in the game window.

	@Override
	public void create() {
		instance = this;
		
		batch = new SpriteBatch();
		
		setupConsole();
		setScreen(new MenuScreen(this));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.update();

		communicator = new ClientCommunicator();
		lobbyManager = new ClientLobbyManager();

		RayHandler rayHandler = new RayHandler(new World(new Vector2(0, 0), false));
		// box2d lights need a rayhandler to be instantiated, so that's why
		// we pass rayHandler to the loader.

		clientMap = new ClientTmxLoader().load("prototypeMap.tmx", rayHandler);
		// clientMap = new ClientTmxLoader().load("validMap.tmx");
		renderer = new CustomTiledMapRenderer(clientMap, MAP_SCALE, rayHandler);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		batch.begin();
		batch.end();
		console.draw();

		// temporary
		communicator.readMessages();
		communicator.sendMessages();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		console.refresh();
	}

	public static GameClient getInstance() {
		return instance;
	}

	private void setupConsole() {
		console = SingletonGUIConsole.getInstance();
		console.setCommandExecutor(new ConcreteCommandExecutor(this));
		console.setPositionPercent(0, 55);
		console.setSizePercent(100, 45);
	}

	/**
	 * connect to the server and enter the lobby screen
	 * 
	 * @throws ServerAlreadyInitializedException
	 */
	public void hostServer(int tcpPort, String username)
			throws AlreadyConnectedException, ServerAlreadyInitializedException {
		communicator.host(tcpPort);
		connect(ClientCommunicator.LOCAL_HOST, tcpPort, username);
	}

	public void connect(String ip, int tcpPort, String username) throws AlreadyConnectedException {
		communicator.connect(ip, tcpPort, username);
	}

	/**
	 * disconnect from the server and perform the accompanying display changes
	 */
	public void disconnect() {
		setScreen(new MenuScreen(this));
		SingletonGUIConsole.getInstance().log("Intentionally disconnected from server", LogLevel.SUCCESS);
	}

	public void queueMessage(Message msg) {
		communicator.queueMessage(msg);
	}

	/**
	 * Override to automatically dispose of previous screen.
	 * 
	 * @param screen
	 */
	@Override
	public void setScreen(Screen screen) {
		if (getScreen() != null) {
			getScreen().dispose();
		}
		super.setScreen(screen);
	}

	public boolean isConnected() {
		return communicator.isConnected();
	}

	public ClientLobbyManager getLobbyManager() {
		return lobbyManager;
	}

	public void transitionToInGame() {
		setScreen(new GameScreen(this));
		console.log("Transitioned to in-game from lobby");
	}

	public CustomTiledMapRenderer getRenderer() {
		return renderer;
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
