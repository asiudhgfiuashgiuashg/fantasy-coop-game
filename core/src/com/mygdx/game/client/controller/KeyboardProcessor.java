package com.mygdx.game.client.controller;

import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.model.keybinds.Keybinds;
import com.mygdx.game.client.model.keybinds.Keybinds.Input;
import com.mygdx.game.client.model.entity.player.Player;

public class KeyboardProcessor extends InputAdapter {	
	private Keybinds binds;
	private Player localPlayer;
	
	public KeyboardProcessor(Keybinds binds) {
		this.binds = binds;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		Keybinds.Input input = binds.get(keycode);
		localPlayer = GameClient.getInstance().getMap().localPlayer;

		if (localPlayer != null) {
			if (input == Input.UP) {
				localPlayer.up = true;
			} else if (input == Input.DOWN) {
				localPlayer.down = true;
			} else if (input == Input.LEFT) {
				localPlayer.left = true;
			} else if (input == Input.RIGHT) {
				localPlayer.right = true;
			} else if (input == Input.ATTACK) {
				localPlayer.setAttack(true);
			}
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
		localPlayer = GameClient.getInstance().getMap().localPlayer;

		if (localPlayer != null) {
			Keybinds.Input input = binds.get(keycode);
			if (input == Input.UP) {
				localPlayer.up = false;
			} else if (input == Input.DOWN) {
				localPlayer.down = false;
			} else if (input == Input.LEFT) {
				localPlayer.left = false;
			} else if (input == Input.RIGHT) {
				localPlayer.right = false;
			} else if (input == Input.ATTACK) {
				localPlayer.setAttack(false);
			}
		}
		
		return false;
	}
}
