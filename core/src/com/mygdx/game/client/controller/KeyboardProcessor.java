package com.mygdx.game.client.controller;

import com.badlogic.gdx.InputAdapter;
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
		// We can use this to tell when the player wants to stop running in one direction
		return false;
	}
}
