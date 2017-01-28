package com.mygdx.game.client.view;

import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.shared.model.lobby.LobbyPlayer;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Handles rendering for the lobby.
 *
 * Created by elimonent on 8/18/16.
 */
public class LobbyScreen extends DebuggableScreen {
	final GameClient game;
	ClientLobbyManager lobby;
	Skin skin;
	Table players; //should rename this. Might get confusing due to the number of variables with player in their name.
	Label [] labels;	//holds the player name and status
	ScrollPane scrollPane;
	Table log;
	TextField chatField;
	
	OrthographicCamera cam;
	
	public LobbyScreen(GameClient game, InputMultiplexer inputMultiplexer) {
		this.game = game;
		this.inputMultiplexer = inputMultiplexer;
		lobby = game.getLobbyManager();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		stage = new Stage();
		inputMultiplexer.addProcessor(stage);
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		
		//The following is the setup for the list of players and their status, which is updated using the updatePlayers() function.
		//TODO: Consider changing labels to use hash? instead, where the uid of the lobbyplayer is the key, and the lobbyplayer is the value.
		//		By doing this, we can send the updatePlayers function a uid so it only updates that specific class
		players = new Table();
		labels = new Label[5];
		for (int x = 0; x < 5; x++) {
			labels[x] = new Label("", skin);
			players.add(labels[x]);
			players.row();
		}
		updatePlayers();
		
		//This sets up the chat system, which is updated using updateChat().
		log = new Table(skin);
		chatField = new TextField("", skin);
		chatField.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keyCode) {
				if (keyCode == Input.Keys.ENTER) {
					sendMessage();
					return true;
				} else {
					return false;
				}
			}
		});
		updateChat();
		log.add(chatField);
		ScrollPane chat = new ScrollPane(log, skin);
		
		
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        cam.update();
        updatePlayers();	//TODO: add a change state to the addPlayer function of the lobby, and then have a listener that would only update when it notices a change state.
        
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.end();
        
        stage.draw();
	}
	
	public void updatePlayers() {
		ListIterator<ClientLobbyPlayer> playerIterator = lobby.getLobbyPlayers().listIterator();
		
		ClientLobbyPlayer currentPlayer;
		int counter = 0;
		while (playerIterator.hasNext()) {
			currentPlayer = playerIterator.next();
			String line = currentPlayer.getUsername() + ": ";
			if (currentPlayer.isReady()) {
				line += "Ready";
			} else {
				line += "Not Ready";
			}
			labels[counter].setText(line);
			counter++;
		}
	}
	
	public void updateChat() {
		ListIterator<ChatMessage> chatIterator = lobby.getChatMessages().listIterator();
		log.clearChildren();
		while (chatIterator.hasNext()) {
			ChatMessage chatLine = chatIterator.next();
			log.add(lobby.getByUid(chatLine.uid) + ": " + chatLine.message);
			log.row();
		}
		log.add(chatField);
	}
	
	//TODO: Messages aren't being sent over the server, need to fix.
	public void sendMessage() {
		System.out.println("LobbyScreen: check");
        game.sendToServer(new ChatMessage(chatField.getText()));
		updateChat();
		chatField.setText("");
		stage.setKeyboardFocus(chatField);
	}
	
	@Override
	public void toggleDebug() {

	}
}
