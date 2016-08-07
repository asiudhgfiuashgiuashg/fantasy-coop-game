package com.mygdx.game.server.model;

/**
 * Represents a cutscene which is acted out incrementally each tick.
 * A cutscene is read from a file describing the cutscene.
 * When a cutscene starts, the GameState is set to CUTSCENE.
 * When a cutscene ends, the GameState is set to GAME
 * @author elimonent
 *
 */
public abstract class Cutscene implements Actable {

}
