package com.mygdx.game.client.controller.networklisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.util.SingletonGUIConsole;
import com.mygdx.game.util.network.messages.lobby.ClassAssignmentMsg;
import com.mygdx.game.util.network.messages.lobby.OtherClassAssignmentMsg;
import com.mygdx.game.util.network.messages.lobby.LobbyPlayerInfoMsg;

/**
 * General listener that the client uses to demultiplex network messages from the server
 * Add a receive function for each message type you want to deal with.
 * Created by elimonent on 8/27/2016.
 */
public class LobbyListener extends Listener.ReflectionListener {
    private SingletonGUIConsole console = SingletonGUIConsole.getInstance();
    public void received(Connection connection, OtherClassAssignmentMsg msg) {
        console.log("Got a class assignment message for some lobby player besides myself: "
                + msg.getPlayerClass() + "/" + "uid:" + msg.getUid());
    }

    public void received(Connection connection, LobbyPlayerInfoMsg infoMsg) {
        console.log("New player joined lobby with uid " + infoMsg.uid);
    }

    public void received(Connection connection, ClassAssignmentMsg msg) {
        console.log("Got a class assignment for myself: " + msg.getPlayerClass());

    }
}
