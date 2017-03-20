package com.mygdx.game.client.model;

import box2dLight.RayHandler;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
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
import com.mygdx.game.client.view.renderer.CustomTiledMapRenderer;
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
import com.mygdx.game.client.controller.KeyboardProcessor;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;
import com.mygdx.game.client.model.keybinds.Keybinds;
import com.mygdx.game.client.model.keybinds.Keybinds.Input;

/**
 * The main game loop for the client application. Delegates rendering to one of
 * three screens.
 *
 * Singleton.
 *
 */
public class GameClient extends Game {
	/**
	 * The guaranteed height of the game world to be shown. Invariant under
	 * different resolutions.
	 */
	public static final int WORLD_HEIGHT = 180;

	/** Console for logging and commands */
	public static SingletonGUIConsole console;
	/** Singleton instance */
	private static GameClient instance;

	/** Communicator handles network messages */
	private ClientCommunicator communicator;
	/** Manages lobby functionality */
	private ClientLobbyManager lobbyManager;
	/** Game map, loaded from .tmx file */
	private ClientTiledMap clientMap;
	/** The one and only spritebatch for rendering */
	private SpriteBatch batch;

	/** Viewport for game world. Maps game coords to screen coords */
	private StretchViewport gameViewport;
	/** Viewport for all UI */
	private StretchViewport uiViewport;

	/** Resolution and fullscreen setting chosen by user */
	private int screenWidth;
	private int screenHeight;
	private float aspectRatio;
	private boolean fullscreen;

	/** Screens */
	private MenuScreen menuScreen;
	private LobbyScreen lobbyScreen;
	private GameScreen gameScreen;

	/** Multiplexer for input processors */
	private InputMultiplexer inputMultiplexer;
	
	/** Keyboard input processor */
	private KeyboardProcessor keyboardProcessor;
	
	/** User keybinds */
	private Keybinds keybinds;
	
	/** Renders the game map and everything in it */
	private CustomTiledMapRenderer renderer;
	/** Box2d lights handler */
	private RayHandler rayHandler;

	@Override
	public void create() {
		// Load screen resolution preferences
		Preferences prefs = Gdx.app.getPreferences(PreferencesConstants.RESOLUTION_PREFS);
		fullscreen = prefs.getBoolean(PreferencesConstants.FULLSCREEN, PreferencesConstants.FULLSCREEN_DEFAULT);
		screenWidth = prefs.getInteger(PreferencesConstants.SCREEN_WIDTH, PreferencesConstants.SCREEN_WIDTH_DEFAULT);
		screenHeight = prefs.getInteger(PreferencesConstants.SCREEN_HEIGHT, PreferencesConstants.SCREEN_HEIGHT_DEFAULT);
		aspectRatio = screenWidth / (float) screenHeight;
		
		// Apply screen resolution, fullscreen/windowed mode
		setFullscreen(fullscreen);

		// Set singleton instance
		instance = this;

		setupConsole();

		batch = new SpriteBatch();
		communicator = new ClientCommunicator();
		lobbyManager = new ClientLobbyManager();

		// Set up input processors
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(console.getInputProcessor());
		keybinds = new Keybinds();
		keyboardProcessor = new KeyboardProcessor(keybinds);
		inputMultiplexer.addProcessor(keyboardProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);

		// box2d lights handler
		rayHandler = new RayHandler(new World(new Vector2(0, 0), false));

		// box2d lights need a rayhandler to be instantiated, so that's why
		// we pass rayHandler to the loader.
		clientMap = new ClientTmxLoader().load("prototypeMap.tmx", rayHandler);
		renderer = new CustomTiledMapRenderer(clientMap, batch, rayHandler);

		// Initialize viewports
		gameViewport = new StretchViewport(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
		uiViewport = new StretchViewport(screenWidth, screenHeight);

		// ... and screens
		menuScreen = new MenuScreen(uiViewport, batch);
		lobbyScreen = new LobbyScreen(uiViewport, batch);
		gameScreen = new GameScreen(gameViewport, uiViewport, batch, renderer);
		
		// Start out in menu
		setScreen(menuScreen);		
	}

	/**
	 * Gets the singleton instance
	 * 
	 * @return instance
	 */
	public static GameClient getInstance() {
		return instance;
	}

	/**
	 * Gets the aspect ratio of user chosen resolution
	 * 
	 * @return aspect ratio
	 */
	public float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * Gets the screen width of user chosen resolution. If in full screen mode,
	 * not necessarily equal to Gdx.graphics.getWidth()
	 * 
	 * @return width in pixels
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Gets the screen height of user chosen resolution. If in full screen mode,
	 * not necessarily equal to Gdx.graphics.getHeight()
	 * 
	 * @return height in pixels
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Sets the user's resolution. In windowed mode, this will become the
	 * resolution of the window. In fullscreen mode, this resolution will be
	 * stretched to fit the actual screen.
	 * 
	 * @param width
	 *            in pixels
	 * @param height
	 *            in pixels
	 */
	public void setResolution(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		aspectRatio = width / (float) height;

		uiViewport.setWorldWidth(width);
		uiViewport.setWorldHeight(height);

		gameViewport.setWorldWidth(WORLD_HEIGHT * aspectRatio);

		setFullscreen(fullscreen);
	}

	/**
	 * Sets the fullscreen / windowed mode of the application and applies the
	 * current screen width and height. Gdx methods call GameClient.resize()
	 * 
	 * @param full
	 *            true for full screen, false for windowed
	 */
	public void setFullscreen(boolean full) {
		fullscreen = full;
		if (full) {
			DisplayMode displayMode = Gdx.graphics.getDisplayMode();
			Gdx.graphics.setFullscreenMode(displayMode);
		} else {
			Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
		}
	}

	/**
	 * Switches input processor to the new screen's stage.
	 * 
	 * @param screen
	 */
	@Override
	public void setScreen(Screen screen) {
		DebuggableScreen current = (DebuggableScreen) this.screen;
		if (current != null)
			inputMultiplexer.removeProcessor(current.getStage());
		super.setScreen(screen);
		DebuggableScreen next = (DebuggableScreen) screen;
		inputMultiplexer.addProcessor(next.getStage());
	}

	/**
	 * Called every tick. Delegates rendering to current screen's render()
	 */
	@Override
	public void render() {
		// Sets background fill color
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Calls screen.render()
		super.render();
		console.draw();

		// temporary
		communicator.readMessages();
	}

	/**
	 * Called when resolution change is applied. Updates viewport mappings from
	 * world coords to screen coords. In the case of the UI viewport, the
	 * "world" coords are simply the width and height provided. The boolean
	 * argument to the UI viewport's update tells the camera to center after
	 * applying the new dimensions.
	 * 
	 * @param width
	 *            resolution width in pixels
	 * @param height
	 *            resolution height in pixels
	 */
	@Override
	public void resize(int width, int height) {
		uiViewport.update(width, height, true);
		gameViewport.update(width, height);
		console.refresh();
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
			throws AlreadyConnectedException, ServerAlreadyInitializedException, IOException {
		communicator.host(tcpPort);
		connect(ClientCommunicator.LOCAL_HOST, tcpPort, username);

		setScreen(lobbyScreen);
	}

	public void connect(String ip, int tcpPort, String username) throws AlreadyConnectedException, IOException {
		communicator.connect(ip, tcpPort, username);
		
		setScreen(lobbyScreen);
	}

	/**
	 * disconnect from the server and perform the accompanying display changes
	 */
	public void disconnect() {
		setScreen(menuScreen);
		communicator.disconnect();
		SingletonGUIConsole.getInstance().log("Intentionally disconnected from server", LogLevel.SUCCESS);
	}

	public void sendToServer(Message msg) {
		communicator.sendToServer(msg);
	}

	public boolean isConnected() {
		return communicator.isConnected();
	}

	public ClientLobbyManager getLobbyManager() {
		return lobbyManager;
	}

	public void showLobbyScreen() {
		setScreen(lobbyScreen);
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
	
	public Keybinds getKeybinds() {
		return keybinds;
	}
}
