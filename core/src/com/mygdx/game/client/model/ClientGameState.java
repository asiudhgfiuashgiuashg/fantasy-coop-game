package com.mygdx.game.client.model;

/**
 * The state of the game
 * LOBBY - players preparing for the game to start (choosing classes, waiting for players)
 * GAME - players are actively playing the game. The game world is simulated each tick.
 * CUTSCENE - players are viewing a cutscene. The game world simulation is paused.
 * PAUSED - the game is paused.
 * MAIN_MENU - in the main menu
 * @author elimonent
 *
 */
public enum ClientGameState {
	LOBBY,
	GAME,
	CUTSCENE,
	PAUSED,
	MAIN_MENU,
}
