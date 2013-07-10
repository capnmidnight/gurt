/**
 * FILE: Ship.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import org.jbox2d.common.Vec2;

import gurt.physics.Renderable;

/**
 * A rocket ship that is controlled by the user
 * 
 * @author stm
 */
public class Ship extends Renderable {
    // TODO extract model to data file
    private static final int[] xs1 = new int[]{-5, 10, -5, 0};
    private static final int[] ys1 = new int[]{-5, 0, 5, 0};
    private static final Polygon shipPoly = new Polygon(xs1, ys1,
            xs1.length);
    // TODO extract model to data file
    public static final int[] xs2 = new int[]{-4, -1, -4, -10};
    public static final int[] ys2 = new int[]{-2, 0, 2, 0};
    public static final Polygon forePoly = new Polygon(xs2, ys2,
            xs2.length);
    // TODO extract model to data file
    public static final int[] xs3 = new int[]{11, 10, 11, 11};
    public static final int[] ys3 = new int[]{-5, 0, 5, 0};
    public static final Polygon aftPoly = new Polygon(xs3, ys3,
            xs3.length);
    // TODO extract model to data file
    public static final int[] xs4 = new int[]{-13, -6, 0, 6,
        10, 6, 0, -6
    };
    public static final int[] ys4 = new int[]{0, 2, 3, 2, 0,
        -2, -3, -2
    };
    public static final Polygon hypPoly = new Polygon(xs4, ys4,
            xs4.length);
    public static final Polygon[] thrust = new Polygon[]{aftPoly,
        null, forePoly, hypPoly
    };
    public static final Color[] thrustColor = new Color[]{Color.CYAN,
        null, Color.RED, Color.BLUE
    };
    private static final float THRUST = 0.00025f;
    private static final float TURN_RATE = 0.007f;
    private int thrustOn,  turnOn,  heat;

    /**
     * Create a new ship at the given location
     * 
     * @param x
     *            the location
     * @param y
     *            the location
     */
    public Ship(float x, float y) {
        super(shipPoly, x, y, 0, 0, 10.0f, Integer.MAX_VALUE, 0, true, true,
                false, false, Color.GREEN);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        // reduce gun heat
        heat -= dt;
        // calculate thrust vector
        if (thrustOn != 0) {
            float ax = (float)(thrustOn * THRUST * Math.cos(dir));
            float ay = (float)(thrustOn * THRUST * Math.sin(dir));
            this.body.applyForce(new Vec2(ax * this.mass, ay * this.mass), body.getPosition());
        }
        // rotate the ship
        if (turnOn != 0) {
            dir += turnOn * TURN_RATE * dt;
        }
        // turn on afterburner
        if (thrustOn == 2) {
            this.maxSpeed = 2.0f;
        } else {
            this.maxSpeed = 1.0f;
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        AffineTransform temp = g.getTransform();
        g.translate(this.x, this.y);
        g.setColor(color);
        g.rotate(dir);
        int i = thrustOn + 1;
        if (thrust[i] != null) {
            g.setColor(thrustColor[i]);
            g.drawPolygon(thrust[i]);
        }
        g.setTransform(temp);
    }

    /**
     * Set the current thrust level/direction
     * 
     * @param on
     *            the thrust level:<br>
     *            -1 = reverse<br>
     *            0 = off<br>
     *            1 = normal<br>
     *            2 = afterburner
     */
    public void thrust(int on) {
        thrustOn = on;
    }

    /**
     * set the turn direction
     * 
     * @param on
     *            the turn direction value:<br>
     *            -1 = left <br>
     *            0 = none<br>
     *            1 = right<br>
     */
    public void turn(int on) {
        turnOn = on;
    }

    /**
     * @return true if the ship's gun heat is low enough to fire
     */
    public boolean canFire() {
        return heat <= 0;
    }

    /**
     * Creates a new bullet object as if the ship had fired the bullet
     * 
     * @return a bullet traveling in the direction relative to the ship that the
     *         ship is facing
     */
    public Bullet fireBullet() {
        heat = 200;
        return new Bullet(x, y, vx, vy, dir);
    }

    public int getThrust() {
        return thrustOn;
    }

    @Override
    public void renderMap(java.awt.Graphics2D g, float sx, float sy, float tx, float ty) {
        g.setColor(color);
        int mx = (int) ((this.getX() + tx) * sx);
        int my = (int) ((this.getY() + ty) * sy);
        g.drawLine(0, my, gurt.client.screens.Screen.width, my);
        g.drawLine(mx, 0, mx, gurt.client.screens.Screen.height);
        g.setColor(Color.ORANGE);
        g.drawString("X:" + x, 10, 40);
        g.drawString("Y:" + x, 10, 55);
        g.drawString("dX:" + vx, 10, 70);
        g.drawString("dY:" + vy, 10, 85);
    }
}
