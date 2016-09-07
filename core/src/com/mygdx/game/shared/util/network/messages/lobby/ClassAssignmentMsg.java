package com.mygdx.game.shared.util.network.messages.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Sent to a client to let them know what their class is.
 * Created by elimonent on 9/6/2016.
 */
public class ClassAssignmentMsg {
    protected PlayerClassEnum playerClass;

    public ClassAssignmentMsg(PlayerClassEnum playerClass) {
        this.playerClass = playerClass;
    }

    /**
     * required by kryo
     */
    public ClassAssignmentMsg() {

    }

    public PlayerClassEnum getPlayerClass() {
        return playerClass;
    }
}
