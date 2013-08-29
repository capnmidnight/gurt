/**
 * FILE: Renderable.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

/**
 * An object that can be painted to the scene. It has a shape, a color, and
 * various attributes.
 * 
 * @author stm
 */
public abstract class Renderable extends BilliardBall {

    private static final boolean DEBUG = false;
    protected Polygon poly;
    protected float dir;
    protected boolean rotates = false,  solid = true,  massive = false,  staticObj = false;
    protected Color color;

    /**
     * 
     * @param poly
     *            the shape of the object
     * @param x
     *            the location
     * @param y
     *            the location
     * @param dx
     *            the velocity
     * @param dy
     *            the velocity
     * @param mass
     * @param life
     *            determines how much damage the object can take
     * @param dir
     *            the heading of the object
     * @param rotates
     *            true if the object rotates
     * @param solid
     *            true if the object is involved in collisions
     * @param massive
     *            true if the object exerts a gravitational field
     * @param staticObj
     *            true if the object does not move
     * @param color
     *            the color to use for painting
     */
    public Renderable(Polygon poly, float x, float y, float dx, float dy,
            float mass, long life, float dir, boolean rotates, boolean solid,
            boolean massive, boolean staticObj, Color color) {
        super(x, y, dx, dy, Math.max((float)poly.getBounds2D().getWidth(), (float)poly.getBounds2D().getHeight()) / 2f, mass);
        this.poly = poly;
        this.life = life;
        this.dir = dir;
        this.rotates = rotates;
        this.solid = solid;
        this.massive = massive;
        this.staticObj = staticObj;
        this.color = color;
        this.maxSpeed = 1.0f;
    }

    /**
     * If this object is not a static object, updates the location based on the
     * current velocity of the object
     * 
     * @param dt
     *            the number of milliseconds since the last update
     */
    @Override
    public void update(float dt) {
        if (!staticObj) {
            super.update(dt);
        }
    }

    /**
     * Draw the object
     * 
     * @param g
     *            the Graphics2D object to use for rendering
     */
    public void render(Graphics2D g) {
        AffineTransform temp = g.getTransform();
        g.translate(this.x, this.y);
        g.setColor(color);
        if (rotates) {
            g.rotate(dir);
        }
        g.drawPolygon(poly);
        if (DEBUG) {
            g.setColor(Color.WHITE);
            g.drawOval(-(int) this.radius, -(int) this.radius,
                    (int) this.radius * 2, (int) this.radius * 2);
        }
        g.setTransform(temp);
    }

    public abstract void renderMap(Graphics2D g, float sx, float sy, float tx, float ty);

    /**
     * @return true if the object is used in collision interactions
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * @return true if the object has a gravitational field
     */
    public boolean isMassive() {
        return massive;
    }

    /**
     * gets the color used for painting
     * 
     * @return java.awt.Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return true if the object does not update its position
     */
    public boolean isStatic() {
        return staticObj;
    }
}
