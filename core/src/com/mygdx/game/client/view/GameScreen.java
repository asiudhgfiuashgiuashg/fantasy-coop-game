package com.mygdx.game.client.view;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.client.model.GameClient;

/**
 * Handles rendering in-game.
 *
 */
public class GameScreen extends DebuggableScreen {
	private CustomTiledMapRenderer renderer;
	private static final float MAP_SCALE = 4f; // how much to scale polygons,
	// tiles, etc. ex) A scale of 2.0 means that every pixel in a loaded image
	// will take up 2 pixels in the game window.
	private OrthographicCamera camera;

	public GameScreen(TiledMap map, RayHandler rayHandler) {
		renderer = new CustomTiledMapRenderer(map, MAP_SCALE, rayHandler);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameClient.SCREEN_WIDTH,
				GameClient.SCREEN_HEIGHT);
		camera.update();
	}

	@Override
	public void render(float delta) {
		// do the drawing here
		// for example, batch.draw(textureregion, x, y);
		renderer.setView(camera);
		renderer.render();
	}

	@Override
	public void toggleDebug() {
		renderer.toggleDebug();
	}

}
