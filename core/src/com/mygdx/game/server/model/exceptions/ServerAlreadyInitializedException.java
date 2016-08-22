package com.mygdx.game.server.model.exceptions;

/**
 * Throw this exception if the Server singleton was already initialized when init() was called.
 * Created by elimonent on 8/21/16.
 */
public class ServerAlreadyInitializedException extends Exception {
	private int port; //The port that the server was already running on

	/**
	 *
	 * @param port The port that the server was already running on
	 */
	public ServerAlreadyInitializedException(int port) {
		this.port = port;
	}

	@Override
	public String getMessage() {
		return "Initialization failed. Server already initialized on port " + port + ".";
	}

	public int getPort() {
		return port;
	}
}
