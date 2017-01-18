package com.mygdx.game.server.controller;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.controller.Communicator;
import com.mygdx.game.shared.model.lobby.LobbyPlayer;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.mygdx.game.shared.network.LobbyMessage.ChooseUsernameMessage;
import com.mygdx.game.shared.network.LobbyMessage.ClassAssignmentMessage;
import com.mygdx.game.shared.network.LobbyMessage.OtherClassAssignmentMessage;
import com.mygdx.game.shared.network.LobbyMessage.ReadyStatusMessage;
import com.mygdx.game.shared.network.LobbyMessage.LobbyPlayerInfoMessage;
import com.mygdx.game.shared.network.Message;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.strongjoshua.console.LogLevel;

public class ServerCommunicator extends Communicator {
	public static final int DEFAULT_PORT = 63332;

	private GameServer gameServer = GameServer.getInstance();
	private static final SingletonGUIConsole console = SingletonGUIConsole.getInstance();
	private final ServerLobbyManager manager = gameServer.getLobbyManager();
	
	private Server server;
	
	public ServerCommunicator(int port) {
		server = new Server();
		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				console.log("Player connected to Server: " + connection);

				// send the new player the info of every other
				// currently-connected player.
				for (LobbyPlayer lobbyPlayer : manager.getLobbyPlayers()) {
					LobbyPlayerInfoMessage msg = new LobbyPlayerInfoMessage();
					msg.recipient = connection.getID();
					msg.uid = lobbyPlayer.getUid();
					msg.username = lobbyPlayer.getUsername();
					msg.playerClass = lobbyPlayer.getPlayerClass();
					queueMessage(msg);
				}

				ServerLobbyPlayer newLobbyPlayer = new ServerLobbyPlayer(connection);
				manager.addLobbyPlayer(newLobbyPlayer);

				// Send everyone else the uid of the player who just connected
				// to the lobby.
				// This uid will be referenced when providing info updates like
				// username in the lobby's future
				LobbyPlayerInfoMessage msg = new LobbyPlayerInfoMessage();
				msg.recipient = connection.getID();
				msg.uid = newLobbyPlayer.getUid();
				msg.except = true;
				queueMessage(msg);

				// send the newly-connected player all previous chat messages.
				for (ChatMessage chatMsg : manager.getChatMessages()) {
					chatMsg.recipient = connection.getID();
					chatMsg.except = false;
					queueMessage(chatMsg);
				}
				System.out.println("connection id: " + connection.getID() +
						" connected");
			}

			@Override
			public void received(Connection connection, Object object) {
				if (!(object instanceof Message)) {
					// TODO: some sort of error here?
					return;
				}
				
				Message msg = (Message) object;
				msg.uid = connection.getID();
				incomingBuffer.add(msg);
			}
		});

		Kryo kryo = server.getKryo();
		// Automatic registration (enables them to be serialized/deserialized)
		kryo.setRegistrationRequired(false);
		server.start();

		try {
			server.bind(port);
			console.log("Server started", LogLevel.SUCCESS);
		} catch (IOException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessages() {
		while (!outgoingBuffer.isEmpty()) {
			Message msg = outgoingBuffer.poll();
			if (msg == null)
				return;

			if (msg.recipient == 0) {
				server.sendToAllTCP(msg);
			} else {
				if (msg.except) {
					server.sendToAllExceptTCP(msg.recipient, msg);
				} else {
					server.sendToTCP(msg.recipient, msg);
				}
			}
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
			
			if (msg instanceof ChatMessage) {
				manager.addChatMessage((ChatMessage) msg);
				// Send chat message to everybody except the sender
				msg.recipient = msg.uid; 
				msg.except = true;
				queueMessage(msg);
			}
			
			if (msg instanceof ChooseUsernameMessage) {
				// Alert everyone else that a player connected with the given username
				ServerLobbyPlayer player = manager.getByUid(msg.uid);
				player.setUsername(((ChooseUsernameMessage) msg).username);
				msg.recipient = msg.uid;
				msg.except = true;
				queueMessage(msg);
			}
			
			if (msg instanceof ClassAssignmentMessage) {
				// Check if this class is available
				PlayerClass requestedClass = ((ClassAssignmentMessage) msg).playerClass;
				if (manager.classNotTakenYet(requestedClass)) {
					// Tell the player they got the class they wanted
					ServerLobbyPlayer player = manager.getByUid(msg.uid);
					player.setPlayerClass(requestedClass);
					queueMessage(msg);
					
					// And tell everyone else too
					OtherClassAssignmentMessage otherMsg = new OtherClassAssignmentMessage();
					otherMsg.recipient = msg.uid;
					otherMsg.except = true;
					otherMsg.playerClass = requestedClass;
					queueMessage(otherMsg);
				}
			}
			
			if (msg instanceof ReadyStatusMessage) {
				// Set player ready status
				ServerLobbyPlayer player = manager.getByUid(msg.uid);
				player.setReady(((ReadyStatusMessage) msg).ready);
				
				// Tell everyone else
				msg.recipient = msg.uid;
				msg.except = true;
				queueMessage(msg);
				
				manager.checkForGameStart();
			}
		}
	}
}
