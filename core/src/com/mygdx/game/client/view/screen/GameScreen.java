package com.mygdx.game.client.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.view.renderer.CustomTiledMapRenderer;


public class GameScreen extends DebuggableScreen {
	private final GameClient game;
	private CustomTiledMapRenderer renderer;
	/** Game camera. Controls the part of the game map being shown via position and zoom */
	private OrthographicCamera camera;
	
	public GameScreen(Viewport gameViewport, Viewport uiViewport, Batch batch, CustomTiledMapRenderer renderer) {
		super(uiViewport, batch);
		this.camera = (OrthographicCamera) gameViewport.getCamera();
		this.renderer = renderer;
		game = GameClient.getInstance();
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		// TEST: Just an example of positioning the camera; completely arbitrary
		camera.position.x = 120;
		camera.position.y = 120;
		camera.zoom = 1.2f;
		
		//Setting up a ScrollPane before adding it to the stage as an example
		// of how it works.
		final TextField text = new TextField("Hit X!", skin);
		text.setPosition(0, 0);

		stage.addActor(text);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// Update camera projection matrix
		camera.update();

		// Render based on game camera position and zoom
		renderer.setView(camera);
		renderer.render();
		
		// Draw UI elements (HUD)
		stage.draw();
	}
	
	@Override
	public void toggleDebug() {
		renderer.toggleDebug();
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
	}
	
}
