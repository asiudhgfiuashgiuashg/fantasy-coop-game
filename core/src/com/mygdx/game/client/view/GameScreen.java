package com.mygdx.game.client.view;

import box2dLight.RayHandler;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.game.client.model.GameClient;


	
public class GameScreen extends DebuggableScreen {
	private CustomTiledMapRenderer renderer;
	private static final float MAP_SCALE = 4f; // how much to scale polygons,
	// tiles, etc. ex) A scale of 2.0 means that every pixel in a loaded image
	// will take up 2 pixels in the game window.
	private OrthographicCamera camera;
	final GameClient game;

	Skin skin;
	ScrollPane scrollPane;
	
	
	public GameScreen(final GameClient game, TiledMap map, RayHandler rayHandler, InputMultiplexer inputMultiplexer) {
		this.game = game;
		this.inputMultiplexer = inputMultiplexer;
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		inputMultiplexer.addProcessor(stage);
		
		renderer = new CustomTiledMapRenderer(map, MAP_SCALE, rayHandler);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameClient.SCREEN_WIDTH,
				GameClient.SCREEN_HEIGHT);
		camera.update();
		
		//Setting up a ScrollPane before adding it to the stage as an example of how it works.
		final TextField text = new TextField("Hit X!", skin);
		
		List<String> list = new List<String>(skin);
		list.setItems("People to take out: ", "Roger", "Michael", "Richard", "Roger", "Mother fkin Roger!");
		list.getSelection().setMultiple(true);
		list.getSelection().setRequired(false);
		
		scrollPane = new ScrollPane(list, skin);
		stage.addActor(text);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show()
	{
		//friend.play();
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
     
        camera.update();
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.end();
       
        
		renderer.setView(camera);
		renderer.render();
        
        stage.draw(); 
		
	}
	
	@Override
	public void toggleDebug() {
		renderer.toggleDebug();
	}



	
}
