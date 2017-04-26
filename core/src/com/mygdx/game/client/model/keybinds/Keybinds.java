package com.mygdx.game.client.model.keybinds;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.shared.model.PreferencesConstants;

/**
 * Maintains a map of key codes to user input actions (move, cast spell, attacking
 * etc.)
 * 
 * @author Sawyer Harris
 *
 */
public class Keybinds {
	private static final int DEFAULT_UP = Keys.W;
	private static final int DEFAULT_LEFT = Keys.A;
	private static final int DEFAULT_DOWN = Keys.S;
	private static final int DEFAULT_RIGHT = Keys.D;
	private static final int DEFAULT_ATTACK = Keys.SPACE;

	private HashMap<Integer, Input> map;
	private Preferences prefs;

	/**
	 * Constructs the map of keybindings from libgdx preferences
	 */
	public Keybinds() {
		map = new HashMap<Integer, Input>();
		prefs = Gdx.app.getPreferences(PreferencesConstants.KEYBINDINGS_PREFS);

		// Initialize any unbound keys to default
		initializeBinds();

		Map<String, ?> m = prefs.get();
		for (Map.Entry<String, ?> entry : m.entrySet()) {
			Input input = Input.valueOf(entry.getKey());
			Integer keycode = Integer.parseInt((String) entry.getValue());
			map.put(keycode, input);
		}
	}

	/**
	 * Gets the user input action from the key code
	 * 
	 * @param keycode
	 *            integer code for keyboard key (see libgdx Input.Keys)
	 * @return the input requested by user
	 */
	public Input get(int keycode) {
		return map.get(keycode);
	}

	/**
	 * Binds a key. Call flush() to save changes
	 * 
	 * @param input
	 * @param keycode
	 */
	public void set(Input input, int keycode) {
		prefs.putInteger(input.toString(), keycode);
	}

	/**
	 * Saves changes to preferences
	 */
	public void flush() {
		prefs.flush();
	}

	/**
	 * Sets ALL keybinds to default values
	 */
	public void reset() {
		set(Input.UP, DEFAULT_UP);
		set(Input.LEFT, DEFAULT_LEFT);
		set(Input.DOWN, DEFAULT_DOWN);
		set(Input.RIGHT, DEFAULT_RIGHT);
		set(Input.ATTACK, DEFAULT_ATTACK);
		flush();
	}

	/**
	 * Sets any unbound keys to default values
	 */
	private void initializeBinds() {
		if (prefs.getInteger(Input.UP.toString()) == 0)
			set(Input.UP, DEFAULT_UP);
		if (prefs.getInteger(Input.LEFT.toString()) == 0)
			set(Input.LEFT, DEFAULT_LEFT);
		if (prefs.getInteger(Input.DOWN.toString()) == 0)
			set(Input.DOWN, DEFAULT_DOWN);
		if (prefs.getInteger(Input.RIGHT.toString()) == 0)
			set(Input.RIGHT, DEFAULT_RIGHT);
		if (prefs.getInteger(Input.ATTACK.toString()) == 0) {
			set(Input.ATTACK, DEFAULT_ATTACK);
		}
		flush();
	}

	/**
	 * Enum of user input buttons that keyboard keys will map to.
	 * 
	 * @author Sawyer Harris
	 *
	 */
	public enum Input {
		UP, LEFT, DOWN, RIGHT, ATTACK, SPELL1, SPELL2, SPELL3, SPELL4, POTION
	}
}
