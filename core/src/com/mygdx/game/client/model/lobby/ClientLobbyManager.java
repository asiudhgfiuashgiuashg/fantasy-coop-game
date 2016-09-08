package com.mygdx.game.client.model.lobby;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.shared.model.LobbyManager;
import com.mygdx.game.shared.util.network.messages.lobby.ChooseUsernameMessage;

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
 /*       if (getLobbyPlayers().contains(player)) { //update
            ClientLobbyPlayer lobbyPlayer = getLobbyPlayers().get(getLobbyPlayers().indexOf(player));
            lobbyPlayer.username = player.username;
            lobbyPlayer.playerClass = player.playerClass;
        } else { //add
            getLobbyPlayers().add(player);
        }*/

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
        kryoClient.sendTCP(new ChooseUsernameMessage(localLobbyPlayer.username));
    }
}
