package com.mygdx.game.server.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Contains information that the client needs to update its local representation of a Drawable.
 * A drawMessage is JSONized and sent to a client.
 * @author elimonent
 *
 */
public class DrawMessage {
	/**
	 * Uniquely identifies the Drawable that this message is updating.
	 * Uids are synchronized between client and server.
	 */
	protected String uid;
	/**
	 * position in global coordinates
	 */
	protected Vector2 position;
	protected String spriteName;
	/**
	 * The layer that the Drawable is drawn in.
	 */
	protected int visLayer;
}
