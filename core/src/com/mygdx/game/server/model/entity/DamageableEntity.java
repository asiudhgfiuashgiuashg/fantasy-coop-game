package com.mygdx.game.server.model.entity;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.entity.*;
import com.mygdx.game.server.model.Effect;

/**
 * An entity that has health.
 * @author elimonent
 *
 */
public abstract class DamageableEntity extends DynamicEntity {
	protected DamageableEntity(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}
	
	protected int health;
	protected List<Effect> effects;
}
