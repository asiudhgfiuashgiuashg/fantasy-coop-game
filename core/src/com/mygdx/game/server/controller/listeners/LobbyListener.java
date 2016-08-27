package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.util.SingletonGUIConsole;
import com.mygdx.game.util.network.messages.SelectClassMessage;

/**
 * General listener for lobby network messages.
 * Write a received ( conn, T messageOfTypeT) for each type of message you want to deal with.
 * Created by elimonent on 8/27/16.
 */
public class LobbyListener extends Listener.ReflectionListener {
	public void received(Connection connection, SelectClassMessage message) {
		SingletonGUIConsole.getInstance().log("Received a class change request from " + connection);
		SingletonGUIConsole.getInstance().log("Requested class: " + message.getPlayerClass());
	}
}
