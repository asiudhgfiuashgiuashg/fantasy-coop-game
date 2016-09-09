package com.mygdx.game.server.model;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.server.model.trigger.Trigger;

/**
 * Represents a game map (one contiguous area) on the server.
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public class GameMap {
	private String name;
	private Array<Enemy> enemies;
	private Array<NonEnemyCharacter> nonEnemyCharacters;
	private Array<ActiveSpell> activeSpells;
	private Array<Projectile> projectiles;
	private Array<Trigger> triggers;
	private Array<StaticEntity> staticEntities;

	/**
	 * Constructor used by MapLoader.
	 * 
	 * @param name
	 *            name of map
	 */
	public GameMap(String name) {
		this.name = name;
	}
}
