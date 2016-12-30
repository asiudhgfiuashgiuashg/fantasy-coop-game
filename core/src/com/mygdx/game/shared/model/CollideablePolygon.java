package com.mygdx.game.shared.model;

import java.util.Arrays;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;

/**
 * A Polygon which can detect collision with other polygons. For collision
 * detection to work, a CollideablePolygon must be convex and the vertices must
 * be specified in counter-clockwise order.
 *
 * @author elimonent
 * @author Sawyer Harris
 */
public class CollideablePolygon extends Polygon {
	/**
	 * Used to check for intersection between various geometric objects
	 */
	private static final Intersector INTERSECTOR = new Intersector();
	// the y position above which another entity should be rendered behind
	// instead of in front of the entity with this CollideablePolygon hitbox.
	// Relative to the polygon's origin NOT the world origin. Use
	// getTransformedCutoffY for that.
	private float cutoffY;

	public CollideablePolygon(float[] vertices) {
		super(vertices);
		cutoffY = calcCutoffY();
	}

	/**
	 * Constructs a copy of a given polygon.
	 * 
	 * @param polygon
	 *            polygon to copy
	 */
	public CollideablePolygon(CollideablePolygon polygon) {
		this(Arrays.copyOf(polygon.getVertices(), polygon.getVertices().length));
		setOrigin(polygon.getOriginX(), polygon.getOriginY());
		setPosition(polygon.getX(), polygon.getY());
		setRotation(polygon.getRotation());
		setScale(polygon.getScaleX(), polygon.getScaleY());
	}

	/**
	 * estimate the y position (relative to the collideablepolygon origin)
	 * above which another entity should be
	 * rendered behind instead of in front of the entity with this
	 * CollideablePolygon hitbox
	 * @return
	 */
	private float calcCutoffY() {
		return 6; // TODO - esitmate the cutoffy
	}


	/**
	 * @param other
	 *            The other CollideablePolygon - vertices must be in
	 *            counter-clockwise order
	 * @param mtv
	 *            The minimum magnitude vector required to push {@code this}
	 *            polygon out of collision with {@code other}
	 * @return true if there is collision. Also populates mtv.
	 */
	public boolean collides(CollideablePolygon other, Intersector.MinimumTranslationVector mtv) {
		return INTERSECTOR.overlapConvexPolygons(this.getVertices(), other.getVertices(), mtv);
	}


	/**
	 * A helper method to allow graceful iteration through all consecutive edge
	 * pairs in areVerticesConvex() using index wrapping. AKA treating the
	 * linear array as a circular array.
	 *
	 * @param i
	 *            The virtual index
	 * @param arrLength
	 *            The length of the array.
	 * @return The actual index. == i if i < arrLength.
	 */
	private int wrapIndex(int i, int arrLength) {
		return i % arrLength;
	}

	/**
	 * check that there aren't an odd number of floats in the array.
	 *
	 * @param vertices
	 * @return
	 */
	private boolean notOddNumberOf(float[] vertices) {
		if (vertices.length % 2 == 1) { // odd #
			return false;
		}

		return true;
	}


	public float getCutoffY() {
		return cutoffY;
	}

	public float getTransformedCutoffY() {
		float transformedCutoffY = cutoffY;
		transformedCutoffY *= getScaleY();

		//TODO account for rotation

		transformedCutoffY += getY() + getOriginY();

		return transformedCutoffY;
	}
}