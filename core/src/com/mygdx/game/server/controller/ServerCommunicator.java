package com.mygdx.game.server.controller;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.server.model.GameMap;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.DynamicEntity;
import com.mygdx.game.server.model.entity.player.Player;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.controller.Communicator;
import com.mygdx.game.shared.model.lobby.LobbyPlayer;
import com.mygdx.game.shared.model.lobby.PlayerClass;
import com.mygdx.game.shared.network.GameMessage;
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
					msg.uid = lobbyPlayer.getUid();
					msg.username = lobbyPlayer.getUsername();
					msg.playerClass = lobbyPlayer.getPlayerClass();
					sendTo(msg, connection.getID());
				}

				ServerLobbyPlayer newLobbyPlayer = new ServerLobbyPlayer(connection);
				manager.addLobbyPlayer(newLobbyPlayer);

				// Send everyone else the uid of the player who just connected
				// to the lobby.
				// This uid will be referenced when providing info updates like
				// username in the lobby's future
				LobbyPlayerInfoMessage msg = new LobbyPlayerInfoMessage();
				msg.uid = newLobbyPlayer.getUid();
				sendToAllExcept(msg, connection.getID());

				// send the newly-connected player all previous chat messages.
				for (ChatMessage chatMsg : manager.getChatMessages()) {
					sendTo(chatMsg, connection.getID());
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


	/**
	 * send a message to everyone
	 * @param msg
	 */
	public void sendToAll(Message msg) {
		server.sendToAllTCP(msg);
	}

	/**
	 * send a message to everyone except a certain uid
	 * @param msg
	 * @param exceptId
	 */
	public void sendToAllExcept(Message msg, int exceptId) {
		server.sendToAllExceptTCP(exceptId, msg);
	}

	/**
	 * send a message to a single connection
	 * @param msg
	 * @param recipientId
	 */
	public void sendTo(Message msg, int recipientId) {
		server.sendToTCP(recipientId, msg);
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
				sendToAllExcept(msg, msg.uid);
			}

			if (msg instanceof ChooseUsernameMessage) {
				// Alert everyone else that a player connected with the given username
				ServerLobbyPlayer player = manager.getByUid(msg.uid);
				player.setUsername(((ChooseUsernameMessage) msg).username);
				sendToAllExcept(msg, msg.uid);
			}

			// a player has requested a class assignment
			if (msg instanceof ClassAssignmentMessage) {
				System.out.println("class assignment requested");
				// Check if this class is available
				PlayerClass requestedClass = ((ClassAssignmentMessage) msg).playerClass;
				if (manager.classNotTakenYet(requestedClass)) {
					// Tell the player they got the class they wanted
					ServerLobbyPlayer player = manager.getByUid(msg.uid);
					player.setPlayerClass(requestedClass);
					sendTo(msg, msg.uid);
					
					// And tell everyone else too
					OtherClassAssignmentMessage otherMsg = new OtherClassAssignmentMessage();
					// msg.uid (the requesting connection)
					otherMsg.playerClass = requestedClass;
					otherMsg.uid = msg.uid;
					sendToAllExcept(otherMsg, msg.uid);
					System.out.println("requested class granted");
				}
			}
			
			if (msg instanceof ReadyStatusMessage) {
				// Set player ready status
				ServerLobbyPlayer player = manager.getByUid(msg.uid);
				player.setReady(((ReadyStatusMessage) msg).ready);
				
				// Tell everyone else
				sendToAllExcept(msg, msg.uid);

				System.out.println("got ready message");
				manager.checkForGameStart();
			}

			if (msg instanceof GameMessage.PosUpdateMessage) {
				GameMessage.PosUpdateMessage posMsg = (GameMessage.PosUpdateMessage) msg;
				Vector2 position = posMsg.position;
				GameMap map = GameServer.getInstance().getMap();

				int cUid = msg.uid;
				//System.out.println("cuid: " + cUid);
				Player toUpdate = getPlayerMatchingConnectionUid(cUid, map);
				toUpdate.setVelocity(posMsg.velocity);
				toUpdate.setPosition(position);
				posMsg.entityUID = toUpdate.getUid();
				posMsg.visLayer = 0;
				sendToAllExcept(posMsg, cUid);
			}

			if (msg instanceof GameMessage.AnimationUpdateMessage) {
				GameMessage.AnimationUpdateMessage animMsg = (GameMessage.AnimationUpdateMessage) msg;
				int cUid = msg.uid;
				GameMap map = GameServer.getInstance().getMap();
				animMsg.entityUID = getPlayerMatchingConnectionUid(cUid, map).getUid();
				sendToAllExcept(animMsg, cUid);
			}

			if (msg instanceof GameMessage.AttackMessage) {
				int cUid = msg.uid;
				GameMessage.AttackMessage atkMsg = (GameMessage.AttackMessage) msg;
				GameMap map = GameServer.getInstance().getMap();
				Player player = getPlayerMatchingConnectionUid(cUid, map);
				player.attack(atkMsg.destination);
			}

			if (msg instanceof GameMessage.InteractionMsg) {
				GameMessage.InteractionMsg intMsg = (GameMessage.InteractionMsg) msg;
				DynamicEntity target = GameServer.getInstance().getMap().getDynamicEntityByUid(intMsg.entityUID);
				int cUid = msg.uid; // the connection uid of the player who interacted with something
				GameMap map = GameServer.getInstance().getMap();
				Player player = getPlayerMatchingConnectionUid(cUid, map);
				if (target != null) {
					target.onInteractionFrom(player);
				}
			}
		}
	}

	/**
	 *
	 * @param cUid connection uid
	 * @param map
	 * @return
	 */
	private Player getPlayerMatchingConnectionUid(int cUid, GameMap map) {
		if (map.rangerPlayer != null && map.rangerPlayer.connectionUid == cUid) {
			return map.rangerPlayer;
		}
		if (map.magePlayer != null && map.magePlayer.connectionUid == cUid) {
			return map.magePlayer;
		}

		return map.shieldPlayer;
	}

	public void cleanup() {
		server.stop();
	}
}
