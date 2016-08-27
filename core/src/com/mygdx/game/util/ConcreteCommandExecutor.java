package com.mygdx.game.util;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.client.GameClient;
import com.mygdx.game.server.model.Server;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.server.model.lobby.PlayerClassEnum;
import com.mygdx.game.util.network.messages.SelectClassMessage;
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

	public void connect(String ip, int port) {
		console.log("Attempting to connect to " + ip + ":" + port);
		try {
			gameClient.setupClientAndConnect(ip, port);

		} catch (AlreadyConnectedException e) {
			console.log("Already connected to a server!", LogLevel.ERROR);
		}
	}

	/**
	 * Host a server on this computer, on a separate thread.
	 * @param port
	 */
	public void startServer(int port) {
		console.log("Starting server on port " + port + " ...");
		Server server = Server.getInstance();

		try {
			server.init(port, gameClient);
			serverStarted = true;
		} catch (ServerAlreadyInitializedException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		} catch (AlreadyConnectedException e) {
			console.log(e.getMessage() + "--" + "You can't start a server when you are \n already connected to one!", LogLevel.ERROR);
		}
	}

	/**
	 * Host a server on this computer listening on the default port.
	 */
	public void startServer() {
		startServer(Server.DEFAULT_PORT);
	}

	/**
	 * Stop the server running on this computer.
	 */
	public void stopServer() {
		console.log("Stopping server");
		Server.getInstance().stop();
		serverStarted = false;
	}

	/**
	 * Disconnect the client from a server.
	 */
	public void disconnect() {
		console.log("Disconnecting from server");
		gameClient.disconnect();
	}


	public void listConnectedClients() {
		if (serverStarted) {
			console.log("Connected clients: ");
			for (Connection connection : Server.getInstance().getServer().getConnections()) {
				console.log(connection.toString()
						+ " " + connection.getRemoteAddressTCP());
			}
		} else {
			console.log("No server running", LogLevel.ERROR);
		}
	}

	public void currentScreen() {
		console.log("Current screen: " + gameClient.getScreen().getClass());
	}

	public void requestClass(String className) {
		PlayerClassEnum playerClass = PlayerClassEnum.valueOf(className);
		try {
			gameClient.getClient().sendTCP(new SelectClassMessage(playerClass));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
