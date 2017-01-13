package com.mygdx.game.client.controller;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.shared.controller.Communicator;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.mygdx.game.shared.network.LobbyMessage.ChooseUsernameMessage;
import com.mygdx.game.shared.network.LobbyMessage.ClassAssignmentMessage;
import com.mygdx.game.shared.network.LobbyMessage.GameStartMessage;
import com.mygdx.game.shared.network.LobbyMessage.LobbyPlayerInfoMessage;
import com.mygdx.game.shared.network.LobbyMessage.OtherClassAssignmentMessage;
import com.mygdx.game.shared.network.LobbyMessage.ReadyStatusMessage;
import com.mygdx.game.shared.network.Message;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;

public class ClientCommunicator extends Communicator {
	private static final GameClient gameClient = GameClient.getInstance();
	private static final SingletonGUIConsole console = SingletonGUIConsole.getInstance();
	private ClientLobbyManager manager;

	private static final int CONNECT_TIMEOUT = 5000; // timeout for connecting
														// client to server.
	public static final String LOCAL_HOST = "127.0.0.1";

	private Client client;
	
	public ClientCommunicator() {
		client = new Client();
		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if (!(object instanceof Message)) {
					// TODO: some sort of error here?
					return;
				}
				
				Message msg = (Message) object;
				incomingBuffer.add(msg);
			}
		});
		
		Kryo kryo = client.getKryo();
		kryo.setRegistrationRequired(false); // automatic registration of
												// objects in kryo (which
												// enables them to be
												// serialized/deserialized)
	}
	
	@Override
	public void sendMessages() {
		while (!outgoingBuffer.isEmpty()) {
			Message msg = outgoingBuffer.poll();
			if (msg == null) {
				return;
			}
			
			client.sendTCP(msg);
		}
	}

	@Override
	public void readMessages() {
		while (!incomingBuffer.isEmpty()) {
			Message msg = incomingBuffer.poll();
			if (msg == null) {
				return;
			}
			
			/* PROCESS MESSAGES */
			
			if (msg instanceof LobbyPlayerInfoMessage) {
				LobbyPlayerInfoMessage infoMsg = (LobbyPlayerInfoMessage) msg;
				console.log("Added player to lobby with uid " + infoMsg.uid);
		        ClientLobbyPlayer lobbyPlayer = new ClientLobbyPlayer(infoMsg.uid, infoMsg.username, infoMsg.playerClass);
		        manager.addLobbyPlayer(lobbyPlayer);
			}
			
			if (msg instanceof ClassAssignmentMessage) {
				ClassAssignmentMessage classMsg = (ClassAssignmentMessage) msg;
				console.log("Got a class assignment for myself: " + classMsg.playerClass);
		        manager.getLocalLobbyPlayer().setPlayerClass(classMsg.playerClass);
			}
			
			if (msg instanceof OtherClassAssignmentMessage) {
				OtherClassAssignmentMessage classMsg = (OtherClassAssignmentMessage) msg;
				console.log("Got a class assignment message for some lobby player besides myself: "
		                + classMsg.playerClass + "/" + "uid:" + classMsg.uid);
		        manager.getByUid(classMsg.uid).setPlayerClass(classMsg.playerClass);
			}
			
			if (msg instanceof ChooseUsernameMessage) {
				ChooseUsernameMessage usrMsg = (ChooseUsernameMessage) msg;
				ClientLobbyPlayer lobbyPlayer = manager.getByUid(usrMsg.uid);
				lobbyPlayer.setUsername(usrMsg.username);
			}
			
			if (msg instanceof ReadyStatusMessage) {
				ReadyStatusMessage rdyMsg = (ReadyStatusMessage) msg;
				ClientLobbyPlayer lobbyPlayer = manager.getByUid(rdyMsg.uid); //find out who the uid refers to
		        lobbyPlayer.setReady(rdyMsg.ready);
			}
			
			if (msg instanceof GameStartMessage) {
				gameClient.transitionToInGame();
			}
			
			if (msg instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage) msg;
				String username = manager.getByUid(chatMsg.uid).getUsername();
				String chatBoxStr = username + ": " + chatMsg.message;
		        console.log("CHAT: " + chatBoxStr);
		        gameClient.getLobbyManager().addChatMessage(chatMsg); //save the chat message
			}
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public void disconnect() {
		client.stop();
	}

	public void host(int tcpPort) throws ServerAlreadyInitializedException {
		GameServer.getInstance().init(tcpPort);
	}

	public void connect(String ip, int tcpPort, String username) throws AlreadyConnectedException {
		if (null != client && client.isConnected() == true) {
			throw new AlreadyConnectedException();
		}

		
		client.start();
		try {
			client.connect(CONNECT_TIMEOUT, ip, tcpPort);
			console.log("Connected to server", LogLevel.SUCCESS);
			
			ClientLobbyPlayer player = new ClientLobbyPlayer(ClientLobbyPlayer.LOCAL_PLAYER_UID, username);
			manager = gameClient.getLobbyManager();
			manager.addLobbyPlayer(player);
			manager.setLocalLobbyPlayer(player);
			
			ChooseUsernameMessage msg = new ChooseUsernameMessage();
			msg.username = username;
			queueMessage(msg);
		} catch (IOException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		}
	}

}
