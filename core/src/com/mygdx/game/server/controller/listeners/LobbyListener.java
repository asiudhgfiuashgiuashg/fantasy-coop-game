package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.server.model.lobby.ServerLobbyManager;
import com.mygdx.game.shared.model.LobbyPlayer;
import com.mygdx.game.server.model.lobby.PlayerClassEnum;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.mygdx.game.shared.util.network.messages.lobby.ClassAssignmentMsg;
import com.mygdx.game.shared.util.network.messages.lobby.OtherClassAssignmentMsg;
import com.mygdx.game.shared.util.network.messages.lobby.SelectClassMessage;

/**
 * General listener for lobby network messages.
 * Write a received ( conn, T messageOfTypeT) for each type of message you want to deal with.
 * Created by elimonent on 8/27/16.
 */
public class LobbyListener extends Listener.ReflectionListener {
	private ServerLobbyManager serverLobbyManager;
	private Server server;
	private final SingletonGUIConsole console = SingletonGUIConsole.getInstance();

	public LobbyListener(ServerLobbyManager serverLobbyManager, Server server) {
		this.serverLobbyManager = serverLobbyManager;
		this.server = server;
	}
	public void received(Connection connection, SelectClassMessage message) {
		console.log("Received a class change request from " + connection);
		console.log("Requested class: " + message.getPlayerClass());

		//check if this class is available, and send a class assignment message to everyone if it is.
		PlayerClassEnum requestedClass = message.getPlayerClass();
		if (serverLobbyManager.classNotTakenYet(requestedClass)) {
			LobbyPlayer player = serverLobbyManager.getPlayerByConnection(connection);
			player.playerClass = requestedClass;
			server.sendToAllExceptTCP(connection.getID(),
					new OtherClassAssignmentMsg(requestedClass, player.getUid())); //let everyone else know that this lobby player has been assigned this class (which they requested)
			server.sendToTCP(connection.getID(), new ClassAssignmentMsg(player.playerClass));
			SingletonGUIConsole.getInstance().log("Assigned " + requestedClass + " to " + connection);
		}
	}
}
