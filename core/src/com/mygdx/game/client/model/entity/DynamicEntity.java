package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
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
public class DynamicEntity extends MapEntity {
	// maps animation name (specified in aseprite) to an animation (a
	// sequence of frames drawn in aseprite)
	// TODO load animation map (a class exists in util package for this)
	private Map<String, Animation> nameToAnimationMap;
	// position of this entity in the previous 2 position updates (to be used
	// for interpolation)
	private Vector2[] prevPos;
	private TextureRegion currTextureRegion;
	private Animation currAnimation;

	public DynamicEntity(String entUid, String className, Vector2 pos) {
		super();
		this.uid = entUid;
		this.position = pos;
		currTextureRegion = new TextureRegion(new Texture(Gdx.files.internal
				("prototype/tall_test.png")));
	}

	@Override
	//TODO return the texture region which should be drawn
	public TextureRegion getTextureRegion() {
		return currTextureRegion;
	}

	public void setAnimation(String animationName) {
		this.currAnimation = nameToAnimationMap.get(animationName);
	}
}
