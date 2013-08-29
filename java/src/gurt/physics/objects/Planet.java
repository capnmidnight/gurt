/**
 * FILE: Planet.java
 * DATE: November 9th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;

import gurt.physics.Massive;

/**
 * The main gravity source in the universe. Not much else for now.
 * 
 * @author stm
 * 
 */
public class Planet extends Massive {

    private static final BasicStroke stroke = new BasicStroke(4);
    private String name;
    private static final Font font = new Font("Arial", Font.PLAIN,
            20);

    /**
     * Creates a new, named planet of a given size and mass
     * 
     * @param x
     * @param y
     * @param radius
     * @param mass
     * @param name
     */
    public Planet(float x, float y, float radius, float mass, String name) {
        super(Planet.makePolygon(radius), x, y, mass, 0, false, Color.BLUE);
        this.radius = radius;
        this.name = name;
    }

    private static Polygon makePolygon(float size) {
        int n = (int) (size * size) / 5000;
        int[] xs = new int[n];
        int[] ys = new int[n];
        for (int i = 0; i < n; ++i) {
            float a = (float)(i * Math.PI * 2 / n);
            xs[i] = (int) ((Math.cos(a)) * size);
            ys[i] = (int) ((Math.sin(a)) * size);
        }
        return new Polygon(xs, ys, n);
    }

    @Override
    public void update(float dt) {
        // make sure the planet never moves and never gains energy.
        vx = 0;
        vy = 0;
    }

    @Override
    public void render(Graphics2D g) {
        Stroke temp = g.getStroke();
        g.setStroke(stroke);
        super.render(g);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(this.name, (int) this.x, (int) this.y);
        g.setStroke(temp);
    }

    @Override
    public void renderMap(java.awt.Graphics2D g, float sx, float sy, float tx, float ty) {
        g.setColor(color);
        float r = radius * sx;
        int mx = (int) ((this.getX() + tx) * sx);
        int my = (int) ((this.getY() + ty) * sy);
        g.drawOval((int) (mx - r), (int) (my - r), (int) (r * 2), (int) (r * 2));
        g.drawString(this.getName(), mx, my);
    }

    public String getName() {
        return name;
    }
}
