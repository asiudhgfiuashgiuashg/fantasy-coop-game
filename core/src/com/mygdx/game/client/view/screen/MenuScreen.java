package com.mygdx.game.client.view.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;

/**
 * Handles rendering for the main menu which appears when the game is first started.
 */

public class MenuScreen extends DebuggableScreen {
	private final GameClient game;

	/**
	 *
	 */
	public MenuScreen(Viewport viewport, Batch batch) {
		super(viewport, batch);
		game = GameClient.getInstance();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		/* To understand better how the ui is organized, think of a hierarchy of layout widgets (e.x. SplitPane), which is what organizes everything on the screen,
		 * and widgets, which is the actual components you want to add (e.x. buttons, text fields, tables, etc..).
		 * However, Scene2d ui requires that all widgets must be instantiated from the bottom up, meaning the layout would be done after the main components.
		 * That's why buttons need to be created before SplitPanes. 
		 */
		
		//The following is used after selecting a server option to connect/create a server
		//Many of these ui components are final because I could not find a way to use them within anonymous classes (for the ClickListeners) without them being as such.
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
		diagBox.setPosition(150, 150);
		final TextButton connectButton = new TextButton("Connect", skin);
		final TextButton cancelButton = new TextButton("Cancel", skin);
		final HorizontalGroup serverButtons = new HorizontalGroup();
		serverButtons.addActor(connectButton);
		serverButtons.addActor(cancelButton);
		serverButtons.padLeft(70f);
		serverButtons.padBottom(50f);
		
		//The following creates the ui for the MenuScreen, as well as, decides what happens when the host and join buttons are clicked
		final Label topPane = new Label("Select a Server option", skin);
		final HorizontalGroup botPane = new HorizontalGroup();
		final TextButton hostButton = new TextButton("Host Server", skin, "default");
		hostButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				diagBox.setHeight(150);
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
							
							//game.setScreen(new LobbyScreen(game, inputMultiplexer));
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
				diagBox.setHeight(200);
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
							//game.setScreen(new LobbyScreen(game, inputMultiplexer));
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
		
		/*
		 * This is used to exit out of a server option and move back into the MenuScreen. Unfortunately, I couldn't find a way to remove specific actors 
		 * from the stage, so this listener must be placed
		 */
		cancelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				stage.addActor(pane);
			}
		});
		stage.addActor(pane);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{    
        stage.draw();
	}
	
	@Override
	public void toggleDebug() {
	
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
	}
}
