package com.mygdx.game.shared.network;

/**
 * Network message base class.
 * 
 * @author Sawyer Harris
 *
 */
public abstract class Message {
	/** UID of client who sent the message */
	public int uid;
	/**
	 * UID of client who should receive message. Used by server only. Default value is
	 * sent to all clients
	 */
	public int recipient;
	/** If true, server will send message to all clients EXCEPT recipient */
	public boolean except;
}