package com.mygdx.game.client.controller;

import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.keybinds.Keybinds;
import com.mygdx.game.client.model.keybinds.Keybinds.Input;

public class KeyboardProcessor extends InputAdapter {	
	private Keybinds binds;
	
	public KeyboardProcessor(Keybinds binds) {
		this.binds = binds;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		Keybinds.Input input = binds.get(keycode);
		System.out.println(input);

		if (input == Input.UP) {
			GameClient.getInstance().getMap().localPlayer.moveUp();
		} else if (input == Input.DOWN) {
			GameClient.getInstance().getMap().localPlayer.moveDown();
		} else if (input == Input.LEFT) {
			GameClient.getInstance().getMap().localPlayer.moveLeft();
		} else if (input == Input.RIGHT) {
			GameClient.getInstance().getMap().localPlayer.moveRight();
		}

		/* 
		 * EXAMPLE of what happens next
		 * 
		 * if (input == Input.ATTACK) {
		 * 		mainCharacter.tryToAttack()
		 * } else if (input == ...) {
		 * 		... and so on
		 * }
		*/
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		Keybinds.Input input = binds.get(keycode);
		if (input == Input.UP) {
			GameClient.getInstance().getMap().localPlayer.moveUp();
		} else if (input == Input.DOWN) {
			GameClient.getInstance().getMap().localPlayer.moveDown();
		} else if (input == Input.LEFT) {
			GameClient.getInstance().getMap().localPlayer.moveLeft();
		} else if (input == Input.RIGHT) {
			GameClient.getInstance().getMap().localPlayer.moveRight();
		}

		return false;
	}
}
