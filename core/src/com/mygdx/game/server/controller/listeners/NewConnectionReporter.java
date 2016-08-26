package com.mygdx.game.server.controller.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.util.SingletonGUIConsole;

/**
 * Listens for new connections and reports them in the in-game console.
 * Created by elimonent on 8/25/16.
 */
public class NewConnectionReporter extends Listener {

	@Override
	public void connected (Connection connection) {
		SingletonGUIConsole.getInstance().log("Player connected to Server: " + connection);
	}
}
