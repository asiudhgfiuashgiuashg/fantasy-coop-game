package com.mygdx.game.client.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.client.model.GameClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

/**
 * Handles rendering for the lobby.
 *
 * Created by elimonent on 8/18/16.
 */
public class LobbyScreen extends DebuggableScreen {
	final GameClient game;
	Skin skin;
	ScrollPane scrollPane;
	
	OrthographicCamera cam;
	
	public LobbyScreen(GameClient game, InputMultiplexer inputMultiplexer) {
		this.game = game;
		this.inputMultiplexer = inputMultiplexer;

		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		stage = new Stage();
		inputMultiplexer.addProcessor(stage);
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		
		Table players = new Table();
		String p1Status = "not ready";
		Label p1 = new Label("Rob: " + p1Status, skin);
		Label p2 = new Label("Stan: ready", skin);
		Label p3 = new Label("Tyler: ready", skin);
		
		players.add(p1);
		players.row();
		players.add(p2);
		players.row();
		players.add(p3);
		players.row();
		
		Table log = new Table(skin);
		log.add("Rob: The hell are we doing?");
		log.row();
		log.add("Stan: The fuck if I know..");
		log.row();
		log.add("Tyler: Prem's a fucking idiot");
		log.row();
		log.add("Rob: Who's that?");
		log.row();
		log.add("Tyler: get the fuck off my back nigga.");
		log.row();
		log.add("Rob: Well fuck you too then");
		log.row();
		
		ScrollPane chat = new ScrollPane(log, skin);
		TextArea chatField = new TextArea("", skin);
		log.add(chatField);
		SplitPane splitpane = new SplitPane(players, chat, true, skin);
		
		splitpane.setFillParent(true);
		stage.addActor(splitpane);
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        cam.update();
        
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.end();
        
        stage.draw();
	}
	
	@Override
	public void toggleDebug() {

	}
}
