package com.mygdx.game.client.view;

import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.shared.model.lobby.LobbyPlayer;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.mygdx.game.shared.network.LobbyMessage.ClassAssignmentMessage;
import com.mygdx.game.shared.network.LobbyMessage.ReadyStatusMessage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Handles rendering for the lobby.
 *
 * Created by elimonent on 8/18/16.
 */
public class LobbyScreen extends DebuggableScreen {
	final GameClient game;
	ClientLobbyManager lobby;
	Skin skin;		//Default skin.
	Table players; //should rename this. Might get confusing due to the number of variables with player in their name.
	Label [] labels;	//holds the player name and status
	ScrollPane chat;	//This holds the log and the chatField for the chat system
	Table log;		//This holds the chat log
	TextField chatField;	//This is the chat bar in the chat system
	TextButton startButton;
	
	OrthographicCamera cam;
	
	public LobbyScreen(GameClient gameTemp, InputMultiplexer inputMultiplexer) {
		game = gameTemp;
		this.inputMultiplexer = inputMultiplexer;
		lobby = game.getLobbyManager();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		stage = new Stage();
		stage.setDebugAll(true);
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
		chat = new ScrollPane(log, skin);

		updateChat();
		
		VerticalGroup lobbyOptions = new VerticalGroup();
		HorizontalGroup serverOptions = new HorizontalGroup();
		
		TextButton readyButton = new TextButton("Ready", skin);
		readyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				readyButtonPressed();
			}
		});		
		startButton = new TextButton("Start Game", skin);
		startButton.setTouchable(Touchable.disabled);
		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.transitionToInGame();
			}
		});
		serverOptions.addActor(readyButton);
		serverOptions.addActor(startButton);
		
		//Sets up the class selection buttons
		HorizontalGroup classOptions = new HorizontalGroup();
		Label classLabel = new Label("Class: ", skin);
		TextButton rangerButton = new TextButton("Ranger Guy", skin);
		rangerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changePlayerClass(PlayerClass.RANGER);
			}
		});
		TextButton shieldButton = new TextButton("Shield Guy", skin);
		shieldButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changePlayerClass(PlayerClass.SHIELD);
			}
		});
		TextButton mageButton = new TextButton("Mage Guy", skin);
		mageButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changePlayerClass(PlayerClass.MAGE);
			}
		});
		classOptions.addActor(classLabel);
		classOptions.addActor(rangerButton);
		classOptions.addActor(shieldButton);
		classOptions.addActor(mageButton);
		
		lobbyOptions.addActor(classOptions);
		lobbyOptions.addActor(serverOptions);
		
		
		SplitPane topSplitPane = new SplitPane(players, lobbyOptions, false, skin);
		
		
		SplitPane mainSplitPane = new SplitPane(topSplitPane, chat, true, skin);
		
		
		mainSplitPane.setFillParent(true);
		stage.addActor(mainSplitPane);
		
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
        
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.end();
        
        stage.draw();
        stage.act();
	}
	
	
	@Override
	public void updateUI() {
		updatePlayers();
		updateChat();
		isLobbyReady();
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
			line += "    Class: " + currentPlayer.getPlayerClass();
			labels[counter].setText(line);
			counter++;
		}
	}
	
	public void updateChat() {
		ListIterator<ChatMessage> chatIterator = lobby.getChatMessages().listIterator();
		log.clearChildren();
		while (chatIterator.hasNext()) {
			ChatMessage chatLine = chatIterator.next();
			log.add(lobby.getByUid(chatLine.uid).getUsername() + ": " + chatLine.message);
			log.row();
		}
		log.add(chatField);
		log.row();
		chat.scrollTo(0, 0, 800, 50);
	}
	
	public void sendMessage() {
		ChatMessage message = new ChatMessage();
		message.message = chatField.getText();
		message.uid = -1;
        game.sendToServer(message);
        lobby.addChatMessage(message);
		updateChat();
		chatField.setText("");
		stage.setKeyboardFocus(chatField);
	}
	
	public void readyButtonPressed() {
		ClientLobbyPlayer player = lobby.getLocalLobbyPlayer();
		if (player.isReady()) {
			player.setReady(false);
		} else {
			player.setReady(true);
		}
		sendReadyMessage(player.isReady());
	}
	
	public void sendReadyMessage(boolean ready) {
		ReadyStatusMessage message = new ReadyStatusMessage(); 
		message.ready = ready;
		message.uid = -1;
		game.sendToServer(message);
		lobby.getLocalLobbyPlayer().setReady(ready);
		updatePlayers();
		isLobbyReady();
	}
	
	/**
	 * 
	 * This method checks to see if everyone in the lobby is ready 
	 *
	 * @return returns a true or false result
	 */
	public void isLobbyReady() {
		ListIterator<ClientLobbyPlayer> playerIterator = lobby.getLobbyPlayers().listIterator();
		boolean result = true;
		while (playerIterator.hasNext()) {
			ClientLobbyPlayer player = playerIterator.next();
			if (!player.isReady()) {
				result = false;
			}
		}
		if (result) {
			startButton.setTouchable(Touchable.enabled);
		} else {
			startButton.setTouchable(Touchable.disabled);
		}
	}
	
	public void changePlayerClass(PlayerClass classType) {
		lobby.getLocalLobbyPlayer().setPlayerClass(classType);
		ClassAssignmentMessage message = new ClassAssignmentMessage();
		message.playerClass = classType;
		game.sendToServer(message);
		updatePlayers();
	}
	
	@Override
	public void toggleDebug() {

	}
}
