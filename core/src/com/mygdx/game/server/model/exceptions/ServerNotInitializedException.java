package com.mygdx.game.server.model.exceptions;

/**
 * Created by elimonent on 8/21/16.
 */
public class ServerNotInitializedException extends Throwable {

	@Override
	public String getMessage() {
		return "Server not initialized! Please initialize the server with a port";
	}
}
