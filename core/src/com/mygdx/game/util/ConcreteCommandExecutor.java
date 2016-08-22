package com.mygdx.game.util;

import com.mygdx.game.server.model.Server;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.LogLevel;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Public functions in this class are accessible via the developer console.
 * Arguments must be primitive or Strings.
 * No two methods of the same name can have the same number of parameters.
 * Created by elimonent on 8/21/16.
 */
public class ConcreteCommandExecutor extends CommandExecutor {
	public void connect(String ip, int port) {
		console.log("Attempting to connect to" + ip + ":" + port);
		try {
			InetAddress iNetIP = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		}
		//TODO
	}

	/**
	 * Host a server on this computer.
	 * @param port
	 */
	public void startServer(int port) {
		console.log("Starting server on port " + port + " ...");
		Server server = Server.getInstance();

		try {
			server.init(port);
			(new Thread(server)).start();
			console.log("Server started", LogLevel.SUCCESS);
		} catch (ServerAlreadyInitializedException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
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
	}
}
