package com.mygdx.game.client.controller;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.client.model.ClientGameState;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.entity.DynamicEntity;
import com.mygdx.game.client.model.entity.player.MagePlayer;
import com.mygdx.game.client.model.entity.player.Player;
import com.mygdx.game.client.model.entity.player.RangerPlayer;
import com.mygdx.game.client.model.entity.player.ShieldPlayer;
import com.mygdx.game.client.model.exceptions.AlreadyConnectedException;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.exceptions.ServerAlreadyInitializedException;
import com.mygdx.game.shared.controller.Communicator;
import com.mygdx.game.shared.network.GameMessage;
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

	public void sendToServer(Message msg) {
		client.sendTCP(msg);
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
			} else if (msg instanceof ClassAssignmentMessage) {
				ClassAssignmentMessage classMsg = (ClassAssignmentMessage) msg;
				console.log("Got a class assignment for this client: " + classMsg.playerClass);
				manager.getLocalLobbyPlayer().setPlayerClass(classMsg.playerClass);
			} else if (msg instanceof OtherClassAssignmentMessage) {
				OtherClassAssignmentMessage classMsg = (OtherClassAssignmentMessage) msg;
				console.log("Got a class assignment message for some lobby " + "player besides myself: " + classMsg.playerClass + "/" + "uid:" + classMsg.uid);
				manager.getByUid(classMsg.uid).setPlayerClass(classMsg.playerClass);
			} else if (msg instanceof ChooseUsernameMessage) {
				ChooseUsernameMessage usrMsg = (ChooseUsernameMessage) msg;
				ClientLobbyPlayer lobbyPlayer = manager.getByUid(usrMsg.uid);
				lobbyPlayer.setUsername(usrMsg.username);
			} else if (msg instanceof ReadyStatusMessage) {
				ReadyStatusMessage rdyMsg = (ReadyStatusMessage) msg;
				ClientLobbyPlayer lobbyPlayer = manager.getByUid(rdyMsg.uid);
				// find out who the uid refers to
				lobbyPlayer.setReady(rdyMsg.ready);
			} else if (msg instanceof GameStartMessage) {
				gameClient.transitionToInGame();
				System.out.println("got game start message");
			} else if (msg instanceof ChatMessage) {
				ChatMessage chatMsg = (ChatMessage) msg;
				String username = manager.getByUid(chatMsg.uid).getUsername();
				String chatBoxStr = username + ": " + chatMsg.message;
				console.log("CHAT: " + chatBoxStr);
				manager.addChatMessage(chatMsg); // save
				// the chat message
			} else if (msg instanceof GameMessage && GameClient.getInstance().getGameState().get() == ClientGameState.GAME) {
				if (msg instanceof GameMessage.InitDynamicEntityMsg) {
					GameMessage.InitDynamicEntityMsg initMsg = (GameMessage.InitDynamicEntityMsg) msg;
					DynamicEntity entity;
					if (initMsg.isLocalPlayer) {
						String className = initMsg.className;
						Player player;
						if (className.equals("RangerPlayer")) {
							player = new RangerPlayer(initMsg.entityUID, initMsg.className, initMsg.pos, 0);
						} else if (className.equals("MagePlayer")) {
							player = new MagePlayer(initMsg.entityUID, initMsg.className, initMsg.pos, 0);
						} else { // "SHIELDPLAYER"
							player = new ShieldPlayer(initMsg.entityUID, initMsg.className, initMsg.pos, 0);
						}
						player.setVertices(initMsg.vertices);
						gameClient.addLocalPlayer(player);
						entity = player;
					} else {
						DynamicEntity newEntity = new DynamicEntity(initMsg.entityUID, initMsg.className, initMsg.pos, initMsg.visLayer);
						newEntity.setVertices(initMsg.vertices);
						newEntity.setMass(initMsg.mass);
						if (initMsg.solid) {
							newEntity.solid = true;
						}
						gameClient.addDynamicEntity(newEntity);
						entity = newEntity;
					}
					entity.setHealth(initMsg.health);
					entity.setMaxHealth(initMsg.maxHealth);
					entity.setLights(initMsg.entityLightList);
					entity.setRotation(initMsg.rotation);
					entity.setOrigin(initMsg.originX, initMsg.originY); // what point to rotate hitbox around
					entity.hasHealth = initMsg.hasHealth;

				} else if (msg instanceof GameMessage.PosUpdateMessage) {
					GameMessage.PosUpdateMessage posMsg = (GameMessage.PosUpdateMessage) msg;
					DynamicEntity entity = gameClient.getMap().getDynamicEntityByUid(posMsg.entityUID);
					if (entity != null) {
						//System.out.println("got a position upudate message for: " + entity.getUid() + " at " + posMsg.position);
						if (posMsg.position != null) {
							entity.setPosition(posMsg.position);
						}
						if (posMsg.velocity != null) {
							entity.setVelocity(posMsg.velocity);
							//System.out.println("set velocity of " + entity.getUid() + " to " + posMsg.velocity);
						}
					}


				} else if (msg instanceof GameMessage.AnimationUpdateMessage) {
					GameMessage.AnimationUpdateMessage animationUpdateMessage = (GameMessage.AnimationUpdateMessage) msg;

					DynamicEntity entity = gameClient.getMap().getDynamicEntityByUid(animationUpdateMessage.entityUID);
					if (animationUpdateMessage.animationName != null) {
						if (animationUpdateMessage.playMode == null) {
							System.out.println("NULL playmode for " + entity.getUid());
						}
						entity.setAnimation(animationUpdateMessage.animationName, animationUpdateMessage.frameDuration, animationUpdateMessage.playMode);
					}
				} else if (msg instanceof GameMessage.HitboxUpdateMessage) {
					GameMessage.HitboxUpdateMessage hitboxMsg = (GameMessage.HitboxUpdateMessage) msg;
					DynamicEntity entity = gameClient.getMap().getDynamicEntityByUid(hitboxMsg.entityUID);
					entity.setVertices(hitboxMsg.vertices);
					entity.setPosition(entity.getPos());
				} else if (msg instanceof GameMessage.HealthUpdateMsg) {
					GameMessage.HealthUpdateMsg healthMsg = (GameMessage.HealthUpdateMsg) msg;
					DynamicEntity toUpdate = gameClient.getMap().getDynamicEntityByUid(healthMsg.entityUID);
					toUpdate.setHealth(healthMsg.health);
				} else if (msg instanceof GameMessage.RemoveDynamicEntityMsg) {
					GameMessage.RemoveDynamicEntityMsg removeMsg = (GameMessage.RemoveDynamicEntityMsg) msg;
					GameClient.getInstance().getMap().removeDynamicEntityByUid(removeMsg.entityUID);
				} else if (msg instanceof GameMessage.SayMsg) {
					GameMessage.SayMsg sayMsg = (GameMessage.SayMsg) msg;
					GameClient.getInstance().getGameScreen().displayDialogue(sayMsg.dialogueLine);
				} else {
					System.out.println("unhandled network message of type " + msg.getClass());
				}
			}
			gameClient.getScreen().updateUI();
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public void disconnect() {
		client.stop();
	}

	public void host(int tcpPort) throws ServerAlreadyInitializedException {
		try {
			GameServer.getInstance().init(tcpPort, "prototypeMap.tmx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect(String ip, int tcpPort, String username) throws AlreadyConnectedException, IOException {
		if (null != client && client.isConnected() == true) {
			throw new AlreadyConnectedException();
		}

		client.start();
		client.connect(CONNECT_TIMEOUT, ip, tcpPort);
		console.log("Connected to server", LogLevel.SUCCESS);

		ClientLobbyPlayer player = new ClientLobbyPlayer(ClientLobbyPlayer.LOCAL_PLAYER_UID, username);
		manager = gameClient.getLobbyManager();
		manager.addLobbyPlayer(player);
		manager.setLocalLobbyPlayer(player);

		ChooseUsernameMessage msg = new ChooseUsernameMessage();
		msg.username = username;
		sendToServer(msg);
	}

}
