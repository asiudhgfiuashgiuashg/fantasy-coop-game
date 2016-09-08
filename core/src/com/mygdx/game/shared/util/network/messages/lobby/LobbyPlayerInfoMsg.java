package com.mygdx.game.shared.util.network.messages.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Sent Server -> Client after the client connects to describe a player who is in the lobby
 * Created by elimonent on 8/28/2016.
 */
public class LobbyPlayerInfoMsg {
    public int uid;
    public String username;
    public PlayerClassEnum playerClass;
    public LobbyPlayerInfoMsg() {

    }
    public LobbyPlayerInfoMsg(int uid, String username, PlayerClassEnum playerClass) {
        this.uid = uid;
        this.username = username;
        this.playerClass = playerClass;
    }
}
