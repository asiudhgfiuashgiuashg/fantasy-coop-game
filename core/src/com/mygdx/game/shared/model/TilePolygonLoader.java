package com.mygdx.game.shared.model;

import com.badlogic.gdx.utils.XmlReader;

/**
 * Polygon loading methods usable by both client and server.
 *
 * @author elimonent
 * @author Sawyer Harris
 */
public class TilePolygonLoader {
	/**
	 * load the hitbox with the name "hitbox" from tile xml
	 *
	 * @param tileElement
	 * @return
	 */
	public static float[] loadTilePolygon(XmlReader.Element tileElement) {
		int tileHeight = tileElement.getChildByName("image").getInt("height");
		// Load polygon hitbox
		XmlReader.Element objGroup = tileElement.getChildByName("objectgroup");
		if (objGroup != null) {
			for (XmlReader.Element object : objGroup.getChildrenByName("object")) {
				String objName = object.getAttribute("name", null);
				if (objName != null && objName.equals("hitbox")) {
					return loadPolygonVertices(object, tileHeight);
				}

			}
		}
		return null;
	}

	/**
	 * loads vertices from a tmx polygon
	 * @param object - xml element with the polygon
	 * @param tileHeight - height of the tile which affects the vertex values
	 * @return list of vertices {x0, y0, x1, y1, ...}
	 */
	public static float[] loadPolygonVertices(XmlReader.Element object, int tileHeight) {
		// The first child named "polygon" is the actual polygon hitbox
		XmlReader.Element polygon = null;
		if ((polygon = object.getChildByName("polygon")) != null) {
			// Borrowed code from libgdx BaseTmxMapLoader.java
			// With fix to vertices coordinates
			String[] points = polygon.getAttribute("points").split(" ");
			float[] vertices = new float[points.length * 2];
			for (int i = 0; i < points.length; i++) {
				String[] point = points[i].split(",");
				vertices[i * 2] = Float.parseFloat(point[0]) + object.getFloatAttribute("x");
				vertices[i * 2 + 1] = (tileHeight - object.getFloatAttribute("y")) - Float.parseFloat(point[1]);
			}
			return vertices;
		}
		return null;
	}
}
