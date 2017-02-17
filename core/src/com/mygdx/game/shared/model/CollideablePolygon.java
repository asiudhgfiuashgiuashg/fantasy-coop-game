package com.mygdx.game.shared.model;

import java.util.Arrays;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.PolygonObject;

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

	/**
	 * The y position above which another entity should be rendered behind
	 * instead of in front of the entity with this CollideablePolygon
	 * hitbox.
	 * Relative to the polygon's origin NOT the world origin. Use
	 * getTransformedCutoffY for that.
	 */
	private float cutoffY;

	/**
	 * The maximum distance from the polygon's origin to any of its
	 * vertices.
	 */
	private float maxLength;

	/**
	 * no-arg constructor for serialization
	 */
	public CollideablePolygon() {

	}

	public CollideablePolygon(float[] vertices) {
		super();
		if (vertices != null) {
			setVertices(vertices); //smh
		}


		cutoffY = calcCutoffY();
		updateMaxLength();
	}

	/**
	 * proactively calculate the max length value
	 */
	protected void updateMaxLength() {
		maxLength = calcMaxLength();
	}

	/**
	 * Constructs a copy of a given polygon.
	 *
	 * @param polygon polygon to copy
	 */
	public CollideablePolygon(CollideablePolygon polygon) {
		this(polygon.getVertices());
		setOrigin(polygon.getOriginX(), polygon.getOriginY());
		setPosition(polygon.getX(), polygon.getY());
		setRotation(polygon.getRotation());
		setScale(polygon.getScaleX(), polygon.getScaleY());
	}

	/**
	 * Checks for a collision with another polygon object.
	 *
	 * @param other the other polygonobject
	 * @return true if there is a collision
	 */
	public boolean collides(CollideablePolygon other) {
		Vector2 diff = new Vector2(other.getX() - this.getX(), other.getY() - this.getY());
		if (diff.len() > other.getMaxLength() + this.getMaxLength()) {
			return false;
		}
		return INTERSECTOR.overlapConvexPolygons(this, other);
	}


	/**
	 * estimate the y position (relative to the collideablepolygon origin)
	 * above
	 * which another entity should be rendered behind instead of in
	 * front of
	 * the
	 * entity CollideablePolygon hitbox
	 *
	 * @return
	 */
	private float calcCutoffY() {
		float[] vertices = getVertices();
		boolean crossProductPositive = true;
		float highestNookY = 0; // aka the cutoffY
		/*
		 * Check cross product of consecutive edges. i is the index of the x
		 * value for the first point of vectorTwo, so i + 1 is the y value for
		 * the first point, and i + 2 is the x value of the second point of
		 * vectorTwo, and i + 3 is the y value of the second point.
		 *
		 * Nook points will have negative cross product, non-nooks will have
		 * positive. If the polygon wrapping (order points are specified) is
		 * reversed, the opposite will be true.
		 */
		for (int i = 0; i < vertices.length; i += 2) {

			Vector2 vectorOne = new Vector2(vertices[wrapIndex(i + 2, vertices.length)] - vertices[i], vertices[wrapIndex(i + 3, vertices.length)] - vertices[i + 1]);
			Vector2 vectorTwo = new Vector2(vertices[wrapIndex(i + 4, vertices.length)] - vertices[wrapIndex(i + 2, vertices.length)], vertices[wrapIndex(i + 5, vertices.length)] - vertices[wrapIndex(i + 3, vertices.length)]);
			crossProductPositive = vectorOne.crs(vectorTwo) > 0;

			if (!crossProductPositive) { // found nook
				float nookY = vertices[wrapIndex(i + 3, vertices.length)];
				if (nookY > highestNookY) {
					highestNookY = nookY;
				}
			}
		}
		return highestNookY;

	}

	/**
	 * Uses the largest dimension of the bounding rectangle as the
	 * maxLength
	 * for
	 * collisions.
	 *
	 * @return max[ rectangle width, rectangle height ]
	 */
	private float calcMaxLength() {
		if (getVertices().length == 0) {
			System.out.println("returning zero for max length");
			return 0;
		}
		Rectangle bounds = getBoundingRectangle();
		return Math.max(bounds.getWidth(), bounds.getHeight());
	}

	/**
	 * A helper method to allow graceful iteration through all consecutive
	 * edge
	 * pairs in areVerticesConvex() using index wrapping. AKA treating the
	 * linear array as a circular array.
	 *
	 * @param i         The virtual index
	 * @param arrLength The length of the array.
	 * @return The actual index. == i if i < arrLength.
	 */
	private int wrapIndex(int i, int arrLength) {
		return i % arrLength;
	}

	/**
	 * Returns the untransformed cutoffY used for rendering.
	 *
	 * @return cutoffY
	 */
	public float getCutoffY() {
		return cutoffY;
	}

	/**
	 * Returns the maxLength used for collision checking.
	 *
	 * @return maxLength
	 */
	public float getMaxLength() {
		return maxLength;
	}

	/**
	 * Returns the transformed cutoffY which accounts for the polygon's
	 * scaleY
	 * and y and originY values.
	 *
	 * @return transformed cutoffY
	 */
	public float getTransformedCutoffY() {
		float transformedCutoffY = cutoffY;
		transformedCutoffY *= getScaleY();

		// TODO account for rotation

		transformedCutoffY += getY() + getOriginY();

		return transformedCutoffY;
	}

	public void setPosition(Vector2 position) {
		setPosition(position.x, position.y);
	}
}