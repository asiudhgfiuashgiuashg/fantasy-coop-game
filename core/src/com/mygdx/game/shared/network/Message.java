package com.mygdx.game.shared.network;

import com.badlogic.gdx.math.Vector2;

/**
 * Network message base class.
 * 
 * @author Sawyer Harris
 *
 */
public abstract class Message {
	/** UID of client who sent the message or the client that the message is
	 * about
	 * ex) if this is a class selection message, uid should be the client
	 * whose class is changing
	 *
	 */
	public int uid;
}
