package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.Actable;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.shared.model.EntityLight;
import com.mygdx.game.shared.network.GameMessage;

import java.util.ArrayList;
import java.util.List;

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

	public List<EntityLight> lights;


	protected DynamicEntity(String uid, Vector2 position, int visLayer,
							boolean solid) {
		super(uid, position, visLayer, solid);
		this.lights = new ArrayList<EntityLight>();
		// Draw on creation
		draw();
	}

	/**
	 * Sets the entity's position along with its hitbox's position
	 *
	 * @param position
	 */
	@Override
	public void setPosition(Vector2 position) {
		if (!getPosition().epsilonEquals(position, .00000001f)) {
			super.setPosition(position);
			if (getUid() != null) {
				draw();
			}
		}
	}

	/**
	 * Creates a PosUpdateMessage to tell clients to update their view of this
	 * entity
	 */
	public void draw() {
		GameMessage.PosUpdateMessage posMsg = new GameMessage
				.PosUpdateMessage();
		posMsg.entityUID = uid;
		posMsg.position = getPosition();
		posMsg.visLayer = visLayer;
		posMsg.velocity = getVelocity();
		server.sendToAll(posMsg);
	}

	/**
	 * Call this if you want to change animations or restart the current one
	 * (on the client side)
	 */
	public void sendAnimation(float frameDuration) {
		GameMessage.AnimationUpdateMessage msg = new GameMessage.AnimationUpdateMessage();
		msg.entityUID = getUid();
		msg.animationName = animationName;
		server.sendToAll(msg);
	}

	/**
	 * send polygon updates to client and update the polygon field
	 * @param polygon
	 */
	@Override
	public void setVertices(float[] vertices) {
		super.setVertices(vertices);

		GameMessage.HitboxUpdateMessage hitboxMsg = new GameMessage
				.HitboxUpdateMessage();
		hitboxMsg.entityUID = getUid();
		hitboxMsg.vertices = vertices;


		server.sendToAll(hitboxMsg);
	}

	@Override
	public void act(long elapsedTime) {
		// dt needs to be in seconds (just like on client)
		float dt = elapsedTime / 1000f;
		doPhysics(dt, GameServer.getInstance().getMap().getSolidObjects());
	}
}
