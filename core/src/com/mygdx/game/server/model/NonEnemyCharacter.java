package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * an AI who does not attack the player
 * @author elimonent
 *
 */
public abstract class NonEnemyCharacter extends DamageableEntity {

	protected NonEnemyCharacter(String uid, Vector2 position, String spriteName, int visLayer,
			CollideablePolygon polygon) {
		super(uid, position, spriteName, visLayer, polygon);
	}

}
