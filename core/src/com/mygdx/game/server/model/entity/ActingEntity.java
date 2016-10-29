package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Actable;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * 
 * @author elimonent
 *
 * An entity which is updated every tick with act()
 */
public abstract class ActingEntity extends Entity implements Actable {

	protected ActingEntity(String uid, Vector2 position, int visLayer) {
		super(uid, position, visLayer);
	}
	
}
