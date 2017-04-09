package com.mygdx.game.server.model.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.DynamicEntity;
import com.mygdx.game.shared.network.GameMessage;

/**
 * Basic stuff that every player should have.
 */
public class Player extends DynamicEntity {

	public int connectionUid;
	private static final GameServer server = GameServer.getInstance();


	protected Player(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	/**
	 * Override draw so that players don't get their own position update messages when draw() is called from entity.setPosition() (on the server)
	 */
	@Override
	public void draw() {
		GameMessage.PosUpdateMessage msg = new GameMessage.PosUpdateMessage();
		msg.entityUID = uid;
		msg.position = getPosition();
		msg.visLayer = visLayer;
		server.sendToAllExcept(msg, connectionUid);
	}
}
