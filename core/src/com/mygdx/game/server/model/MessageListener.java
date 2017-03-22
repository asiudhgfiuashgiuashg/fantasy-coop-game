package com.mygdx.game.server.model;


public interface MessageListener <T extends EventMessage> {
	/**
	 * Act on this message
	 * @param message passed to the listener by the pipeline
	 */
	public void notify(T message);
}
