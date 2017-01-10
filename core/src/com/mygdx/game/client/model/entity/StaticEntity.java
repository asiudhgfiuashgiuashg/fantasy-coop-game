package com.mygdx.game.client.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shared.model.CollideablePolygon;

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
     * @param tileMapObject holds the tile which represents this entity
     *                      visually. Used to obtain x and y position
     *                      which was loaded in from tiled into
     *                      tileMapObject.
     * @param mapHeight     needed to flip Tiled y axis (points down) to match
     * @param tileHeight
     */
    public StaticEntity(CollideablePolygon hitbox, TiledMapTileMapObject
            tileMapObject, float mapHeight, float tileHeight) {
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
        console.log("y: " + tileMapObject.getProperties()
                .get("y", Float.class));
        console.log("mapheight: " + mapHeight);
        console.log("static entity y : " + yPos);
        //for some reason yPos needs negated or the y values are flipped..
        this.position = new Vector2(xPos, yPos);

        // sometimes there is no hitbox, so don't worry about assigning it
        // and moving it to position if it doesn't exist
        if (hitbox != null) {
            this.hitbox = new CollideablePolygon(hitbox); // make a  copy of the
            // polygon loaded with the tile set so that we can offset it to the
            // correct position on the map
            this.hitbox.setPosition(this.position.x, this.position.y);//
            // offset hitbox to be on top of this static entity on the map.
            console.log("hitbox y: " + this.hitbox.getY());
            console.log("hitbox vertices: ");
            for (int i = 0; i < this.hitbox.getVertices().length; i++) {
                if (i % 2 == 0) {
                    console.log("x: " + this.hitbox.getVertices()[i]);
                } else {
                    console.log("y: " + this.hitbox.getVertices()[i]);
                }
            }
        }
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
}
