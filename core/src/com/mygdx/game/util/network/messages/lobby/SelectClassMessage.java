package com.mygdx.game.util.network.messages.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Represents a network message for class selection sent from client -> server
 * Created by elimonent on 8/26/16.
 */
public class SelectClassMessage {
	private PlayerClassEnum playerClass;
	public SelectClassMessage(PlayerClassEnum playerClass) {
		this.playerClass = playerClass;
	}

	/**
	 * required for Kryo deserialization to work
	 * @return
	 */
	public SelectClassMessage() {

	}

	public PlayerClassEnum getPlayerClass() {
		return playerClass;
	}
}
