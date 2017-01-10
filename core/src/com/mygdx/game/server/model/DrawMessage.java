package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Contains information that the client needs to update its local representation
 * of a Drawable. A drawMessage is JSONized and sent to a client.
 * 
 * @author elimonent
 * @author Sawyer Harris
 *
 */
public class DrawMessage {
	/**
	 * Constructs a DrawMessage to deliver draw info about an entity to the
	 * clients.
	 * 
	 * @param uid
	 * @param position
	 * @param spriteName
	 * @param visLayer
	 */
	public DrawMessage(String uid, Vector2 position, String spriteName, int visLayer) {
		this.uid = uid;
	}

	/**
	 * Uniquely identifies the Drawable that this message is updating. Uids are
	 * synchronized between client and server.
	 */
	public String uid;

	/** Draw data for entity */
	public Vector2 position;
	public String spriteName;
	public int visLayer;
}
