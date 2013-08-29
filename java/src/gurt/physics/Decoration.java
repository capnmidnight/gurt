/**
 * FILE: Decoration.java
 * DATE: November 7th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics;

import java.awt.Color;
import java.awt.Polygon;

/**
 * A simple graphical feature that does not move or factor into physics
 * calculations.
 * 
 * @author stm
 * 
 */
public abstract class Decoration extends Renderable
{
	/**
	 * Main constructor, defines the shape, location, and color of the
	 * decoration.
	 * 
	 * @param poly
	 * @param x
	 * @param y
	 * @param color
	 */
	public Decoration(Polygon poly, float x, float y, Color color)
	{
		super(poly, x, y, 0, 0, 0, 0, 0, true, false, false, true, color);
	}
}
