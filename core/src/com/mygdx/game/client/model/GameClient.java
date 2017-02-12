package com.mygdx.game.client.model;

import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.map.ClientTiledMap;
import com.mygdx.game.client.model.map.ClientTmxLoader;
import com.mygdx.game.client.view.CustomTiledMapRenderer;
import com.mygdx.game.client.view.screen.DebuggableScreen;
import com.mygdx.game.client.view.screen.GameScreen;
import com.mygdx.game.client.view.screen.LobbyScreen;
import com.mygdx.game.client.view.screen.MenuScreen;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.shared.model.PreferencesConstants;
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
 * Singleton.
 *
 */
public class GameClient extends Game {
	public static SingletonGUIConsole console;

	private static GameClient instance;

	private ClientCommunicator communicator;

	private ClientLobbyManager lobbyManager;
	private ClientTiledMap clientMap;

	private SpriteBatch batch;

	public static final int WORLD_HEIGHT = 240;
	
	private StretchViewport gameViewport;
	private StretchViewport uiViewport;
	
	private int screenWidth;
	private int screenHeight;
	private float aspectRatio;
	private boolean fullscreen;
	
	private MenuScreen menuScreen;
	private LobbyScreen lobbyScreen;
	private GameScreen gameScreen;

	private InputMultiplexer inputMultiplexer;

	/** renders the map */
	private CustomTiledMapRenderer renderer;

	private RayHandler rayHandler;

	private static final float MAP_SCALE = 2f; // how much to scale polygons,
	// tiles, etc. ex) A scale of 2.0 means that every pixel in a loaded image
	// will take up 2 pixels in the game window.

	@Override
	public void create() {
		// Load screen resolution preferences
		Preferences prefs = Gdx.app.getPreferences(PreferencesConstants.RESOLUTION_PREFS);
		fullscreen = prefs.getBoolean(PreferencesConstants.FULLSCREEN, PreferencesConstants.FULLSCREEN_DEFAULT);
		screenWidth = prefs.getInteger(PreferencesConstants.SCREEN_WIDTH,
				PreferencesConstants.SCREEN_WIDTH_DEFAULT);
		screenHeight = prefs.getInteger(PreferencesConstants.SCREEN_HEIGHT,
				PreferencesConstants.SCREEN_HEIGHT_DEFAULT);

		// Apply screen resolution, fullscreen/windowed mode
		setFullscreen(fullscreen);

		// Set singleton instance
		instance = this;

		setupConsole();

		batch = new SpriteBatch();
		communicator = new ClientCommunicator();
		lobbyManager = new ClientLobbyManager();

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(console.getInputProcessor());
		// TODO: inputMultiplexer.addProcessor(keyboardProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);

		rayHandler = new RayHandler(new World(new Vector2(0, 0), false));
		// box2d lights need a rayhandler to be instantiated, so that's why
		// we pass rayHandler to the loader.

		clientMap = new ClientTmxLoader().load("prototypeMap.tmx", rayHandler);
		renderer = new CustomTiledMapRenderer(clientMap, MAP_SCALE, batch, rayHandler);

		aspectRatio = screenWidth / (float) screenHeight;

		gameViewport = new StretchViewport(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
		uiViewport = new StretchViewport(screenWidth, screenHeight);
		
		menuScreen = new MenuScreen(uiViewport, batch);
		lobbyScreen = new LobbyScreen(uiViewport, batch);
		gameScreen = new GameScreen(gameViewport, uiViewport, batch, rayHandler, renderer);
		setScreen(menuScreen);
	}

	public float getAspectRatio() {
		return aspectRatio;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		console.draw();

		// temporary
		communicator.readMessages();
	}

	@Override
	public void resize(int width, int height) {
		uiViewport.update(width, height, true);
		gameViewport.update(width, height);
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

		setScreen(lobbyScreen);
	}

	public void connect(String ip, int tcpPort, String username) throws AlreadyConnectedException {
		communicator.connect(ip, tcpPort, username);
	}

	/**
	 * disconnect from the server and perform the accompanying display changes
	 */
	public void disconnect() {
		setScreen(menuScreen);
		SingletonGUIConsole.getInstance().log("Intentionally disconnected from server", LogLevel.SUCCESS);
	}

	public void sendToServer(Message msg) {
		communicator.sendToServer(msg);
	}

	/**
	 * Switches input processor to the new screen's stage.
	 * 
	 * @param screen
	 */
	@Override
	public void setScreen(Screen screen) {
		DebuggableScreen current = (DebuggableScreen) this.screen;
		if (current != null) inputMultiplexer.removeProcessor(current.getStage());
		super.setScreen(screen);
		DebuggableScreen next = (DebuggableScreen) screen;
		inputMultiplexer.addProcessor(next.getStage());
	}

	public boolean isConnected() {
		return communicator.isConnected();
	}

	public ClientLobbyManager getLobbyManager() {
		return lobbyManager;
	}

	public void transitionToInGame() {
		setScreen(gameScreen);
		console.log("Transitioned to in-game from lobby");
	}

	@Override
	public DebuggableScreen getScreen() {
		return (DebuggableScreen) super.getScreen();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	/**
	 * add a dynamic entity to the map and register it with the renderer so it
	 * will be rendered
	 * 
	 * @param newEntity
	 */
	public void addDynamicEntity(DynamicEntity newEntity) {
		clientMap.dynamicEntities.add(newEntity);
		renderer.registerDynamicEntity(newEntity);
	}

	public ClientTiledMap getMap() {
		return clientMap;
	}

	public void setResolution(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		aspectRatio = width / (float) height;

		uiViewport.setWorldWidth(width);
		uiViewport.setWorldHeight(height);
		
		gameViewport.setWorldWidth(WORLD_HEIGHT * aspectRatio);
		
		setFullscreen(fullscreen);
	}
	
	public void setFullscreen(boolean full) {
		fullscreen = full;
		if (full) {
			DisplayMode displayMode = Gdx.graphics.getDisplayMode();
			Gdx.graphics.setFullscreenMode(displayMode);
		} else {
			Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
		}
	}
}
