package com.mygdx.game.client.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.mygdx.game.client.model.ClientTiledMap;
import com.mygdx.game.client.model.entity.MapEntity;
import com.mygdx.game.client.model.entity.StaticEntity;
import com.mygdx.game.shared.util.CollideablePolygon;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.client.model.GameClient.console;

/**
 * Extends libgdx's renderer to work with MapEntities and respect visLayers
 *
 * visLayers:
 *     -1 = always render below other entities
 *     TODO 0 = render according to y-position derived from hitbox and
 *     location -- DEFAULT VISLAYER
 *     1 = always render above other entities
 *
 *     Rendering order: Tile Layer -> visLayer -1 entities -> visLayer 0
 *     entities -> visLayer 1 entities
 */
public class CustomTiledMapRenderer extends
	OrthogonalTiledMapRenderer {
	public static final int DEFAULT_VISLAYER = 0;
	private static final float DEFAULT_UNIT_SCALE = 2f;
	private final List<MapEntity> layerNegOneEntities = new
			ArrayList<MapEntity>();
	private final List<MapEntity> layerZeroEntities = new
			ArrayList<MapEntity>();
	private final List<MapEntity> layerOneEntities = new ArrayList<MapEntity>();

	public boolean debug = true; // should I draw things that developers use
	// to debug?
	private float debugLineWidth = 3;

	private final ShapeRenderer shapeRenderer = new ShapeRenderer();


	/**
	 * renders:
	 * - tile layer
	 * - static entities
	 * - dynamic entities
	 * doesn't render:
	 * - gui
	 * - developer console
	 *  TODO respect vislayers when rendering
	 */
	@Override
	public void render() {
		beginRender();
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers()
				.get("Tile Layer 1");
		renderTileLayer(tileLayer); // render tiles before entities so tiles
		// are on the bottom
		renderEntities();
		endRender();

		// since debug rendering uses a shape renderer, it must start after
		// endRender() which calls batch.end(). Otherwise rendering gets
		// messed up and some textureregions don't draw for some reason...
		if (debug) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			debugRenderEntities();
			shapeRenderer.end();
		}


	}

	/**
	 * render debug info about entities (hitboxes)
	 */
	private void debugRenderEntities() {
		debugRenderEntityList(layerNegOneEntities);
		debugRenderEntityList(layerZeroEntities);
		debugRenderEntityList(layerOneEntities);
	}

	private void debugRenderEntityList(List<MapEntity> entities) {
		for (MapEntity entity: entities) {
			debugRenderEntity(entity);
		}
	}

	private void debugRenderEntity(MapEntity entity) {
		CollideablePolygon hitbox = entity.getHitbox();
		if (hitbox != null) {
			Polygon scaledHitbox = new CollideablePolygon(hitbox);// scale
			// the hitbox to render on top of the scaled images properly
			scaledHitbox.setScale(unitScale, unitScale);
			scaledHitbox.setPosition(scaledHitbox.getX() * unitScale,
					scaledHitbox.getY() * unitScale);
			shapeRenderer.polygon(scaledHitbox.getTransformedVertices());
		}
	}

	/**
	 * draw static and dynamic entities by layer
	 *  TODO render dynamic entities
	 */
	private void renderEntities() {
		renderEntityList(layerNegOneEntities);
		renderEntityList(layerZeroEntities);
		renderEntityList(layerOneEntities);
	}

	private void renderEntityList(List<MapEntity> entityList) {
		for (MapEntity entity: entityList) {
			renderEntity(entity);
		}
	}

	private void renderEntity(MapEntity staticEntity) {
		TextureRegion toDraw = staticEntity.getTextureRegion();

		batch.draw(toDraw, staticEntity.getPos().x * unitScale, staticEntity
				.getPos().y * unitScale, toDraw.getRegionWidth() *
				unitScale, toDraw.getRegionHeight() * unitScale);
	}


	@Override
	public void setView(OrthographicCamera camera) {
		super.setView(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(Color.FIREBRICK);
		Gdx.gl20.glLineWidth(debugLineWidth / camera.zoom);
	}

	public CustomTiledMapRenderer(ClientTiledMap map) {
		this(map, DEFAULT_UNIT_SCALE);
	}


	public CustomTiledMapRenderer(TiledMap map, float unitScale) {
		super(map, unitScale);
		setup();
	}

	/**
	 * called in constructors to set up/load variables
	 */
	private void setup() {
		populateEntitiesLists();
	}

	/**
	 * used by constructors to initially populate lists of entities in each
	 * visLayer for easier rendering.
	 *
	 * Initially there will only be static entities, so don't worry about
	 * dynamic entities. These will be inserted into the lists when
	 * communication with the server starts a moment after the map loads.
	 */
	private void populateEntitiesLists() {
		for (MapEntity entity: ((ClientTiledMap) map).staticEntities) {
			int visLayer = entity.getVisLayer();
			if (-1 == visLayer) {
				layerNegOneEntities.add(entity);
			} else if (0 == visLayer) {
				layerZeroEntities.add(entity);
			} else if (1 == visLayer) {
				layerOneEntities.add(entity);
			}
		}
	}


}
