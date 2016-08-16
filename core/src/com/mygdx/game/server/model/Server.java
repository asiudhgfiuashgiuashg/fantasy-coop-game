package com.mygdx.game.server.model;

import com.mygdx.game.server.controller.ServerCommunicator;
import com.mygdx.game.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author elimonent
 *
 * A singleton which keeps track of the server's state
 *  and simulates the world (ticks)
 *  
 * A server instance is created by a client using a button in the client's menu.
 * The player who creates a server instance with their client is the Host.
 */
public class Server implements Runnable {

	private final List<Player> players = new ArrayList<Player>();
	private GameMap map; // The current game map.
	private final ServerCommunicator communicator = new ServerCommunicator();
	private final float TICKRATE = .05f; //How often the server updates the game world.
	private GameState state;
	private Cutscene currCutscene;


	/**
	 * The server loop
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	

	public void setState(GameState state) {
		this.state = state;
	}

}
