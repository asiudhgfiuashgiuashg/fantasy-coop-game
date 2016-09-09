package com.mygdx.game.shared.util.network.messages.lobby;

/**
 * Sent from Client -> Server after connecting to server to tell server what chosen username is.
 * Created by elimonent on 9/8/2016.
 */
public class ChooseUsernameMsg {
    private String username;

    public ChooseUsernameMsg(String username) {
        this.username = username;
    }

    /**
     * required by kryo
     */
    public ChooseUsernameMsg() {

    }

    public String getUsername() {
        return username;
    }
}
