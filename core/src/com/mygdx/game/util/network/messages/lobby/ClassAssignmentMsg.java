package com.mygdx.game.util.network.messages.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Sent to a client to let them know that a client has been assigned a class.
 * Upon receiving this message, a client should update their internal lobby model and lobby view.
 * Created by elimonent on 8/27/2016.
 */
public class ClassAssignmentMsg {
    private PlayerClassEnum playerClass;
    public ClassAssignmentMsg(PlayerClassEnum theClass) {
        this.playerClass = theClass;
    }

    /**
     * required by serialization library
     */
    public ClassAssignmentMsg() {

    }
}
