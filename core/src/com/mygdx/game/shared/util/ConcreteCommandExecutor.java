package com.mygdx.game.shared.util;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.server.controller.ServerCommunicator;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.mygdx.game.shared.network.LobbyMessage.ClassAssignmentMessage;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.LogLevel;


import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;


/**
 * Public functions in this class are accessible via the developer console.
 * Arguments must be primitive or Strings.
 * No two methods of the same name can have the same number of parameters.
 * Created by elimonent on 8/21/16.
 */
public class ConcreteCommandExecutor extends CommandExecutor {
	private GameClient gameClient; //need this reference to order the gameClient to
									//  do things like connect to the server or change screens based on console commands

	private boolean serverStarted = false; //used to restrict usage of methods applying to the



	public ConcreteCommandExecutor(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public void connect(String ip, int port, String username) {
		console.log("Attempting to connect to " + ip + ":" + port);
		try {
			gameClient.connect(ip, port, username);

		} catch (AlreadyConnectedException e) {
			console.log("Already connected to a server!", LogLevel.ERROR);
		}
	}

	/**
	 * Host a server on this computer, on a separate thread.
	 * @param port
	 * @param username the username to connect to your server with
	 */
	public void startServer(int port, String username) {
		console.log("Starting server on port " + port + " ...");

		try {
			gameClient.hostServer(port, username);
		} catch (ServerAlreadyInitializedException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		} catch (AlreadyConnectedException e) {
			console.log(e.getMessage() + "--" + "You can't start a server when you are \n already connected to one!", LogLevel.ERROR);
		}
	}

	/**
	 * Host a server on this computer listening on the default port.
	 */
	public void startServer(String username) {
		serverStarted = true; // BAD fix this
		startServer(ServerCommunicator.DEFAULT_PORT, username);
	}

	/**
	 * Stop the server running on this computer.
	 */
	public void stopServer() {
		console.log("Stopping server");
		GameServer.getInstance().stop();
		serverStarted = false;
	}

	/**
	 * Disconnect the client from a server.
	 */
	public void disconnect() {
		console.log("Disconnecting from server");
		gameClient.disconnect();
	}

	/**
	 * list the clients connected to server
	 */
	public void serverListPlayers() {
		if (serverStarted) {
			console.log("Connected clients: ");
			for (ServerLobbyPlayer lobbyPlayer: GameServer.getInstance().getLobbyManager().getLobbyPlayers()) {
				console.log(lobbyPlayer.toString());
			}
		} else {
			console.log("No server running", LogLevel.ERROR);
		}
	}

	public void currentScreen() {
		console.log("Current screen: " + gameClient.getScreen().getClass());
	}

	/**
	 * request a class from the server
	 * @param className
	 */
	public void requestClass(String className) {
		PlayerClass playerClass = PlayerClass.valueOf(className);
		try {
			ClassAssignmentMessage msg = new ClassAssignmentMessage();
			msg.playerClass = playerClass;
			gameClient.sendToServer(msg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * list players that client knows are in lobby
	 */
	public void clientListPlayers() {
		for (ClientLobbyPlayer lobbyPlayer : gameClient.getLobbyManager().getLobbyPlayers()) {
			console.log(lobbyPlayer.toString());
		}
	}

	/**
	 * send ready message to server
	 * and lobby update view
	 */
	public void ready() {
		gameClient.getLobbyManager().setReady(true);
	}

	/**
	 * send unready message to server
	 * and update lobby view
	 */
	public void unready() {
		gameClient.getLobbyManager().setReady(false);
	}

	/**
	 * send a chat message
	 * @param msg the message to send
	 */
	public void message(String msg) {
		ChatMessage chatMsg = new ChatMessage(msg);
		gameClient.sendToServer(chatMsg);
	}

	/**
	 * toggle rendering debug stuff
	 */
	public void debug() {
		gameClient.getScreen().toggleDebug();
	}
}
