package com.mygdx.game.server.model;

/**
 * Represents a cutscene which is acted out incrementally each tick.
 * When a cutscene starts, the GameState is set to CUTSCENE.
 * When a cutscene ends, the GameState is set to GAME
 * Each individual cutscene is a subclass extending this class.
 * The behavior of each cutscene will be specified in its subclass.
 * @author elimonent
 *
 */
public abstract class Cutscene implements Actable {
	protected void end() {
		//set the global GameState back to GAME here.
	}
}
