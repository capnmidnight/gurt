/**
 * FILE: Star.java
 * DATE: November 9th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics.objects;

import java.awt.Color;
import java.awt.Polygon;

import gurt.physics.Decoration;

/**
 * A static star for display in the background. Provides a sense of movement
 * when travelling. Does not move.
 * 
 * @author stm
 */
public class Star extends Decoration {
    // TODO extract model to data file
    private static final int[] xs = new int[]{0, 2, 3, 0, 4};
    private static final int[] ys = new int[]{4, 0, 4, 1, 2};
    private static final Polygon starPoly = new Polygon(xs, ys, xs.length);

    /**
     * Creates a new Star at the provided location
     * 
     * @param x
     * @param y
     */
    public Star(int x, int y) {
        super(starPoly, x, y, Color.WHITE);
    }

    @Override
    public void renderMap(java.awt.Graphics2D g, float sx, float sy, float tx, float ty) {
    }
}
