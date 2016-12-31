package com.mygdx.game.shared.model;

import java.util.Arrays;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.client.model.GameClient.console;

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
        cutoffY = calcCutoffY(vertices);
    }

    /**
     * Constructs a copy of a given polygon.
     *
     * @param polygon polygon to copy
     */
    public CollideablePolygon(CollideablePolygon polygon) {
        this(Arrays.copyOf(polygon.getVertices(), polygon.getVertices()
                .length));
        setOrigin(polygon.getOriginX(), polygon.getOriginY());
        setPosition(polygon.getX(), polygon.getY());
        setRotation(polygon.getRotation());
        setScale(polygon.getScaleX(), polygon.getScaleY());
    }


    /**
     * estimate the y position (relative to the collideablepolygon origin)
     * above which another entity should be rendered behind instead of in
     * front of the entity
     * CollideablePolygon hitbox
     *
     * @return
     */

    private float calcCutoffY(float[] vertices) {
        boolean crossProductPositive = true;
        float highestNookY = 0; // aka the cutoffY
		/*
		 * Check cross product of consecutive edges. i is the index of the x
		 * value for the first point of vectorTwo, so i + 1 is the y value
		 * for the first point, and i + 2 is the x value of the second point
		 * of vectorTwo, and i + 3 is the y value of the second point.
		 *
		 * Nook points will have negative cross product, non-nooks will have
		 * positive. If the polygon wrapping (order points are specified) is
		 * reversed, the opposite will be true.
		 */
        for (int i = 0; i < vertices.length; i += 2) {

            Vector2 vectorOne = new Vector2(vertices[wrapIndex(i + 2,
					vertices.length)] - vertices[i],
                    vertices[wrapIndex(i + 3, vertices.length)] - vertices[i
							+ 1]);
            Vector2 vectorTwo = new Vector2(
                    vertices[wrapIndex(i + 4, vertices.length)] -
							vertices[wrapIndex(i + 2, vertices.length)],
                    vertices[wrapIndex(i + 5, vertices.length)] -
							vertices[wrapIndex(i + 3, vertices.length)]);
            crossProductPositive = vectorOne.crs(vectorTwo) > 0;

            if (!crossProductPositive) { //found nook
                float nookY = vertices[wrapIndex(i + 3, vertices.length)];
                if (nookY > highestNookY) {
                    highestNookY = nookY;
                }
            }
        }

        return highestNookY;

    }

    /**
     * @param other The other CollideablePolygon - vertices must be in
     *              counter-clockwise order
     * @param mtv   The minimum magnitude vector required to push {@code this}
     *              polygon out of collision with {@code other}
     * @return true if there is collision. Also populates mtv.
     */
    public boolean collides(CollideablePolygon other, Intersector
			.MinimumTranslationVector mtv) {
        return INTERSECTOR.overlapConvexPolygons(this.getVertices(), other
				.getVertices(), mtv);
    }


    /**
     * A helper method to allow graceful iteration through all consecutive edge
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