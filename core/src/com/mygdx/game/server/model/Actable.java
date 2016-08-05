package com.mygdx.game.server.model;

/**
 * @author elimonent
 * Implemented by things which act()
 */
public interface Actable {
	/**
	 * Specify an Actable's behavior in this function.
	 * act() will be called every tick.
	 */
	public void act();
}
