package com.mygdx.game.server.model.entity;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Effect;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * An entity that has health.
 * @author elimonent
 *
 */
public abstract class DamageableEntity extends ActingEntity {
	protected DamageableEntity(String uid, Vector2 position, String spriteName, int visLayer) {
		super(uid, position, spriteName, visLayer);
	}
	
	protected int health;
	protected List<Effect> effects;
}
