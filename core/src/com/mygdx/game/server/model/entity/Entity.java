package com.mygdx.game.server.model.entity;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.PolygonObject;
import com.mygdx.game.shared.network.GameMessage;

/**
 * A PolygonObject which has a sprite to be drawn on the client.
 *
 * @author elimonent
 * @author Sawyer Harris
 */
public abstract class Entity extends PolygonObject {

	static final GameServer server = GameServer.getInstance();

	public Entity(String uid, Vector2 position, int visLayer, boolean solid) {
		super(null, solid);
		this.uid = uid;
		this.position = position;
		this.visLayer = visLayer;

		// Draw on creation
		draw();
	}

	/**
	 * Unique identifier synchronized between client and server
	 */
	protected String uid;
	/**
	 * Position in global coordinates
	 */
	protected Vector2 position;

	/**
	 * Visibility layer for rendering
	 */
	public int visLayer;

	/**
	 * Creates a PosUpdateMessage to tell clients to update their view of this
	 * entity
	 */
	public void draw() {
		GameMessage.PosUpdateMessage msg = new GameMessage.PosUpdateMessage();
		msg.entityUID = uid;
		msg.position = position;
		msg.visLayer = visLayer;
		server.getCommunicator().sendToAll(msg);
	}

	/**
	 * Returns a copy of the entity's position. All modifications must be done
	 * via setPosition()
	 *
	 * @return copy of position
	 */
	public Vector2 getPosition() {
		return new Vector2(position);
	}

	/**
	 * Sets the entity's position along with its hitbox's position
	 *
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		if (!getPosition().epsilonEquals(position, .00000001f)) {
			this.position = position;

			// Bind hitbox position to entity position
			Polygon polygon = getPolygon();
			if (polygon != null) {
				getPolygon().setPosition(position.x, position.y);
			}

			draw();
		}
	}


	/**
	 * Gets entity's current visibility layer
	 *
	 * @return visLayer
	 */
	public int getVisLayer() {
		return visLayer;
	}

	/**
	 * Sets entity's visibility layer
	 *
	 * @param visLayer -1, 0, or 1
	 */
	public void setVisLayer(int visLayer) {
		this.visLayer = visLayer;

		// Redraw on visLayer change
		draw();
	}

	/**
	 * Gets entity's uid
	 *
	 * @return uid
	 */
	public String getUid() {
		return uid;
	}

	@Override
	public String toString() {
		return "Entity [uid=" + uid + ", position=" + position + ", visLayer="
				+ visLayer + "]";
	}
}
