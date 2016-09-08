package com.mygdx.game.shared.util.network.messages.lobby;

/**
 * Sent from Client -> Server after connecting to server to tell server what chosen username is.
 * Created by elimonent on 9/8/2016.
 */
public class ChooseUsernameMessage {
    private String username;

    public ChooseUsernameMessage(String username) {
        this.username = username;
    }

    /**
     * required by kryo
     */
    public ChooseUsernameMessage() {

    }
}
