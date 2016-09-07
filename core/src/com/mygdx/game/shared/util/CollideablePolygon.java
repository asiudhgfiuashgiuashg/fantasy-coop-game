package com.mygdx.game.shared.util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;


/**
 * A Polygon which can detect collision with other polygons.
 * For collision detection to work, a CollideablePolygon must be convex and
 * the vertices must be specified in counter-clockwise order.
 *
 * @author elimonent
 */
public class CollideablePolygon extends Polygon {
	/**
	 * Used to check for intersection between various geometric objects
	 */
	private static final Intersector INTERSECTOR = new Intersector();

	public CollideablePolygon(float[] vertices) {
		super(vertices);
		checkVerticesValidity(vertices);
	}

	/**
	 * Check if a set of vertices specify a valid polygon.
	 * A valid polygon is convex and specified counter-clockwise.
	 * Throw an exception if the vertices are not valid.
	 *
	 * @param vertices
	 */
	private void checkVerticesValidity(float[] vertices) {
		if (!notOddNumberOf(vertices)) {
			throw new IllegalArgumentException("The vertex array must have an even number of floats");
		}
		if (!areVerticesConvex(vertices)) {
			throw new IllegalArgumentException("Vertices must be specified in counter-clockwise order and must specify a convex polygon.");
		}
	}

	/**
	 * @param other The other CollideablePolygon - vertices must be in counter-clockwise order
	 * @param mtv   The minimum magnitude vector required to push {@code this} polygon out of collision with {@code other}
	 * @return true if there is collision. Also populates mtv.
	 */
	public boolean collides(CollideablePolygon other, Intersector.MinimumTranslationVector mtv) {
		return INTERSECTOR.overlapConvexPolygons(this.getVertices(), other.getVertices(), mtv);
	}

	/**
	 * Helper method to help enforce that vertices are convex.
	 * Method used: http://stackoverflow.com/a/1881201
	 * This method may give a false negative if the vertices are not specified in counterclockwise order.
	 *
	 * @param vertices
	 * @return whether polygon specified by these vertices is convex.
	 */
	private boolean areVerticesConvex(float[] vertices) {
		boolean crossProductPositive = true;

		/*
		 * Make sure all the pairs of consecutive edges have a positive cross product.
		 * i is the index of the x value for the first point of vectorTwo, so i + 1 is the y value for the first point,
		 *  and i + 2 is the x value of the second point of vectorTwo, and i + 3 is the y value of the second point.
		 */
		for (int i = 0; crossProductPositive && i < vertices.length; i += 2) {

			Vector2 vectorOne = new Vector2(vertices[wrapIndex(i + 2, vertices.length)] - vertices[i], vertices[wrapIndex(i + 3, vertices.length)] - vertices[i + 1]);
			Vector2 vectorTwo = new Vector2(vertices[wrapIndex(i + 4, vertices.length)]
					- vertices[wrapIndex(i + 2, vertices.length)],
					vertices[wrapIndex(i + 5, vertices.length)] - vertices[wrapIndex(i + 3, vertices.length)]);
			crossProductPositive = vectorOne.crs(vectorTwo) > 0;
		}

		return crossProductPositive;
	}

	/**
	 * A helper method to allow graceful iteration through all consecutive edge pairs in areVerticesConvex() using index wrapping.
	 * AKA treating the linear array as a circular array.
	 *
	 * @param i         The virtual index
	 * @param arrLength The length of the array.
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
		if (vertices.length % 2 == 1) { //odd #
			return false;
		}

		return true;
	}

	@Override
	public void setVertices(float[] vertices) {
		checkVerticesValidity(vertices);
		super.setVertices(vertices);
	}
}