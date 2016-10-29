package com.mygdx.game.server.model.entity.friendly;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.entity.DamageableEntity;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * an AI who does not attack the player
 * @author elimonent
 *
 */
public abstract class Friendly extends DamageableEntity {

	protected Friendly(String uid, Vector2 position, String spriteName, int visLayer) {
		super(uid, position, spriteName, visLayer);
	}

}
