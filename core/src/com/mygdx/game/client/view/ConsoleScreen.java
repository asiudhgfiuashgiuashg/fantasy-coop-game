package com.mygdx.game.client.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.strongjoshua.console.GUIConsole;

/**
 * A Screen that has a developer console enabled.
 * Open and close this console by pressing '~'
 * You must include calls to super.show() and super.render() in subclasses of this class.
 *
 * Created by elimonent on 8/19/16.
 */
public class ConsoleScreen extends ScreenAdapter {
	private GUIConsole console;

	@Override
	public void show() {
		console = new GUIConsole();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		console.draw();
	}
}
