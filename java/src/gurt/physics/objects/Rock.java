/**
 * FILE: Rock.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics.objects;

import java.awt.Color;
import java.awt.Polygon;

import gurt.physics.Renderable;

/**
 * A large rock that hurtles around space. It breaks into smaller rocks.
 * 
 * @author stm
 * 
 */
public class Rock extends Renderable {
    // TODO extract model to data file
    private static final int[] xs = new int[]{-20, -18, -10,
        -5, 0, 5, 10, 15, 22, 15, 10, 5, 0, -5, -10, -15
    };
    private static final int[] ys = new int[]{5, -5, -3, -15,
        -10, -20, -5, 0, 3, 10, 20, 15, 25, 17, 15, 10
    };
    private static final Polygon rockPoly = new Polygon(xs, ys, xs.length);

    /**
     * Create a new rock at the given location with the given velocity
     * 
     * @param x
     *            the x component of the location
     * @param y
     *            the y component of the location
     * @param dx
     *            the x component of the velocity
     * @param dy
     *            the y component of the velocity
     */
    public Rock(float x, float y, float dx, float dy) {
        super(rockPoly, x, y, dx, dy, 100.0f, Long.MAX_VALUE, 0, true, true,
                false, false, Color.GRAY);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        this.dir += 0.005;
    }

    @Override
    public void renderMap(java.awt.Graphics2D g, float sx, float sy, float tx, float ty) {
        g.setColor(color);
        int mx = (int) ((this.getX() + tx) * sx);
        int my = (int) ((this.getY() + ty) * sy);
        g.drawLine(mx - 1, my, mx + 1, my);
        g.drawLine(mx, my - 1, mx, my + 1);
    }
}
