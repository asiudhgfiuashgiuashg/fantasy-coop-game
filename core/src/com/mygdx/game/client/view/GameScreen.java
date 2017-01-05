package com.mygdx.game.client.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

/**
 * Handles rendering in-game.
 *
 * Created by elimonent on 8/18/16.
 */
public class GameScreen extends ScreenAdapter
{
	final GameClient game;
	
	Texture oldMan;
	Music friend;
	Stage stage;
	Skin skin;
	ScrollPane scrollPane;
	
	OrthographicCamera cam;
	
	public GameScreen(final GameClient game) {
		this.game = game;
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		stage = new Stage();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		cam.zoom = 2.5f; 
		
		oldMan = new Texture(Gdx.files.internal("old-man.jpg"));
		friend = Gdx.audio.newMusic(Gdx.files.internal("friend in me.mp3"));
		
		final TextField text = new TextField("Hit X!", skin);
		
		List<String> list = new List<String>(skin);
		list.setItems("People to take out: ", "Roger", "Michael", "Richard", "Roger", "Mother fkin Roger!");
		list.getSelection().setMultiple(true);
		list.getSelection().setRequired(false);
		
		scrollPane = new ScrollPane(list, skin);
		stage.addActor(text);
		Gdx.input.setInputProcessor(stage);
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
        
        cam.zoom -= .001f;
        if (cam.zoom <= 0) {
        	cam.rotate(.5f, 0, 0, 10);
        	cam.translate(0, .3f);
        }
        cam.update();
        
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(oldMan, 400 - oldMan.getWidth()/2, 240 - oldMan.getHeight()/2);
        game.batch.end();
        
        if (Gdx.input.isKeyPressed(Keys.X)) {
        	stage.addActor(scrollPane);
        }
        
        stage.draw(); 
		
	}


	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose()
	{
		oldMan.dispose();
		friend.dispose();
	}
	
	
}
