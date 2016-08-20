package com.mygdx.game.client;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.client.view.MenuScreen;
import com.strongjoshua.console.GUIConsole;

/**
 * The main game loop for the client application.
 * Delegates rendering to one of three screens.
 * @author elimonent
 *
 */
public class Client extends Game {
	private GUIConsole console;
	
	@Override
	public void create () {
		console = new GUIConsole();
		setScreen(new MenuScreen());
	}

	@Override
	public void render () {
		super.render();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		console.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		console.refresh();
	}

}
