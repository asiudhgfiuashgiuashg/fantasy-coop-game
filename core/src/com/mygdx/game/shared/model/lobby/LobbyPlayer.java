package com.mygdx.game.shared.model.lobby;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * represents a player in a lobby.
 * The server and client both extend this to fit their needs
 * Created by elimonent on 9/6/2016.
 */
public abstract class LobbyPlayer {
    protected int uid; //uniquely identifies this client to other clients
    private String username;
    private PlayerClass playerClass;

    private final AtomicBoolean ready = new AtomicBoolean(false); // Whether or not this player is ready to start.
                                                                // This is Atomic because the Server or GameClient thread will access it
                                                                //  and also the listeners on the Kryo Server or KryoClient thread, so 2 different threads

    public int getUid() {
        return uid;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}


	public boolean isReady() {
		return ready.get();
	}
	
	public void setReady(boolean ready) {
		this.ready.set(ready);
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

    /**
     * for printing to console
     * @return
     */
    @Override
    public String toString() {
        return username + " / uid: " + uid + " / class: " + playerClass + " / ready status: " + ready;
    }
}
