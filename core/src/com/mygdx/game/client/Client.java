package com.mygdx.game.client;


import com.badlogic.gdx.Game;

import com.mygdx.game.client.view.MenuScreen;

/**
 * The main game loop for the client application.
 * Delegates rendering to one of three screens.
 * @author elimonent
 *
 */
public class Client extends Game {

	
	@Override
	public void create () {
		setScreen(new MenuScreen());
	}


}
