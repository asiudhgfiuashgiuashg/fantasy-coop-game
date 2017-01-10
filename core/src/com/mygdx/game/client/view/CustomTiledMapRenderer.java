package com.mygdx.game.client.view;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.client.model.ClientTiledMap;
import com.mygdx.game.client.model.entity.MapEntity;
import com.mygdx.game.shared.model.CollideablePolygon;

import java.util.*;

import static com.mygdx.game.client.model.GameClient.SCREEN_HEIGHT;
import static com.mygdx.game.client.model.GameClient.console;

/**
 * Extends libgdx's renderer to work with MapEntities and respect visLayers
 *
 * visLayers:
 *     -1 = always render below other entities
 *     0 = render in order according to y-position derived from hitbox and
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
			LinkedList<MapEntity>();
	private final List<MapEntity> layerOneEntities = new ArrayList<MapEntity>();

	public boolean debug = true; // should I draw things that developers use
	// to debug?
	private float debugLineWidth = 3;

	private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	private World world; // used to render lights
	private RayHandler rayhandler; // used to render lights
	private PointLight light;
	/*
	 * The color which covers everything. Move alpha towards zero to make
	 * things darker or move alpha towards one to make things lighter
	 */
	private Color ambientColor;

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
		world = new World(new Vector2(0, 0), false);
		rayhandler = new RayHandler(world);
		rayhandler.setCombinedMatrix(batch.getProjectionMatrix());
		ambientColor = Color.CLEAR;
		setAmbientAlpha(.5f);

		light = new PointLight(rayhandler, 100, Color.YELLOW, 100, 250,
				250);
	}

	public void setAmbientColor(Color color) {
		this.ambientColor = color;
		rayhandler.setAmbientLight(ambientColor);
	}

	/**
	 * good for simulating time of day
	 * @param alpha
	 */
	public void setAmbientAlpha(float alpha) {
		ambientColor.a = alpha;
		rayhandler.setAmbientLight(ambientColor);
	}

	/**
	 * renders:
	 * - tile layer
	 * - static entities
	 * - dynamic entities
	 * - lighting
	 * doesn't render:
	 * - gui
	 * - developer console
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
		light.setPosition(Gdx.input.getX(), SCREEN_HEIGHT - Gdx.input.getY());
		rayhandler.updateAndRender();
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
			debugDrawCollideablePolygon(hitbox);
		}
	}

	/**
	 * used for debugging -- polygons shouldn't be drawn normally
	 * @param hitbox
	 */
	private void debugDrawCollideablePolygon(CollideablePolygon hitbox) {
		CollideablePolygon scaledHitbox = new CollideablePolygon(hitbox);//
		// scale the hitbox to render on top of the scaled images properly
		scaledHitbox.setScale(unitScale, unitScale);
		scaledHitbox.setPosition(scaledHitbox.getX() * unitScale,
				scaledHitbox.getY() * unitScale);
		shapeRenderer.setColor(Color.FIREBRICK);
		shapeRenderer.polygon(scaledHitbox.getTransformedVertices());

		//draw a  line representing the cutoffy
		Rectangle boundingRect = scaledHitbox.getBoundingRectangle(); // used
		// to draw ycuttoff line
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.line(boundingRect.getX(), scaledHitbox.getTransformedCutoffY(),
				boundingRect.getX() + boundingRect.getWidth(), scaledHitbox
						.getTransformedCutoffY());

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

	private void renderEntity(MapEntity staticEntity ) {
		TextureRegion toDraw = staticEntity.getTextureRegion();

		batch.draw(toDraw, staticEntity.getPos().x * unitScale, staticEntity
				.getPos().y * unitScale, toDraw.getRegionWidth() *
				unitScale, toDraw.getRegionHeight() * unitScale);
	}


	@Override
	public void setView(OrthographicCamera camera) {
		super.setView(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);
		Gdx.gl20.glLineWidth(debugLineWidth / camera.zoom);
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
			registerEntity(entity);
		}
	}

	/**
	 * when a new entity appears on the map, tell the renderer to keep track
	 * of it also
	 *
	 * Also use this when instantiating the renderer with the map after the map
	 * has been loaded
	 */
	public void registerEntity(MapEntity entity) {
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
	 * @param entity
	 */
	private void insertIntoLayerZero(MapEntity entity) {
		boolean inserted = false;
		EntityComparator comparator = new EntityComparator();
		for (int i = 0; i < layerZeroEntities.size() && !inserted; i++) {
			MapEntity otherEntity = layerZeroEntities.get(i);
			/* if otherEntity (and all the following entities in the list)
			 * should be rendered above this one
			 */
			if (comparator.compare(entity, otherEntity) < 0) {
				inserted = true;
				layerZeroEntities.add(i, entity);
			}
		}
		if (!inserted) { //stick it at the end of the render list
			layerZeroEntities.add(entity);
		}
	}

	/**
	 * when an entity is removed from the map, tell the renderer to stop
	 * caring about it
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
