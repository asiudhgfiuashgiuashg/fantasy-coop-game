package com.mygdx.game.server.model;


/**
 * an observer for a certain type of event message
 * @param <T>
 */
public interface MessageListener <T extends EventMessage> {
	/**
	 * Act on this message
	 * @param message passed to the listener by the pipeline
	 */
	public void notify(T message);
}
