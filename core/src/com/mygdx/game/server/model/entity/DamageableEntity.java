package com.mygdx.game.server.model.entity;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Effect;

/**
 * An entity that has health.
 * @author elimonent
 *
 */
public abstract class DamageableEntity extends VelocityEntity {
	protected DamageableEntity(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid, 5);
	}
	
	protected int health;
	protected List<Effect> effects;
}
