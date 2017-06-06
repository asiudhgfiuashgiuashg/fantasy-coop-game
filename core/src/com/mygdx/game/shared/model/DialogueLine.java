package com.mygdx.game.shared.model;

/**
 * Represents a dialogue line said by a dynamic entity displayed for some amount of time.
 * Dynamic entities on the server should create instances of this class and send them to the clients to be displayed.
 */
public class DialogueLine {
	public long displayTime; // how long to display the line in milliseconds
	public String msg; // what string to display

	public DialogueLine(String msg, long displayTime) {
		this.msg = msg;
		this.displayTime = displayTime;
	}

	/**
	 * required for kryo serialization
	 */
	private DialogueLine() {

	}

}
