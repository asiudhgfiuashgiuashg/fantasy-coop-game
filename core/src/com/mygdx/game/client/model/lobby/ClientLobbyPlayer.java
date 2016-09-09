package com.mygdx.game.client.model.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;
import com.mygdx.game.shared.model.LobbyPlayer;

/**
 * Represents a lobby player in the client's model of the lobby.
 * Created by elimonent on 9/7/2016.
 */
public class ClientLobbyPlayer extends LobbyPlayer {
    public static final int LOCAL_PLAYER_UID = -1; //if a player's uid is -1, they are the local player
                                                   //The local player is never informed of their actual uid

    public ClientLobbyPlayer(int uid, String username, PlayerClassEnum playerClass) {
        this.uid = uid;
        this.username = username;
        this.playerClass = playerClass;
    }

    public ClientLobbyPlayer(int uid, String username) {
        this.uid = uid;
        this.username = username;
        this.playerClass = null;
    }
}
