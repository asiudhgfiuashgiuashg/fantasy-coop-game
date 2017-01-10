package com.mygdx.game.server.model.player;

import java.util.List;

import com.mygdx.game.server.model.ServerSpell;

/**
 * represents a player (one of the 3 classes which is controlled by a human)
 * @author elimonent
 *
 */
public abstract class Player {
	/**
	 * The class-specific spells that a player has.
	 */
	protected List<ServerSpell> spells;
	
}
