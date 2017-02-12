package com.mygdx.game.client.view.screen;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.view.CustomTiledMapRenderer;


public class GameScreen extends DebuggableScreen {
	private final GameClient game;
	private CustomTiledMapRenderer renderer;
	private RayHandler rayHandler;
	private OrthographicCamera camera;
	
	public GameScreen(Viewport gameViewport, Viewport uiViewport, Batch batch, RayHandler rayHandler, CustomTiledMapRenderer renderer) {
		super(uiViewport, batch);
		this.camera = (OrthographicCamera) gameViewport.getCamera();
		this.rayHandler = rayHandler;
		game = GameClient.getInstance();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		this.renderer = renderer;

		// TEST: Just an example of positioning the camera
		camera.position.x = GameClient.WORLD_HEIGHT / 2;
		camera.position.y = GameClient.WORLD_HEIGHT * game.getAspectRatio() / 2;
		
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		renderer.setView(camera);
		renderer.render();
		
		//rayHandler.setCombinedMatrix(camera);
		//rayHandler.updateAndRender();
		
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
