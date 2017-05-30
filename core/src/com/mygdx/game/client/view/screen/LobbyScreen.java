package com.mygdx.game.client.view.screen;

import java.util.ListIterator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.LobbyMessage.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Handles rendering for the lobbyManager.
 *
 * Created by elimonent on 8/18/16.
 */
public class LobbyScreen extends DebuggableScreen {
	private final GameClient game;
	ClientLobbyManager lobbyManager;
	Skin skin;		//Default skin.
	Table playersTable;
	Label [] labels;	//holds the player name and status
	ScrollPane chat;	//This holds the log and the chatField for the chat system
	Table log;		//This holds the chat log
	TextField chatField;	//This is the chat bar in the chat system
	TextButton startButton;
	
	public LobbyScreen(Viewport viewport, Batch batch) {
		super(viewport, batch);
		game = GameClient.getInstance();
		lobbyManager = game.getLobbyManager();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		stage.setDebugAll(true);
		
		//The following is the setup for the list of playersTable and their status, which is updated using the updatePlayers() function.
		//TODO: Consider changing labels to use hash? instead, where the uid of the lobbyplayer is the key, and the lobbyplayer is the value.
		//		By doing this, we can send the updatePlayers function a uid so it only updates that specific class
		playersTable = new Table();
		labels = new Label[5];
		addTableRows();
		updatePlayers();
		
		//This sets up the chat system, which is updated using updateChat().
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
		log = new Table(skin);
		chat = new ScrollPane(log, skin);
		
		VerticalGroup lobbyOptions = new VerticalGroup();
		HorizontalGroup serverOptions = new HorizontalGroup();
		
		TextButton readyButton = new TextButton("Ready", skin);
		readyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				readyButtonPressed();
			}
		});		
		serverOptions.addActor(readyButton);
		
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
		
		
		log.setFillParent(true);
		chat.setWidth(Gdx.graphics.getWidth());
		Table lobby = new Table(skin);
		lobby.add(playersTable);
		lobby.add(lobbyOptions).height(Gdx.graphics.getHeight() * .4f).fill();
		lobby.row();
		lobby.add(chat).height(Value.percentHeight(.40f, lobby)).align(Align.left).colspan(2).expandX().bottom();
		lobby.row().fill();
		lobby.add(chatField).height(Gdx.graphics.getHeight() * .2f).width(Gdx.graphics.getWidth()).colspan(2);
		lobby.setFillParent(true);
		lobby.setWidth(Gdx.graphics.getWidth());
		lobby.setHeight(Gdx.graphics.getHeight());
		
		chat.layout();
		chat.setClamp(false);
		
		stage.addActor(lobby);
		updateUI();
		
	}

	private void addTableRows() {
		for (int x = 0; x < 5; x++) {
			labels[x] = new Label("", skin);
			playersTable.add(labels[x]);
			playersTable.row();
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{
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
		ListIterator<ClientLobbyPlayer> playerIterator = lobbyManager.getLobbyPlayers().listIterator();
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
		ListIterator<ChatMessage> chatIterator = lobbyManager.getChatMessages().listIterator();
		log.clearChildren();
		while (chatIterator.hasNext()) {
			ChatMessage chatLine = chatIterator.next();
			log.add(lobbyManager.getByUid(chatLine.uid).getUsername() + ": " + chatLine.message).width(Gdx.graphics.getWidth());
			log.row();
		}
		log.row();
		chat.layout(); 
		chat.scrollTo(chat.getX(), chat.getY(), chat.getWidth(), chat.getHeight());
		chat.updateVisualScroll();
	}
	
	public void sendMessage() {
		ChatMessage message = new ChatMessage();
		message.message = chatField.getText();
		message.uid = -1;
        game.sendToServer(message);
        lobbyManager.addChatMessage(message);
		updateChat();
		chatField.setText("");
		stage.setKeyboardFocus(chatField);
	}
	
	public void readyButtonPressed() {
		ClientLobbyPlayer player = lobbyManager.getLocalLobbyPlayer();
		if (!player.isReady() && player.getClass() != null) {
			player.setReady(true);
		} else {
			player.setReady(false);
		}
		sendReadyMessage(player.isReady());
	}
	
	public void sendReadyMessage(boolean ready) {
		ReadyStatusMessage message = new ReadyStatusMessage(); 
		message.ready = ready;
		message.uid = -1;
		game.sendToServer(message);
		lobbyManager.getLocalLobbyPlayer().setReady(ready);
		updatePlayers();
		isLobbyReady();
	}
	
	/**
	 * 
	 * This method checks to see if everyone in the lobbyManager is ready
	 *
	 * @return returns a true or false result
	 */
	public void isLobbyReady() {
		ListIterator<ClientLobbyPlayer> playerIterator = lobbyManager.getLobbyPlayers().listIterator();
		boolean result = true;
		while (playerIterator.hasNext()) {
			ClientLobbyPlayer player = playerIterator.next();
			if (!player.isReady()) {
				result = false;
			}
		}
		if (result) {
			game.sendToServer(new GameStartMessage());
		}
	}
	
	public void changePlayerClass(PlayerClass classType) {
		ClassAssignmentMessage message = new ClassAssignmentMessage();
		message.playerClass = classType;
		game.sendToServer(message);
	}
	
	@Override
	public void toggleDebug() {

	}

	/**
	 * Get rid of entries in the class selection/ready table
	 * Get rid of chat messages.
	 */
	public void clearScreen() {
		playersTable.clearChildren();
		lobbyManager.clearPlayers();
		addTableRows();
		log.clearChildren();
	}
}
