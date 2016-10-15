package com.mygdx.game.shared.util.network.messages.lobby;

/**
 * Client -> Server whether client is ready; uid is -1
 * Server -> Client whether a client is ready; uid is uid of client whose ready status is changing
 * Created by elimonent on 9/9/2016.
 */
public class ReadyStatusMsg {
    public boolean ready;
    public int uid;

    /**
     * use this when sending client -> server
     * @param ready
     */
    public ReadyStatusMsg(boolean ready) {
        this.ready = ready;
        uid = -1;
    }

    /**
     * use this when sending from server -> client
     * @param ready whether the client is rady
     * @param uid identifier of client whose ready status is changing
     */
    public ReadyStatusMsg(boolean ready, int uid) {
        this.uid = uid;
        this.ready = ready;
    }

    /**
     * required by kryo
     */
    public ReadyStatusMsg() {

    }
}
