package com.mygdx.game.shared.model;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * represents a player in a lobby.
 * The server and client both extend this to fit their needs
 * Created by elimonent on 9/6/2016.
 */
public abstract class LobbyPlayer {
    protected int uid; //uniquely identifies this client to other clients
    public String username;
    public PlayerClassEnum playerClass;

    public final AtomicBoolean ready = new AtomicBoolean(false); // Whether or not this player is ready to start.
                                                                // This is Atomic because the Server or GameClient thread will access it
                                                                //  and also the listeners on the Kryo Server or KryoClient thread, so 2 different threads

    public int getUid() {
        return uid;
    }

    /**
     *
     * @param other
     * @return whether uids are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LobbyPlayer)) {
            return false;
        }
        LobbyPlayer theOther = (LobbyPlayer) other;
        return theOther.getUid() == this.getUid();
    }

    @Override
    public int hashCode() {
        return 37 * getUid();
    }
}
