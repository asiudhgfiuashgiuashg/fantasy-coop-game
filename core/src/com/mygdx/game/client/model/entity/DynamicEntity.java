package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

/**
 * A MapEntity which moves around and may have a bunch of different
 * animations or hitboxes depending on its state.
 * These entities are specified by placeholders in the "Dynamic Entities"
 * layer in Tiled. Their appearance and position is specified in code, not in
 * Tiled (unlike StaticEntities).
 */
public abstract class DynamicEntity extends MapEntity {
	// maps animation name (specified in aseprite) to an animation (a
	// sequence of frames drawn in aseprite)
	private Map<String, Animation> nameToAnimationMap;
	// position of this entity in the previous 2 position updates (to be used
	// for interpolation)
	private Vector2[] prevPos;
}
