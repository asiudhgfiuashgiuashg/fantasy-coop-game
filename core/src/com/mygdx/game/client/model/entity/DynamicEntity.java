package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.view.spritesheet.SpritesheetMetadataParser;

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
	private TextureRegion currTextureRegion;
	private Animation currAnimation;
	/**
	 * when the current animation began - needed to get current frame
	 */
	private float timeSinceAnimationBegan = 0;

	// used to load animations from aseprite metadata and associated pngs
	private static SpritesheetMetadataParser spritesheetParser = new SpritesheetMetadataParser();

	public DynamicEntity(String entUid, String className, Vector2 pos, int visLayer) {
		super(visLayer);
		this.uid = entUid;
		setPosition(pos);
		currTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("prototype/blank.png")));
		String animationsImageFileName = className + "-sheet.png";
		nameToAnimationMap = spritesheetParser.getAnimations(Gdx.files.internal(animationsImageFileName));
		this.currAnimation = nameToAnimationMap.get(nameToAnimationMap.keySet().toArray()[0]);
	}

	/**
	 * call every tick to add to time since animation started. Animation needs
	 * this value to calculate the current frame.
	 *
	 * @param deltaT
	 */
	public void tick(float deltaT) {
		timeSinceAnimationBegan += deltaT;
	}

	// TODO - separate out dynamic entities in the map and uupdate them all
	// so they can update their time elapsed every tick
	@Override
	//TODO return the texture region which should be drawn
	public TextureRegion getTextureRegion() {
		currTextureRegion = currAnimation.getKeyFrame(timeSinceAnimationBegan);
		return currTextureRegion;
	}

	public void setAnimation(String animationName) {
		currAnimation = nameToAnimationMap.get(animationName);
		currAnimation.setPlayMode(Animation.PlayMode.LOOP);
		timeSinceAnimationBegan = 0;
	}
}
