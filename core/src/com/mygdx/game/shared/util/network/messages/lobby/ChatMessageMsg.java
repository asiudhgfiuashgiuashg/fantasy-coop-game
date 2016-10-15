package com.mygdx.game.shared.util.network.messages.lobby;

/**
 * Client -> Server or Server -> Client chat message
 * The flow is like so:
 * User types message -> client constructs ChatMessageMsg -> Server receives msg and fills in uid -> Server distributes message to other clients
 * Created by elimonent on 9/9/16.
 */
public class ChatMessageMsg {
	public int uid; //server should populate this so the clients know who the msg is from
	public String message;


	/**
	 * server should use this constructor
	 * @param message the chat message
	 * @param uid the uid of the client who sent this message
	 */
	public ChatMessageMsg(String message, int uid) {
		this.message = message;
		this.uid = uid;
	}

	/**
	 * clients should use this constructor since it doesn't know its uid
	 * @param message the chat message
	 */
	public ChatMessageMsg(String message) {
		this(message, -1);
	}

	/**
	 * required by Kryo
	 */
	public ChatMessageMsg () {

	}
}
