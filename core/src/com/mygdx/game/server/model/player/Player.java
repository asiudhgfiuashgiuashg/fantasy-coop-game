package com.mygdx.game.server.model.player;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.ServerSpell;
import com.mygdx.game.server.model.entity.DamageableEntity;

/**
 * represents a player (one of the 3 classes which is controlled by a human)
 * @author elimonent
 *
 */
public abstract class Player extends DamageableEntity {
	/**
	 * The class-specific spells that a player has.
	 */
	protected List<ServerSpell> spells;

	protected Player(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}
}
