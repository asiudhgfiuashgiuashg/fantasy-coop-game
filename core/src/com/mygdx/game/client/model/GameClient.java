package com.mygdx.game.client.model;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.client.controller.networklisteners.LobbyListener;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.view.GameScreen;
import com.mygdx.game.client.view.LobbyScreen;
import com.mygdx.game.client.view.MenuScreen;
import com.mygdx.game.server.model.GameMap;
import com.mygdx.game.server.model.MapLoader;
import com.mygdx.game.server.model.Server;
import com.mygdx.game.shared.exceptions.MapLoaderException;
import com.mygdx.game.shared.util.ConcreteCommandExecutor;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;


/**
 * The main game loop for the client application.
 * Delegates rendering to one of three screens.
 * @author elimonent
 *
 */
public class GameClient extends Game {
	public static SingletonGUIConsole console;

	private Client client; //communication with server will be done through this object.
	private static int CONNECT_TIMEOUT = 5000; //timeout for connecting client to server.
	private final List<LobbyListener> lobbyListeners = new ArrayList<LobbyListener>(); //holds listeners that need disposed of when leaving the lobby

	private String username = "PLACEHOLDER"; //client's username
	private ClientLobbyManager lobbyManager;
	private TiledMap clientMap;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setupConsole();
		setScreen(new MenuScreen());
		
		clientMap = new ClientTmxLoader().load("validMap.tmx");

	}

	@Override
	public void render () {
		super.render();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		console.draw();

		batch.begin();
		// do the drawing here
		// for example, batch.draw(textureregion, x, y);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
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
	 */
	public void setupClientAndConnect(String ip, int tcpPort, String username) throws AlreadyConnectedException {
		if (null != client && client.isConnected() == true) {
			throw new AlreadyConnectedException();
		}
		this.username = username;
		client = new Client();
		Kryo kryo = client.getKryo();
		kryo.setRegistrationRequired(false); //automatic registration of objects in kryo (which enables them to be serialized/deserialized)
		lobbyManager = new ClientLobbyManager(username, client);
		registerKryoLobbyListeners();
		client.start();
		try {
			client.connect(CONNECT_TIMEOUT, ip, tcpPort);
			console.log("Connected to server", LogLevel.SUCCESS);
			setScreen(new LobbyScreen());
			lobbyManager.sendUsername();
		} catch (IOException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		}
	}

	/**
	 * disconnect from the server and perform the accompanying display changes
	 */
	public void disconnect() {
		client.close();
		setScreen(new MenuScreen());
		SingletonGUIConsole.getInstance().log("Intentionally disconnected from server", LogLevel.SUCCESS);
	}

	/**
	 * Override to automatically dispose of previous screen.
	 * @param screen
	 */
	@Override
	public void setScreen(Screen screen) {
		if (getScreen() != null) {
			getScreen().dispose();
		}
		super.setScreen(screen);
	}

	/**
	 * These listeners will react to server messages
	 */
	private void registerKryoLobbyListeners() {
		LobbyListener lobbyListener = new LobbyListener(lobbyManager, this);
		console.log("Added lobby listener");
		client.addListener(lobbyListener);
		lobbyListeners.add(lobbyListener); //keep track of this listener so we can destroy it later

	}

	public void removeKryoLobbyListeners() {
		for (Listener listener: lobbyListeners) {
			client.removeListener(listener);
		}
	}

	public boolean isConnected() {
		return !(null == client) && client.isConnected();
	}

	public Client getClient() {
		return client;
	}

	public ClientLobbyManager getLobbyManager() {
		return lobbyManager;
	}

	public void transitionToInGame() {
		setScreen(new GameScreen());
		console.log("Transitioned to in-game from lobby");
	}
}
