package com.mygdx.game.shared.model;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

import java.util.Collections;
import java.util.List;

/**
 * Created by elimonent on 9/7/2016.
 */
public abstract class LobbyManager<T extends LobbyPlayer> {
    protected List<T> lobbyPlayers;

    /**
     * add a new player to the list of lobbyPlayers and do related things
     * The server and client will override this and add functionality
     */
    public void addLobbyPlayer(T player) {
        lobbyPlayers.add(player);
    }

    /**
     * @return an unmodifiable list of the lobby players, so that programmers don't try
     *  to add a player directly to the list instead of going through addLobbyPlayer();
     */
    public List<T> getLobbyPlayers() {
        return Collections.unmodifiableList(lobbyPlayers);
    }

    public boolean classNotTakenYet(PlayerClassEnum requestedClass) {
        for (T player: lobbyPlayers) {
            if (player.playerClass == requestedClass) {
                return false;
            }
        }
        return true;
    }
}
