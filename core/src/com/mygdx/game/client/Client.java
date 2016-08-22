package com.mygdx.game.client;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.client.view.MenuScreen;
import com.mygdx.game.util.ConcreteCommandExecutor;
import com.mygdx.game.util.SingletonGUIConsole;

/**
 * The main game loop for the client application.
 * Delegates rendering to one of three screens.
 * @author elimonent
 *
 */
public class Client extends Game {
	private SingletonGUIConsole console;
	
	@Override
	public void create () {
		setupConsole();
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

	private void setupConsole() {
		console = SingletonGUIConsole.getInstance();
		console.setCommandExecutor(new ConcreteCommandExecutor());
		console.setPositionPercent(0, 55);
		console.setSizePercent(100, 45);
	}

}
