package com.mygdx.game.util.network.messages.lobby;

import com.mygdx.game.server.model.lobby.PlayerClassEnum;

/**
 * Sent to a client to let them know that another client has been assigned a class.
 * Upon receiving this message, a client should update their internal lobby model and lobby view.
 * Created by elimonent on 8/27/2016.
 */
public class OtherClassAssignmentMsg extends ClassAssignmentMsg {
    private int uid;
    public OtherClassAssignmentMsg(PlayerClassEnum theClass, int uid) {
        super(theClass);
        this.uid = uid;
    }

    /**
     * required by serialization library
     */
    public OtherClassAssignmentMsg() {

    }

    public int getUid() {
        return uid;
    }
}
