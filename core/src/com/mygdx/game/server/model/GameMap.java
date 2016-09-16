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
		enemies = new Array<Enemy>();
		nonEnemyCharacters = new Array<NonEnemyCharacter>();
		activeSpells = new Array<ActiveSpell>();
		projectiles = new Array<Projectile>();
		triggers = new Array<Trigger>();
		staticEntities = new Array<StaticEntity>();
	}

	public String getName() {
		return name;
	}

	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}

	public Array<NonEnemyCharacter> getNonEnemyCharacters() {
		return nonEnemyCharacters;
	}
	
	public void addNonEnemyCharacter(NonEnemyCharacter nec) {
		nonEnemyCharacters.add(nec);
	}

	public Array<ActiveSpell> getActiveSpells() {
		return activeSpells;
	}
	
	public void addActiveSpell(ActiveSpell spell) {
		activeSpells.add(spell);
	}

	public Array<Projectile> getProjectiles() {
		return projectiles;
	}
	
	public void addProjectile(Projectile proj) {
		projectiles.add(proj);
	}

	public Array<Trigger> getTriggers() {
		return triggers;
	}
	
	public void addTrigger(Trigger trig) {
		triggers.add(trig);
	}
	
	public Array<StaticEntity> getStaticEntities() {
		return staticEntities;
	}
	
	public void addStaticEntity(StaticEntity ent) {
		staticEntities.add(ent);
	}
}
