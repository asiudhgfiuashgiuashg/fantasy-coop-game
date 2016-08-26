package com.mygdx.game.client.model.exceptions;

/**
 * Created by elimonent on 8/26/16.
 */
public class AlreadyConnectedException extends Exception {

	@Override
	public String getMessage() {
		return "Client already connected to a server!";
	}
}
