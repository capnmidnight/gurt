/**
 * FILE: GravitationalField.java DATE: November 9th, 2006 AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.jbox2d.common.Vec2;

import gurt.util.ErrorHandler;

/**
 * Manages a vector field of gravitational forces. Properly accounts for mass,
 * uses a tweaked gravitational constant value.
 * 
 * @author stm
 */
public class GravitationalField
{
	private ArrayList<BilliardBall>	sources;
	// This is not the appropriate value of G. The real value of G is far too
	// small for the close proximity of objects in the game.
	// G = (6.6742 +- 0.001) x 10^-11 m^3 s^-2 kg^-1
	private static final float		G	= 0.0005f;

	/**
	 * Creates a new list to store references to objects that exert
	 * gravitational force.
	 */
	public GravitationalField()
	{
		sources = new ArrayList<BilliardBall>();
	}

	/**
	 * <b>addMass</b>
	 * <p>
	 * public void <b>addMass</b>(BilliardBall obj) Adds a new large object to
	 * the gravity field.
	 * 
	 * @param obj an object that exerts gravitational force
	 */
	public void addMass(BilliardBall obj)
	{
		sources.add(obj);
	}

	/**
	 * calculates a force vector for a specific object in the gravity field
	 * 
	 * @param obj - the object that will be effected by gravity
	 * @return a vector as an array of float. Index 0 is the x component and
	 *         index 1 is the y component.
	 */
	public Vec2 getGravitation(BilliardBall obj)
	{
		float x, y;
		x = y = 0;
		for (BilliardBall b : sources)
		{
			float dx = obj.getX() - b.getX();
			float dy = obj.getY() - b.getY();
			float theta = (float)Math.atan2(dy, dx);
			float dist_2 = dx * dx + dy * dy;
			float minDist = obj.getRadius() + b.getRadius();
			float minDist_2 = minDist * minDist;
			float mass = b.getMass();
			if (dist_2 < minDist_2)
			{
				mass *= dist_2 / minDist_2;
			}
			float val = mass / dist_2;
			x -= val * Math.cos(theta);
			y -= val * Math.sin(theta);
		}
		x *= obj.getMass() * G;
		y *= obj.getMass() * G;
		return new Vec2(x, y);
	}

	public void save(float[] bounds, int width, int height)
	{
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		float sx = bounds[2] / img.getWidth();
		float sy = bounds[3] / img.getHeight();
		BilliardBall rock = new BilliardBall(0, 0, 0, 0, 1, 1);
		float max = Float.MIN_VALUE;
		float min = Float.MAX_VALUE;
		for (int y = 0; y < img.getHeight(); ++y)
		{
			float ty = y * sy + bounds[1];
			rock.setY(ty);
			for (int x = 0; x < img.getWidth(); ++x)
			{
				float tx = x * sx + bounds[0];
				rock.setX(tx);
				Vec2 v = getGravitation(rock);
				float m = v.length();
				max = Math.max(m, max);
				min = Math.min(m, min);
			}
		}
		float s = 255 / (max - min);
		int[] c = new int[3];
		for (int y = 0; y < img.getHeight(); ++y)
		{
			float ty = y * sy + bounds[1];
			rock.setY(ty);
			for (int x = 0; x < img.getWidth(); ++x)
			{
				float tx = x * sx + bounds[0];
				rock.setX(tx);
				Vec2 v = getGravitation(rock);
				float m = v.length();
				c[0] = (int) (128.0 + 127.0 * v.x / m);
				c[1] = (int) (128.0 + 127.0 * v.y / m);
				c[2] = (int) (s * (m - min));
				img.setRGB(x, y, new Color(c[0], c[2], c[1]).getRGB());
			}
		}
		try
		{
			ClassLoader cl = this.getClass().getClassLoader();
			URL url = cl.getResource("out.png");
			ImageIO.write(img, "png", new File(url.toURI()));
		} catch (Exception e)
		{
			ErrorHandler.printError(e, "Could not save map image");
		}
	}
}
