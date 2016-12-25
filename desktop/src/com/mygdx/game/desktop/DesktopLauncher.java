package com.mygdx.game.desktop;

import testing.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.client.model.GameClient;

public class DesktopLauncher {
	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = SCREEN_HEIGHT;
		config.width = SCREEN_WIDTH;
		new LwjglApplication(new GameClient(), config);
	}
}
