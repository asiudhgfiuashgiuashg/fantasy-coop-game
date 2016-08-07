package com.mygdx.game.server.model;

/**
 * A container for a spell's name and cooldown.
 * ActiveSpell is an instance of the spell being cast.
 * ServerSpell is referenced to see if its associated
 *  ActiveSpell can be created.
 * @author elimonent
 *
 */
public class ServerSpell {
	/**
	 * Identifies the spell.
	 */
	String name;
	float maxCooldown;
	float cooldown;
}
