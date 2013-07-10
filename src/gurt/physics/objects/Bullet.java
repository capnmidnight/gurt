/**
 * FILE: Bullet.java
 * DATE: November 9th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics.objects;

import java.awt.Color;
import java.awt.Polygon;
import gurt.physics.Renderable;

public class Bullet extends Renderable
{
	//TODO extract model to data file
	private static final int[]		xs			= new int[] { -2, 0, 1, 1, 2, 0 };
	private static final int[]		ys			= new int[] { 1, 0, -2, 0, 2, 1 };
	private static final Polygon	bulPoly		= new Polygon(xs, ys, xs.length);
	private static final long		MAX_LIFE	= 3000;
	private static final float		MAX_SPEED	= 0.2f;
	private static final float		MASS		= 100.0f;

	public Bullet(float x, float y, float dx, float dy, float dir)
	{
		super(bulPoly, x, y, dx + MAX_SPEED * (float)Math.cos(dir), dy + MAX_SPEED
				* (float)Math.sin(dir), MASS, MAX_LIFE, dir, false, true, false,
				false, Color.GREEN);
		float jump = (float)(Math.sqrt(dx * dx + dy * dy) + 10) * 3;
		this.x += jump * Math.cos(dir);
		this.y += jump * Math.sin(dir);
	}

	@Override
	public void update(float dt)
	{
		this.x += this.vx * dt;
		this.y += this.vy * dt;
		this.life -= dt;
		this.dir += 0.2;
	}
        
    @Override public void renderMap(java.awt.Graphics2D g, float sx, float sy, float tx, float ty)
    {
    }
}
