package com.mygdx.game.client.model.entity;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.FlickerPointLight;
import com.mygdx.game.client.view.renderer.CustomTiledMapRenderer;
import com.mygdx.game.shared.model.CollideablePolygon;

import static com.mygdx.game.client.view.renderer.CustomTiledMapRenderer.DEFAULT_VISLAYER;

import java.util.ArrayList;
import java.util.List;

/**
 * A non-moving object visualized on the screen by a StaticTiledMapTile or an
 * AnimatedTiledMapTile (wrapped in a TiledMapTileMapObject). It may have a
 * polygon hitbox associated with it for collision.
 * The appearance and position of a StaticEntity is specified in Tiled.
 */
public class StaticEntity extends MapEntity {
	public TiledMapTileMapObject tileMapObject; // used for drawing

	/**
	 * @param hitbox        used for collision detection
	 * @param tempLights    temporary lights which contain info used to
	 *                      create real box2d lights to be rendered
	 * @param tileMapObject holds the tile which represents this entity
	 *                      visually. Used to obtain x and y position
	 *                      which was loaded in from tiled into
	 *                      tileMapObject.
	 * @param mapHeight     needed to flip Tiled y axis (points down) to match
	 * @param tileHeight
	 * @param rayHandler
	 */
	public StaticEntity(float[] vertices, List<FlickerPointLight> tempLights, TiledMapTileMapObject tileMapObject, float mapHeight, float tileHeight, RayHandler rayHandler) {
		this.tileMapObject = tileMapObject;
		String visLayerStr = (String) (tileMapObject.getProperties().get("visLayer"));
		// get the vislayer that was loaded for us by libgdx
		setVisLayer(null == visLayerStr ? DEFAULT_VISLAYER : Integer.valueOf(visLayerStr));

		// the libgdx loader loaded the associated TiledMapTileMapObject from
		// the Static Entities layer for us, and in Tiled we specify the x
		// and y position of each static entity as properties, so we can
		// extract it into this static entity.
		float xPos = tileMapObject.getProperties().get("x", Float.class);
		// flip y axis
		float yPos = mapHeight + tileMapObject.getProperties().get("y", Float.class);
		setPosition(new Vector2(xPos, yPos));

		setupHitbox(vertices);
		setupLights(tempLights, rayHandler);
	}


	/**
	 * Used by CustomTiledMapRenderer
	 *
	 * @return the textureregion that the renderer should draw do NOT return
	 * tileMapObject.getTextureRegion() - this won't work if the tile is
	 * animated
	 */
	@Override
	public TextureRegion getTextureRegion() {
		return tileMapObject.getTile().getTextureRegion();
	}

	/**
	 * Translate the tempLights to this entity's position.
	 *
	 * @param tempLights
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private void setupLights(List<FlickerPointLight> tempLights, RayHandler rayHandler) {
		box2dLights = new ArrayList<PointLight>();
		for (FlickerPointLight tempLight : tempLights) {
			FlickerPointLight transTempLight = copyAndTranslateLightPointLight(tempLight, rayHandler);

			box2dLights.add(transTempLight);
		}
	}

	/**
	 * create a copy of the light from the tileset translated to this
	 * entity's position
	 *
	 * @param light
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private FlickerPointLight copyAndTranslateLightPointLight(FlickerPointLight light, RayHandler rayHandler) {
		FlickerPointLight toReturn = new FlickerPointLight(rayHandler, CustomTiledMapRenderer.NUM_RAYS, light.getColor().cpy(), light.getDistance(), light.getX() + getPos().x, light.getY() + getPos().y, light.getFlickerRate(), light.getFlickerDistMult(), light.getFlickerAlphaMult());
		return toReturn;
	}

	/**
	 * take the hitbox passed in from the tileset and translate it to where
	 * this entity actually is
	 *
	 * @param hitbox
	 */
	private void setupHitbox(float[] vertices) {
		// sometimes there is no hitbox, so don't worry about assigning it
		// and moving it to position if it doesn't exist
		if (vertices != null) {
			setVertices(vertices); // make a  copy of
		}
	}
}
