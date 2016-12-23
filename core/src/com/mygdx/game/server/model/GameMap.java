package com.mygdx.game.server.model;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.server.model.entity.Projectile;
import com.mygdx.game.server.model.entity.StaticEntity;
import com.mygdx.game.server.model.entity.enemy.Enemy;
import com.mygdx.game.server.model.entity.friendly.Friendly;
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
	private Array<Friendly> friendlies;
	private Array<ActiveSpell> activeSpells;
	private Array<Projectile> projectiles;
	private Array<Trigger> triggers;
	private Array<StaticEntity> staticEntities;
	private Array<Boundary> boundaries;

	/**
	 * Constructor used by MapLoader.
	 * 
	 * @param name
	 *            name of map
	 */
	public GameMap(String name) {
		this.name = name;
		enemies = new Array<Enemy>();
		friendlies = new Array<Friendly>();
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

	public Array<Friendly> getFriendlies() {
		return friendlies;
	}

	public void addFriendly(Friendly friendly) {
		friendlies.add(friendly);
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
		System.out.print("trig added");
		triggers.add(trig);
	}

	public Array<StaticEntity> getStaticEntities() {
		return staticEntities;
	}

	public void addStaticEntity(StaticEntity ent) {
		staticEntities.add(ent);
	}

	public Array<Boundary> getBoundaries() {
		return boundaries;
	}

	public void addBoundary(Boundary b) {
		boundaries.add(b);
	}
}
