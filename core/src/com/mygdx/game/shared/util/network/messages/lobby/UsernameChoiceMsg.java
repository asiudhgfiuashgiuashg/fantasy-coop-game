package com.mygdx.game.shared.util.network.messages.lobby;

/**
 * Used to tell other players another player's username choice
 * Created by elimonent on 9/8/2016.
 */
public class UsernameChoiceMsg {
    public int uid;
    public String username;

    /**
     *
     * @param uid the identifier for the person who changed their username
     * @param username
     */
    public UsernameChoiceMsg(int uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    /**
     * required by kryo
     */
    public UsernameChoiceMsg() {

    }
}
