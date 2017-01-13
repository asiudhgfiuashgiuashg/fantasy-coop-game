package com.mygdx.game.shared.controller;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.mygdx.game.shared.network.Message;

public abstract class Communicator {
	protected ConcurrentLinkedQueue<Message> incomingBuffer = new ConcurrentLinkedQueue<Message>();
	protected ConcurrentLinkedQueue<Message> outgoingBuffer = new ConcurrentLinkedQueue<Message>();
	
	public void queueMessage(Message msg) {
		outgoingBuffer.add(msg);
	}
	
	public abstract void sendMessages();
	public abstract void readMessages();
}
