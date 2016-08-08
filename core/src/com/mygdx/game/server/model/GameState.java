package com.mygdx.game.server.model;

/**
 * The state of the game
 * LOBBY - players preparing for the game to start (choosing classes, waiting for players)
 * GAME - players are actively playing the game. The game world is simulated each tick.
 * CUTSCENE - players are viewing a cutscene. The game world simulation is paused.
 * PAUSED - the game is paused.
 * @author elimonent
 *
 */
public enum GameState {
	LOBBY,
	GAME,
	CUTSCENE,
	PAUSED,
}
