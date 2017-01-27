package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Actable;
import com.mygdx.game.shared.network.GameMessage;

/**
 * @author elimonent
 *         <p>
 *         An entity which is updated every tick with act()
 */
public abstract class DynamicEntity extends Entity implements Actable {

	/**
	 * Name of entity's current animation
	 */
	protected String animationName;


	protected DynamicEntity(String uid, Vector2 position, int visLayer,
							boolean solid) {
		super(uid, position, visLayer, solid);
	}

	/**
	 * Creates a PosUpdateMessage to tell clients to update their view of this
	 * entity
	 */
	@Override
	public void draw() {
		GameMessage.PosUpdateMessage posMsg = new GameMessage
				.PosUpdateMessage();
		posMsg.entityUID = uid;
		posMsg.position = position;
		posMsg.visLayer = visLayer;
		server.getCommunicator().sendToAll(posMsg);
	}

	/**
	 * Call this if you want to change animations or restart the current one
	 * (on the client side)
	 */
	public void sendAnimation() {
		GameMessage.AnimationUpateMessage msg = new GameMessage
				.AnimationUpateMessage();
		msg.entityUID = getUid();
		msg.animationName = animationName;
		server.getCommunicator().sendToAll(msg);
	}
}
