package com.mygdx.game.client.controller.networklisteners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.client.model.lobby.ClientLobbyManager;
import com.mygdx.game.client.model.lobby.ClientLobbyPlayer;
import com.mygdx.game.shared.util.SingletonGUIConsole;
import com.mygdx.game.shared.util.network.messages.lobby.*;

/**
 * General listener that the client uses to demultiplex network messages from the server
 * Add a receive function for each message type you want to deal with.
 * Created by elimonent on 8/27/2016.
 */
public class LobbyListener extends Listener.ReflectionListener {
    private final ClientLobbyManager lobbyManager;
    private SingletonGUIConsole console = SingletonGUIConsole.getInstance();

    public LobbyListener(ClientLobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager; //will be used to allow the controller (this listener) to modify the model (the lobbyManager)
    }

    /**
     * receive info about another player's class assignment
     */
    public void received(Connection connection, OtherClassAssignmentMsg msg) {
        console.log("Got a class assignment message for some lobby player besides myself: "
                + msg.getPlayerClass() + "/" + "uid:" + msg.getUid());
        lobbyManager.getByUid(msg.getUid()).playerClass = msg.getPlayerClass();
    }

    /**
     * receive info about a new player or player who was there before you.
     * @param connection
     * @param infoMsg
     */
    public void received(Connection connection, LobbyPlayerInfoMsg infoMsg) {
        console.log("Added player to lobby with uid " + infoMsg.uid);
        ClientLobbyPlayer lobbyPlayer = new ClientLobbyPlayer(infoMsg.uid, infoMsg.username, infoMsg.playerClass);
        lobbyManager.addLobbyPlayer(lobbyPlayer);
    }

    /**
     * get info about your own class assignment, usually in response to choosing a class in the gui
     * @param connection
     * @param msg
     */
    public void received(Connection connection, ClassAssignmentMsg msg) {
        console.log("Got a class assignment for myself: " + msg.getPlayerClass());
        lobbyManager.getLocalLobbyPlayer().playerClass = msg.getPlayerClass();
    }

    public void received(Connection connection, UsernameChoiceMsg usernameChoiceMsg) {
        ClientLobbyPlayer lobbyPlayer = lobbyManager.getByUid(usernameChoiceMsg.uid);
        lobbyPlayer.username = usernameChoiceMsg.username;
    }

    public void received(Connection connection, ReadyStatusMsg readyMsg) {
        ClientLobbyPlayer lobbyPlayer = lobbyManager.getByUid(readyMsg.uid); //find out who the uid refers to
        lobbyPlayer.ready.set(readyMsg.ready);
    }
}
