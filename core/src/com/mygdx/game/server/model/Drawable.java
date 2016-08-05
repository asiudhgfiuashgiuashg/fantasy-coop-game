package com.mygdx.game.server.model;

/**
 * Implemented by classes which need to be drawn at some position on the client side.
 * @author elimonent
 *
 */
public interface Drawable {
	/**
	 * Call draw() to get a DrawMessage which represents a drawable's view.
	 * @return a DrawMessage to be sent to the client
	 */
	public DrawMessage draw();
}
