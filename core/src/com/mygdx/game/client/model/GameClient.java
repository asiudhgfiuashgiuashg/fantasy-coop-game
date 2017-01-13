package com.mygdx.game.client.model;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
	private CustomTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;

	private static final float MAP_SCALE = 4f; // how much to scale polygons,
	// tiles, etc. ex) A scale of 2.0 means that every pixel in a loaded image
	// will take up 2 pixels in the game window.

	@Override
	public void create() {
		instance = this;

		setupConsole();
		setScreen(new MenuScreen());

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.update();

		communicator = new ClientCommunicator();		
		lobbyManager = new ClientLobbyManager();

		clientMap = new ClientTmxLoader().load("prototypeMap.tmx");
		// clientMap = new ClientTmxLoader().load("validMap.tmx");
		renderer = new CustomTiledMapRenderer(clientMap, MAP_SCALE);

	}

	@Override
	public void render() {
		super.render();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// do the drawing here
		// for example, batch.draw(textureregion, x, y);
		renderer.setView(camera);
		renderer.render();
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
		communicator.disconnect();
		setScreen(new MenuScreen());
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
		setScreen(new GameScreen());
		console.log("Transitioned to in-game from lobby");
	}
}
