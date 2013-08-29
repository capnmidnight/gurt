/**
 * FILE: Massive.java
 * DATE: November 9th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics;

import java.awt.Color;
import java.awt.Polygon;

/**
 * A Massive object is an object that exerts gravitational force.
 * 
 * @author stm
 * 
 */
public abstract class Massive extends Renderable
{
	/**
	 * Main constructor. Massive objects exert gravitational force. They are
	 * solid objects that do not move or die.
	 * 
	 * @param poly
	 *            the shape of the object
	 * @param x
	 *            the location
	 * @param y
	 *            the location
	 * @param mass
	 * @param dir
	 *            the angle of rotation
	 * @param rotates
	 *            true if the object rotates
	 * @param color
	 *            the color of the object
	 */
	public Massive(Polygon poly, float x, float y, float mass, float dir,
			boolean rotates, Color color)
	{
		super(poly, x, y, 0, 0, mass, Long.MAX_VALUE, dir, rotates, true, true,
				false, color);
	}

	@Override
	public void update(float dt)
	{
		super.update(dt);
		life = Long.MAX_VALUE;
	}
}
