package com.mygdx.game.client.model.entity;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.client.model.ClientTiledMap;
import com.mygdx.game.client.view.CustomTiledMapRenderer;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.client.model.GameClient.console;
import static com.mygdx.game.client.view.CustomTiledMapRenderer
        .DEFAULT_VISLAYER;

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
    public StaticEntity(CollideablePolygon hitbox, List<PointLight> tempLights,
						TiledMapTileMapObject tileMapObject, float mapHeight, float tileHeight, RayHandler rayHandler) {
        this.tileMapObject = tileMapObject;
        String visLayerStr = (String) (tileMapObject.getProperties()
                .get("visLayer"));
        this.visLayer = null == visLayerStr ? DEFAULT_VISLAYER : Integer.valueOf
                (visLayerStr); // get the vislayer that was loaded for us by
        // libgdx

        // the libgdx loader loaded the associated TiledMapTileMapObject from
        // the Static Entities layer for us, and in Tiled we specify the x
        // and y position of each static entity as properties, so we can
        // extract it into this static entity.
        float xPos = tileMapObject.getProperties().get("x", Float.class);
        // flip y axis
        float yPos = mapHeight + tileMapObject.getProperties()
                .get("y", Float.class);
        //for some reason yPos needs negated or the y values are flipped..
        this.position = new Vector2(xPos, yPos);

		setupHitbox(hitbox);
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
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private void setupLights(List<PointLight> tempLights, RayHandler
			rayHandler) {
		box2dLights = new ArrayList<PointLight>();
		for (PointLight tempLight: tempLights) {
			PointLight transTempLight = copyAndTranslateLightPointLight
					(tempLight, rayHandler);

			box2dLights.add(transTempLight);
		}
	}

	/**
	 * create a copy of the light from the tileset translated to this
	 * entity's position
	 * @param light
	 * @param rayHandler needed to instantiate box2dlights
	 */
	private PointLight copyAndTranslateLightPointLight(PointLight light, RayHandler rayHandler) {
		PointLight toReturn = new PointLight(rayHandler,
				CustomTiledMapRenderer.NUM_RAYS, light.getColor().cpy(),
				light.getDistance(), light.getX() + getPos().x, light
				.getY() + getPos().y);
		return toReturn;
	}

	/**
	 * take the hitbox passed in from the tileset and translate it to where
	 * this entity actually is
	 * @param hitbox
	 */
	private void setupHitbox(CollideablePolygon hitbox) {
		// sometimes there is no hitbox, so don't worry about assigning it
		// and moving it to position if it doesn't exist
		if (hitbox != null) {
			this.hitbox = new CollideablePolygon(hitbox); // make a  copy of the
			// polygon loaded with the tile set so that we can offset it to the
			// correct position on the map
			// offset hitbox to be on top of this static entity on the map.
			this.hitbox.setPosition(this.position.x, this.position.y);//
		}
	}
}
