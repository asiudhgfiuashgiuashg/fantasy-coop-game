package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Actable;

/**
 * 
 * @author elimonent
 *
 * An entity which is updated every tick with act()
 */
public abstract class DynamicEntity extends Entity implements Actable {

	protected DynamicEntity(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}
	
}
