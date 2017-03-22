package com.mygdx.game.shared.model.lobby;

import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.server.model.lobby.ServerLobbyPlayer;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * represents/helps manage a lobby
 * @param <T>
 */
public abstract class LobbyManager<T extends LobbyPlayer> {
    protected List<T> lobbyPlayers = new ArrayList<T>();
    protected final List<ChatMessage> chatMsgs = new ArrayList<ChatMessage>();

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

    public boolean classNotTakenYet(PlayerClass requestedClass) {
        for (T player: lobbyPlayers) {
            if (player.getPlayerClass() == requestedClass) {
                System.out.println(player.getPlayerClass());
                System.out.println(requestedClass);
                return false;
            }
        }
        return true;
    }

    public void addChatMessage(ChatMessage chatMsg) {
        this.chatMsgs.add(chatMsg);
    }

    public List<ChatMessage> getChatMessages() {
        return Collections.unmodifiableList(chatMsgs);
    }

    public T getByUid(int uid) {
        for (T lobbyPlayer: getLobbyPlayers()) {
            if (lobbyPlayer.getUid() == uid) {
                return lobbyPlayer;
            }
        }
        System.out.println("Searching for: " + uid);
        return null;
    }

    /**
     * remove lobby players
     */
    public void clearPlayers() {
        lobbyPlayers.clear();
    }

    public void clearChatMsgs() {
		chatMsgs.clear();
	}
}
