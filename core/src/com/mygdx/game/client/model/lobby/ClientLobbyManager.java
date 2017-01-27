package com.mygdx.game.client.model.lobby;

import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.shared.model.lobby.LobbyManager;
import com.mygdx.game.shared.network.LobbyMessage.ChatMessage;
import com.mygdx.game.shared.network.LobbyMessage.ReadyStatusMessage;

/**
 * Maintains lobby state information on the client side.
 * Updates the view based on this state info.
 * For example, when a new client is added to the lobby, this class should
 *  update the view to reflect that.
 * Created by elimonent on 9/6/2016.
 */
public class ClientLobbyManager extends LobbyManager<ClientLobbyPlayer> {
    private static final GameClient client = GameClient.getInstance();
	
	private ClientLobbyPlayer localLobbyPlayer;


    /**
     * Add the player to the client lobby if their uid isn't already there.
     * If their uid is already there, update their info.
     * @param player the player to add/update
     */
    @Override
    public void addLobbyPlayer(ClientLobbyPlayer player) {
    	super.addLobbyPlayer(player);
    	//update client lobby view here
    	
    }


    public void setLocalLobbyPlayer(ClientLobbyPlayer player) {
    	localLobbyPlayer = player;
    }
    
    public ClientLobbyPlayer getLocalLobbyPlayer() {
        return localLobbyPlayer;
    }

    /**
     * Set local player's status to ready.
     * Send Network message to server that player is ready.
     * Update portion of view which reflects readyness
     */
    public void setReady(boolean ready) {
        localLobbyPlayer.setReady(ready);
        ReadyStatusMessage msg = new ReadyStatusMessage();
        msg.ready = ready;
        client.sendToServer(msg);
        //TODO update view
    }

    /**
     * save message and also update chat box (view)
     * @param msg
     */
    @Override
    public void addChatMessage(ChatMessage msg) {
        super.addChatMessage(msg);
        System.out.println("UID: " + msg.uid + "User: " + getByUid(msg.uid));
        client.sendToServer(msg);
        //TODO display message on screen
    }
}
