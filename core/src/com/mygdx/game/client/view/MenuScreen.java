package com.mygdx.game.client.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.client.model.GameClient;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogLevel;

/**
 * Handles rendering for the main menu which appears when the game is first started.
 * Created by elimonent on 8/18/16.
 */
public class MenuScreen extends ScreenAdapter
{
	final GameClient game;
	
	OrthographicCamera cam;
	Skin skin;
	Stage stage;
	
	public MenuScreen(final GameClient game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		//The following is used after selecting a server option to connect/create a server
		TextArea portEntry = new TextArea("", skin);
		TextArea ipEntry = new TextArea("", skin);
		TextArea usernameEntry = new TextArea("", skin);
		final Label port = new Label("Port: ", skin);
		final Label ip = new Label("IP: ", skin);
		final Label username = new Label("Username: ", skin);
		final Table serverInfo = new Table();
		TextButton connectButton = new TextButton("Connect", skin);
		
		
		final Label topPane = new Label("Select a Server option", skin);
		final HorizontalGroup botPane = new HorizontalGroup();
		final TextButton hostButton = new TextButton("Host Server", skin, "default");
		hostButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				serverInfo.clearChildren();
				
            }
		});
		
		final TextButton joinButton = new TextButton("Join Server", skin, "default");
		joinButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new LobbyScreen(game));
            }
		});
		botPane.addActor(hostButton);
		botPane.addActor(joinButton);
		final SplitPane pane = new SplitPane(topPane, botPane, true, skin);
		pane.setFillParent(true);
		
		stage.addActor(pane);
		Gdx.input.setInputProcessor(stage);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        stage.draw();
        game.batch.end();
		
	}
}
