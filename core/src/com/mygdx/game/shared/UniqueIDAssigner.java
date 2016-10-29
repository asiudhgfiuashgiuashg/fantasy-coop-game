package com.mygdx.game.shared;

import com.mygdx.game.server.model.entity.Entity;

/**
 * Generates unique id's for dynamic entities and static entities. Only the
 * server generates uid's for dynamic entities. Static entity uid's are
 * generated based on their object info from Tiled.
 * 
 * @author Sawyer Harris
 *
 */
public class UniqueIDAssigner {
	/** UID generation constants */
	private static final String DELIMITER = ".";
	private static final String DYNAMIC = "dynamic";
	private static final String STATIC = "static";

	/** integer counter for dynamic entities */
	private static int counter = 0;

	// No instances need be created
	private UniqueIDAssigner() {
	}

	/**
	 * Server use only! Generates a UID for a dynamic entity.
	 * 
	 * @param e
	 *            entity
	 * @return uid
	 */
	public static String generateDynamicEntityUID(String name) {
		String uid = String.join(DELIMITER, DYNAMIC, name, Integer.toString(counter));

		counter++;
		return uid;
	}

	/**
	 * Usable by server and client. Generates a UID for a static entity based on
	 * object info in Tiled.
	 *
	 * @param entityName
	 *            name of static entity
	 * @param mapName
	 *            name of game map
	 * @param objectId
	 *            Tiled object id of entity
	 * @return uid
	 */
	public static String generateStaticEntityUID(String entityName, String mapName, int objectId) {
		String uid = String.join(DELIMITER, STATIC, entityName, mapName, Integer.toString(objectId));

		// Don't increment counter for static entities! UID format is different
		return uid;
	}
}
