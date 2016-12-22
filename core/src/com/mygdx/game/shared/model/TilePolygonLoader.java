package com.mygdx.game.shared.model;

import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.game.server.model.MapLoader;
import com.mygdx.game.shared.util.CollideablePolygon;

/**
 * loads tiles
 */
public class TilePolygonLoader {
	public static CollideablePolygon loadTilePolygon(XmlReader.Element
														 tileElement) {
		// Load tiles that have polygon hitboxes
		for (XmlReader.Element objGroup : tileElement.getChildrenByName
				("objectgroup")) {
			// The first child named "object" is considered the hitbox
			XmlReader.Element object = null;
			if ((object = objGroup.getChildByName("object")) != null) {
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
						vertices[i * 2 + 1] = object.getFloatAttribute("y") - Float.parseFloat(point[1]);
					}
					CollideablePolygon p = new CollideablePolygon(vertices);

					return p;

				}
			}
		}
		return null;
	}
}
