package com.mygdx.game.client.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.shared.util.ConcreteCommandExecutor;
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
	final Stage stage;
	
	public MenuScreen(final GameClient game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		//The following is used after selecting a server option to connect/create a server
		final TextField portEntry = new TextField("", skin);
		final TextField ipEntry = new TextField("", skin);
		final TextField usernameEntry = new TextField("", skin);
		final Label port = new Label("Port: ", skin);
		final Label ip = new Label("IP: ", skin);
		final Label username = new Label("Username: ", skin);
		final Table serverInfo = new Table();
		final Dialog diagBox = new Dialog("Server info", skin);
		diagBox.addActor(serverInfo);
		diagBox.setWidth(250);
		diagBox.setHeight(250);
		diagBox.setPosition(150, 150);
		final TextButton connectButton = new TextButton("Connect", skin);
		final TextButton cancelButton = new TextButton("Cancel", skin);
		final HorizontalGroup serverButtons = new HorizontalGroup();
		serverButtons.addActor(connectButton);
		serverButtons.addActor(cancelButton);
		serverButtons.padLeft(70f);
		
		final Label topPane = new Label("Select a Server option", skin);
		final HorizontalGroup botPane = new HorizontalGroup();
		final TextButton hostButton = new TextButton("Host Server", skin, "default");
		hostButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				serverInfo.clearChildren();
				serverInfo.add(username);
				serverInfo.add(usernameEntry);
				serverInfo.row();
				serverInfo.add(port);
				serverInfo.add(portEntry);
				serverInfo.row();
				serverInfo.addActor(serverButtons);
				serverInfo.setFillParent(true);
				
				connectButton.clearListeners();
				connectButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						try {
							game.hostServer(Integer.parseInt(portEntry.getText()), usernameEntry.getText());
						} catch (AlreadyConnectedException e){
							System.out.println("Neo, you're already in the Matrix");
						} catch (ServerAlreadyInitializedException e) {
							System.out.println("The world already exists, find a new one... scrub");
						}
						
						if (game.isConnected()) {
							game.setScreen(new LobbyScreen(game));
						}
						
					}
				});
				
				stage.addActor(diagBox);
            }
		});
		
		final TextButton joinButton = new TextButton("Join Server", skin, "default");
		joinButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				serverInfo.clearChildren();
				serverInfo.add(username);
				serverInfo.add(usernameEntry);
				serverInfo.row();
				serverInfo.add(port);
				serverInfo.add(portEntry);
				serverInfo.row();
				serverInfo.add(ip);
				serverInfo.add(ipEntry);
				serverInfo.row();
				serverInfo.addActor(serverButtons);
				serverInfo.setFillParent(true);
				
				connectButton.clearListeners();
				connectButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						try {
							game.connect(ipEntry.getText(), Integer.parseInt(portEntry.getText()), usernameEntry.getText());
						} catch (AlreadyConnectedException e) {
							System.out.println("Neo, you're already in the Matrix");
						}
						
						if (game.isConnected()) {
							game.setScreen(new LobbyScreen(game));
						}
					}
				});
				
				stage.addActor(diagBox);
            }
		});
		botPane.addActor(hostButton);
		botPane.addActor(joinButton);
		final SplitPane pane = new SplitPane(topPane, botPane, true, skin);
		pane.setFillParent(true);
		
		cancelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				stage.addActor(pane);
			}
		});
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
        game.batch.begin();
        game.batch.setProjectionMatrix(cam.combined);
        
        stage.draw();
        game.batch.end();
		
	}
}
