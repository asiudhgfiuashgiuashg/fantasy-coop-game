package com.mygdx.game.util.network.messages;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Represents a network message for class selection sent from client -> server
 * Created by elimonent on 8/26/16.
 */
public class SelectClassMessage implements Serializeable {
	private PlayerClassEnum playerClass;
	public SelectClassMessage(PlayerClassEnum playerClass) {
		this.playerClass = playerClass;
	}
}
