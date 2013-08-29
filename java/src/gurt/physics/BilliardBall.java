/**
 * FILE: BillardBall.java
 * DATE: November 11th, 2006
 * AUTHOR: Sean T. McBeth
 * EMAIL: capn.midnight@gmail.com
 */
package gurt.physics;

import org.jbox2d.dynamics.Body;

/**
 * A rigid, circular object of a given radius and mass that is involved in
 * ideally elastic collisions. The basis for collision modelling in the
 * application.
 * 
 * @author stm
 * 
 */
public class BilliardBall
{
	protected float		x, y, vx, vy, radius, mass, maxSpeed;
	protected float[]	p;
	public Body		body;
	public long			life;

	/**
	 * Primary constructor. Defines location, velocity, radius, and mass.
	 * 
	 * @param x
	 *            component of location
	 * @param y
	 *            component of location
	 * @param vx
	 *            component of velocity
	 * @param vy
	 *            component of velocity
	 * @param radius
	 * @param mass
	 */
	public BilliardBall(float x, float y, float vx, float vy,
			float radius, float mass)
	{
		this.x = x;
		this.y = y;
		this.p = new float[2];
		this.vx = vx;
		this.vy = vy;
		this.radius = radius;
		this.mass = mass;
	}

	/**
	 * updates the displacement of a ball, given a certain amount of time to
	 * apply the ball's velocity. D = Vt.
	 * 
	 * @param t
	 */
	public void update(float t)
	{
	}

	/**
	 * Get the ball's radius
	 * 
	 * @return the radius of the ball, as a float value
	 */
	public float getRadius()
	{
		return radius;
	}

	/**
	 * Get the ball's mass
	 * 
	 * @return the mass of the ball, as a float value
	 */
	public float getMass()
	{
		return mass;
	}

	/**
	 * get the x component of the ball's location
	 * 
	 * @return the x component of the ball's location, as a float value
	 */
	public float getX()
	{
		return this.x;
	}

	/**
	 * set the x component of the ball's location
	 * 
	 * @param x
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * get the y component of the ball's location
	 * 
	 * @return the y component of the ball's location, as a float value
	 */
	public float getY()
	{
		return this.y;
	}

	/**
	 * set the y component of the ball's location
	 * 
	 * @param y
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * get the x component of the ball's velocity
	 * 
	 * @return the x component of the ball's velocity, as a float value
	 */
	public float getVx()
	{
		return vx;
	}

	/**
	 * set the x component of the ball's velocity
	 * 
	 * @param vx
	 */
	public void setVx(float vx)
	{
		this.vx = vx;
	}

	/**
	 * get the y component of the ball's velocity
	 * 
	 * @return the y component of the ball's velocity, as a float value
	 */
	public float getVy()
	{
		return vy;
	}

	/**
	 * set the y component of the ball's velocity
	 * 
	 * @param vy
	 */
	public void setVy(float vy)
	{
		this.vy = vy;
	}

	/**
	 * Calculate the kinetic energy of the ball. Kinetic energy is defined as
	 * 0.5 * mass * |velocity|^2
	 * 
	 * @return scalar value of kinetic energy
	 */
	public float getKineticEnergy()
	{
		return 0.5f * this.mass * (this.vx * this.vx + this.vy * this.vy);
	}

	/**
	 * Calculate the heading of the ball. The heading is the angular direction
	 * (in radians) that the ball is travelling.
	 * 
	 * @return value in radians of direction of travel
	 */
	public float getHeading()
	{
		return (float)Math.atan2(this.vy, this.vx);
	}

	/**
	 * Calculate the speed of the ball. The speed is the magnitude of the ball's
	 * velocity.
	 * 
	 * @return
	 */
	public float getSpeed()
	{
		return (float)Math.sqrt(getSquareSpeed());
	}

	/**
	 * Calculate the square of the speed of the ball. The speed is the magnitude
	 * of the ball's velocity. Using the square of this value avoids a costly
	 * square root calculation and is useful for certain optimizations.
	 * 
	 * @return
	 */
	public float getSquareSpeed()
	{
		return vx * vx + vy * vy;
	}

	/**
	 * Calculate the angle (in radians) of the vector from this ball to the
	 * parameter ball.
	 * 
	 * @param obj
	 *            another ball
	 * @return
	 */
	public float getBearingTo(BilliardBall obj)
	{
		float dx = obj.getX() - this.getX();
		float dy = obj.getY() - this.getY();
		return (float)Math.atan2(dy, dx);
	}

	/**
	 * Reflects the ball's velocity about a supplied normal vector. The vector
	 * may be normal to an immobile surface that the ball is colliding with.
	 * 
	 * @param normal
	 *            the angle to reflect about
	 */
	public void reflect(float normal)
	{
		float s = (float)Math.sin(normal);
		float c = (float)Math.cos(normal);
		float cs2 = c * s * 2;
		float ss = s * s;
		float cc = c * c;
		float temp = vx * (ss - cc) - vy * cs2;
		vy = vy * (cc - ss) - vx * cs2;
		vx = temp;
	}

	/**
	 * calculates the momentum of the ball. Momentum is mass times velocity, Vm,
	 * and is a vector quantity itself.
	 * 
	 * @return the x and y component of the momentum in an array of double.
	 *         Index 0 is the x component, index 1 is the y component.
	 */
	public float[] getMomentum()
	{
		p[0] = vx * mass;
		p[1] = vy * mass;
		return p;
	}
}
