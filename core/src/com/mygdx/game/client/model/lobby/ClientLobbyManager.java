package com.mygdx.game.client.model.lobby;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.shared.model.LobbyManager;
import com.mygdx.game.shared.util.network.messages.lobby.ChatMessageMsg;
import com.mygdx.game.shared.util.network.messages.lobby.ChooseUsernameMsg;
import com.mygdx.game.shared.util.network.messages.lobby.ReadyStatusMsg;

import java.util.ArrayList;

/**
 * Maintains lobby state information on the client side.
 * Updates the view based on this state info.
 * For example, when a new client is added to the lobby, this class should
 *  update the view to reflect that.
 * Created by elimonent on 9/6/2016.
 */
public class ClientLobbyManager extends LobbyManager<ClientLobbyPlayer> {

    private ClientLobbyPlayer localLobbyPlayer;
    private Client kryoClient;

    /**
     * called after establishing connection between kryo client and server
     * @param username
     * @param kryoClient
     */
    public ClientLobbyManager(String username, Client kryoClient) {
        this.kryoClient = kryoClient;
        lobbyPlayers = new ArrayList<ClientLobbyPlayer>();
        localLobbyPlayer = new ClientLobbyPlayer(ClientLobbyPlayer.LOCAL_PLAYER_UID, username);
        lobbyPlayers.add(localLobbyPlayer);
    }

    /**
     * Add the player to the client lobby if their uid isn't already there.
     * If their uid is already their, update their info.
     * @param player the player to add/update
     */
    @Override
    public void addLobbyPlayer(ClientLobbyPlayer player) {
        super.addLobbyPlayer(player);
        //update client lobby view here
    }

    public ClientLobbyPlayer getByUid(int uid) {
        for (ClientLobbyPlayer lobbyPlayer: getLobbyPlayers()) {
            if (lobbyPlayer.getUid() == uid) {
                return lobbyPlayer;
            }
        }
        return null;
    }

    /**
     * send the local client's chosen username to the server after connecting
     */
    public void sendUsername() {
        kryoClient.sendTCP(new ChooseUsernameMsg(localLobbyPlayer.username));
    }

    public ClientLobbyPlayer getLocalLobbyPlayer() {
        return localLobbyPlayer;
    }

    /**
     * Set local player's status to ready.
     * Send Network message to server that player is ready.
     * Update portion of view which reflects readyness
     */
    public void setAndSendReady(boolean ready) {
        localLobbyPlayer.ready.set(ready);
        kryoClient.sendTCP(new ReadyStatusMsg(ready));
        //TODO update view
    }

    /**
     * save message and also update chat box (view)
     * @param msg
     */
    @Override
    public void addChatMessage(ChatMessageMsg msg) {
        super.addChatMessage(msg);
        //TODO display message on screen
    }
}
