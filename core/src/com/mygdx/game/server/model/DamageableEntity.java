package com.mygdx.game.server.model;

import java.util.List;

/**
 * An entity that has health.
 * @author elimonent
 *
 */
public abstract class DamageableEntity {
	protected int health;
	protected List<Effect> effects;
}
