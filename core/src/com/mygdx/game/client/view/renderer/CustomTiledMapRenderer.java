package com.mygdx.game.client.view.renderer;

import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.client.model.entity.MapEntity;
import com.mygdx.game.client.model.entity.StaticEntity;
import com.mygdx.game.client.model.map.ClientTiledMap;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.*;

/**
 * Extends libgdx's renderer to work with MapEntities and respect visLayers
 * <p>
 * visLayers: -1 = always render below other entities 0 = render in order
 * according to y-position derived from hitbox and location -- DEFAULT VISLAYER
 * 1 = always render above other entities
 * <p>
 * Rendering order: Tile Layer -> visLayer -1 entities -> visLayer 0 entities ->
 * visLayer 1 entities
 */
public class CustomTiledMapRenderer extends OrthogonalTiledMapRenderer {
	public static final int DEFAULT_VISLAYER = 0;
	public static final int NUM_RAYS = 15; // affects the quality of lights
	private static final float DEBUG_FONT_SCALE = .41f;
	private final List<MapEntity> layerNegOneEntities = new ArrayList<MapEntity>();
	private final List<MapEntity> layerZeroEntities = new LinkedList<MapEntity>();
	private final List<MapEntity> layerOneEntities = new ArrayList<MapEntity>();

	public boolean debug = true; // should I draw things that developers use to
	// debug?
	private float debugLineWidth = 3;

	private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	private RayHandler rayHandler; // used to render lights

	/*
	 * The color which covers everything. Move alpha towards zero to make things
	 * darker or move alpha towards one to make things lighter
	 */
	private Color ambientColor;

	private FPSLogger fpsLogger;
	private List<DynamicEntity> dynamicEntities;

	/* used for debugging */
	private final BitmapFont font = new BitmapFont();

	public CustomTiledMapRenderer(TiledMap map, Batch batch, RayHandler rayHandler) {
		super(map, batch);
		this.rayHandler = rayHandler;
		setup();
	}

	/**
	 * called in constructors to set up/load variables
	 */
	private void setup() {
		populateEntitiesLists();
		// rayHandler.setCombinedMatrix(batch.getProjectionMatrix());
		ambientColor = Color.CLEAR;
		setAmbientAlpha(.15f);
		fpsLogger = new FPSLogger();
		dynamicEntities = new ArrayList<DynamicEntity>();
		font.getData().setScale(DEBUG_FONT_SCALE);
	}

	public void setAmbientColor(Color color) {
		this.ambientColor = color;
		rayHandler.setAmbientLight(ambientColor);
	}

	/**
	 * good for simulating time of day
	 *
	 * @param alpha
	 */
	public void setAmbientAlpha(float alpha) {
		ambientColor.a = alpha;
		rayHandler.setAmbientLight(ambientColor);
	}

	/**
	 * renders: - tile layer - static entities - dynamic entities - lighting -
	 * hitboxes and cutoffY's if debug is true doesn't render: - gui - developer
	 * console
	 */
	@Override
	public void render() {
		beginRender();
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 1");
		renderTileLayer(tileLayer); // render tiles before entities so tiles
		// are on the bottom
		renderEntities();
		endRender();
		rayHandler.updateAndRender();
		// since debug rendering uses a shape renderer, it must start after
		// endRender() which calls batch.end(). Otherwise rendering gets
		// messed up and some textureregions don't draw for some reason...
		if (debug) {
			// draw hitboxes
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			debugRenderEntityLines();
			shapeRenderer.end();
			// fpsLogger.log();
			// render text info like position
			beginRender();
			debugRenderTextInfos();
			endRender();
		}

		// update dynamic entity animation frame
		for (DynamicEntity entity : dynamicEntities) {
			entity.tick(Gdx.graphics.getDeltaTime());
		}
	}

	/**
	 * render textual debug information about entities
	 */
	private void debugRenderTextInfos() {
		debugRenderEntityTextInfosList(layerNegOneEntities);
		debugRenderEntityTextInfosList(layerZeroEntities);
		debugRenderEntityTextInfosList(layerOneEntities);
	}

	private void debugRenderEntityTextInfosList(List<MapEntity> entities) {
		for (MapEntity entity : entities) {
			debugRenderEntityTextInfo(entity);
		}
	}

	private void debugRenderEntityTextInfo(MapEntity entity) {
		float posDrawX = entity.getPos().x;
		float posDrawY = entity.getPos().y;

		float visLayerDrawX = posDrawX;
		float visLayerDrawY = posDrawY - DEBUG_FONT_SCALE * 15;

		font.draw(batch, entity.getPos().toString(), posDrawX, posDrawY);
		font.draw(batch, "vislayer: " + entity.getVisLayer(), visLayerDrawX, visLayerDrawY);
	}

	/**
	 * use shaperenderer to render debug info about entities that needs to be
	 * rendered as lines or simple shapes
	 */
	private void debugRenderEntityLines() {
		debugRenderEntityList(layerNegOneEntities);
		debugRenderEntityList(layerZeroEntities);
		debugRenderEntityList(layerOneEntities);
	}

	private void debugRenderEntityList(List<MapEntity> entities) {
		for (MapEntity entity : entities) {
			debugRenderEntity(entity);
		}
	}

	private void debugRenderEntity(MapEntity entity) {
		debugDrawCollideablePolygon(entity);
	}

	/**
	 * used for debugging -- polygons shouldn't be drawn normally
	 *
	 * @param entity
	 */
	private void debugDrawCollideablePolygon(CollideablePolygon entity) {
		shapeRenderer.setColor(Color.FIREBRICK);
		shapeRenderer.polygon(entity.getTransformedVertices());

		shapeRenderer.setColor(Color.YELLOW);
		for (float[] triangle: entity.getTriangles()) {
			shapeRenderer.polygon(triangle);
	}

		// draw a line representing the cutoffy
		Rectangle boundingRect = entity.getBoundingRectangle(); // used
		// to draw ycuttoff line
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.line(boundingRect.getX(), entity.getTransformedCutoffY(), boundingRect.getX() + boundingRect.getWidth(), entity.getTransformedCutoffY());

	}

	/**
	 * draw static and dynamic entities by layer TODO render dynamic entities
	 */
	private void renderEntities() {
		renderEntityList(layerNegOneEntities);
		renderEntityList(layerZeroEntities);
		renderEntityList(layerOneEntities);
	}

	private void renderEntityList(List<MapEntity> entityList) {
		for (MapEntity entity : entityList) {
			renderEntity(entity);
		}
	}

	/**
	 * draws an entity's texture and its associated lights
	 *
	 * @param staticEntity
	 */
	private void renderEntity(MapEntity entity) {
		drawEntityTexture(entity);
	}

	private void drawEntityTexture(MapEntity entity) {
		TextureRegion toDraw = entity.getTextureRegion();

		batch.draw(toDraw, entity.getPos().x, entity.getPos().y, toDraw.getRegionWidth(), toDraw.getRegionHeight());
	}

	@Override
	public void setView(OrthographicCamera camera) {
		super.setView(camera);
		rayHandler.setCombinedMatrix(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);
		Gdx.gl20.glLineWidth(debugLineWidth / camera.zoom);
	}

	/**
	 * used by constructors to initially populate lists of entities in each
	 * visLayer for easier rendering.
	 * <p>
	 * Initially there will only be static entities, so don't worry about
	 * dynamic entities. These will be inserted into the lists when
	 * communication with the server starts a moment after the map loads.
	 */
	private void populateEntitiesLists() {
		for (StaticEntity entity : ((ClientTiledMap) map).staticEntities) {
			registerStaticEntity(entity);
		}
	}

	/**
	 * when a new entity appears on the map, tell the renderer to keep track of
	 * it also
	 * <p>
	 * Also use this when instantiating the renderer with the map after the map
	 * has been loaded
	 */
	public void registerStaticEntity(StaticEntity entity) {
		putInVisLayerList(entity);
	}

	/**
	 * keep track of dynamic entities in a separate list so you can update their
	 * animations in render loop
	 *
	 * @param entity
	 */
	public void registerDynamicEntity(DynamicEntity entity) {
		dynamicEntities.add(entity);
		putInVisLayerList(entity);
	}

	private void putInVisLayerList(MapEntity entity) {
		int visLayer = entity.getVisLayer();
		if (-1 == visLayer) {
			layerNegOneEntities.add(entity);
		} else if (0 == visLayer) {
			insertIntoLayerZero(entity);
		} else if (1 == visLayer) {
			layerOneEntities.add(entity);
		}
	}

	/**
	 * put this entity into an already-sorted layer 0
	 *
	 * @param entity
	 */
	private void insertIntoLayerZero(MapEntity entity) {
		boolean inserted = false;
		EntityComparator comparator = new EntityComparator();
		for (int i = 0; i < layerZeroEntities.size() && !inserted; i++) {
			MapEntity otherEntity = layerZeroEntities.get(i);
			/*
			 * if otherEntity (and all the following entities in the list)
			 * should be rendered above this one
			 */
			if (comparator.compare(entity, otherEntity) < 0) {
				inserted = true;
				layerZeroEntities.add(i, entity);
			}
		}
		if (!inserted) { // stick it at the end of the render list
			layerZeroEntities.add(entity);
		}
	}

	/**
	 * when an entity is removed from the map, tell the renderer to stop caring
	 * about it
	 */
	public void deregisterEntity(MapEntity entity) {
		int visLayer = entity.getVisLayer();
		if (-1 == visLayer) {
			layerNegOneEntities.remove(entity);
		} else if (0 == visLayer) {
			layerZeroEntities.remove(entity);
		} else if (1 == visLayer) {
			layerOneEntities.remove(entity);
		}
		dynamicEntities.remove(entity);
	}

	/**
	 * toggle rendering debug stuff such as drawing hitboxes
	 */
	public void toggleDebug() {
		debug = debug ? false : true;
	}

	/**
	 * used to sort entities in layer zero for rendering
	 */
	private class EntityComparator implements Comparator<MapEntity> {
		@Override
		public int compare(MapEntity o1, MapEntity o2) {
			/*
			 * the values by which o1 and o2 will be sorted
			 */
			float o1RenderY = o1.getCutOffY();
			float o2RenderY = o2.getCutOffY();

			float comparisonVal = o2RenderY - o1RenderY;
			if (comparisonVal < 0) {
				return -1;
			}
			if (comparisonVal > 0) {
				return 1;
			}
			return 0;
		}
	}
}
