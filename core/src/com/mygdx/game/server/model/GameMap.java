package com.mygdx.game.server.model;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.server.model.entity.DynamicEntity;
import com.mygdx.game.server.model.entity.Entity;
import com.mygdx.game.server.model.entity.StaticEntity;
import com.mygdx.game.server.model.entity.VelocityEntity;
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
	private final ArrayList<DynamicEntity> dynamicEntities;
	private String name;
	private ArrayList<Enemy> enemies;
	private ArrayList<Friendly> friendlies;
	private ArrayList<ActiveSpell> activeSpells;
	private ArrayList<VelocityEntity> projectiles;
	private ArrayList<Trigger> triggers;
	private ArrayList<StaticEntity> staticEntities;
	private ArrayList<Boundary> boundaries;
	private ArrayList<PolygonObject> solidObjects;

	/**
	 * Constructor used by MapLoader.
	 * 
	 * @param name
	 *            name of map
	 */
	public GameMap(String name) {
		this.name = name;
		enemies = new ArrayList<Enemy>();
		friendlies = new ArrayList<Friendly>();
		activeSpells = new ArrayList<ActiveSpell>();
		projectiles = new ArrayList<VelocityEntity>();
		triggers = new ArrayList<Trigger>();
		staticEntities = new ArrayList<StaticEntity>();
		solidObjects = new ArrayList<PolygonObject>();
		dynamicEntities = new ArrayList<DynamicEntity>();
	}

	public String getName() {
		return name;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Friendly> getFriendlies() {
		return friendlies;
	}

	public ArrayList<ActiveSpell> getActiveSpells() {
		return activeSpells;
	}

	public ArrayList<VelocityEntity> getProjectiles() {
		return projectiles;
	}

	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}

	public ArrayList<StaticEntity> getStaticEntities() {
		return staticEntities;
	}

	public ArrayList<Boundary> getBoundaries() {
		return boundaries;
	}
	
	public ArrayList<PolygonObject> getSolidObjects() {
		return solidObjects;
	}

	public List<DynamicEntity> getDynamicEntities() {
		return dynamicEntities;
	}

	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
		addToDynamicEntities(enemy);
	}


	public void addFriendly(Friendly friendly) {
		friendlies.add(friendly);
		addToDynamicEntities(friendly);
	}

	/**
	 * add a dynamic entity to the dynamic entities list and put it in the
	 * solids list if it's solid
	 * @param entity
	 */
	private void addToDynamicEntities(DynamicEntity entity) {
		dynamicEntities.add(entity);
		if (entity.isSolid()) {
			solidObjects.add(entity);
		}
	}
}
