package com.mygdx.game.server.model.entity.friendly;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.entity.DamageableEntity;

/**
 * an AI who does not setAttack the player
 * @author elimonent
 *
 */
public abstract class Friendly extends DamageableEntity {

	protected Friendly(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

}
