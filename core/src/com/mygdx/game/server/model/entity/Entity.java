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

	public Entity(float[] vertices, String uid, Vector2 position, int visLayer, boolean solid) {
		super(vertices, solid, position);
		this.uid = uid;
		this.visLayer = visLayer;

	}

	public Entity(String uid, Vector2 position, int visLayer, boolean solid) {
		this(null, uid, position, visLayer, solid);
	}

	/**
	 * Unique identifier synchronized between client and server
	 */
	protected String uid;


	/**
	 * Visibility layer for rendering
	 */
	public int visLayer;





	/**
	 * Gets entity's current visibility layer
	 *
	 * @return visLayer
	 */
	public int getVisLayer() {
		return visLayer;
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
		return "Entity [uid=" + uid + ", position=" + getPosition() + ", " + "visLayer=" + visLayer + "]";
	}

	/**
	 * equal if uids are equal
	 *
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof Entity)) {
			return false;
		}
		Entity theOther = (Entity) other;
		return this.getUid().equalsIgnoreCase(theOther.getUid());
	}

	@Override
	public int hashCode() {
		return 37 * getUid().hashCode();
	}
}
