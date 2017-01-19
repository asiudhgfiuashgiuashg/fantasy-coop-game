package com.mygdx.game.shared.controller;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.mygdx.game.shared.network.Message;

/**
 * used by server or client to send and receive network messages using kryo
 */
public abstract class Communicator {
	protected ConcurrentLinkedQueue<Message> incomingBuffer = new ConcurrentLinkedQueue<Message>();

	public abstract void readMessages();
}
