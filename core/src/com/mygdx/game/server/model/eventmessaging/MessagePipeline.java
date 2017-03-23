package com.mygdx.game.server.model.eventmessaging;


import java.util.ArrayList;
import java.util.List;

/**
 * A place that listeners can subscribe to certain events.
 * Things which generate these events should send them to the appropriate pipeline
 *
 * Example: a quest is completed when 10 dragons are killed. The quest should listen on the appropriate
 * pipeline for death events. When a dragon AI dies it should send a death event to the death pipeline.
 * @param <T>
 */
public class MessagePipeline<T extends EventMessage> {
	private List<MessageListener<T>> listeners = new ArrayList<MessageListener<T>>();

	public void registerListener(MessageListener<T> listener) {
		listeners.add(listener);
	}

	public void deregisterListener(MessageListener<T> listener) {
		listeners.remove(listener);
	}

	/**
	 * send the message to all listeners on this pipeline (everyone who is subscribed to this type of message)
	 *
	 * @param message
	 */
	public void putMessage(T message) {
		for (MessageListener<T> listener: listeners) {
			listener.notify(message);
		}
	}
}
