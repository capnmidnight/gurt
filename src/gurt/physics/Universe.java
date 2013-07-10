/**
 * FILE: Universe.java DATE: November 7th, 2006 AUTHOR: Sean T. McBeth EMAIL:
 * capn.midnight@gmail.com
 */
package gurt.physics;

import gurt.client.screens.Screen;
import gurt.client.sound.AudioOutput;
import gurt.physics.objects.Planet;
import gurt.physics.objects.Rock;
import gurt.physics.objects.Star;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * The entire game universe. Contains all the stars, planets, rocks, and ships
 * in the game.
 * 
 * @author stm
 */
public class Universe
{
	private World 					world;
	private ArrayList<BilliardBall>	decorations, dynamicObjects, staticObjects;
	private Renderable				focusObj;
	private Random					rand;
	private GravitationalField		gravField;
	private static final int		STAR_COUNT	= 100000;
	private static final int		STAR_RANGE	= (int) (Math.sqrt(STAR_COUNT) * 200);
	private static final int		STAR_MID	= STAR_RANGE / 2;
	private static final int		ROCK_COUNT	= 100;
	private static final int		ROCK_RANGE	= (int) (Math.sqrt(ROCK_COUNT) * 500);
	private static final int		ROCK_MID	= ROCK_RANGE / 2;
	private int						minX, minY, maxX, maxY, hitCount;
	private float[]				bounds;

	/**
	 * Initialize a new universe of objects. Asteroids, stars, planets, etc.
	 */
	public Universe()
	{
		Vec2 gravity = new Vec2(0, -1);
		world = new World(gravity, true);
		
		decorations = new ArrayList<BilliardBall>();
		dynamicObjects = new ArrayList<BilliardBall>();
		staticObjects = new ArrayList<BilliardBall>();
		gravField = new GravitationalField();
		
		rand = new Random();
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
		for (int i = 0; i < STAR_COUNT; ++i)
		{
			int x = rand.nextInt(STAR_RANGE) - STAR_MID;
			int y = rand.nextInt(STAR_RANGE) - STAR_MID;
			minX = Math.min(x, minX);
			minY = Math.min(y, minY);
			maxX = Math.max(x, maxX);
			maxY = Math.max(y, maxY);
			decorations.add(new Star(x, y));
		}
		for (int i = 0; i < ROCK_COUNT; ++i)
		{
			int x = ROCK_RANGE * i / ROCK_COUNT - ROCK_MID;
			int y = ROCK_RANGE * i / ROCK_COUNT - ROCK_MID;
			x += rand.nextInt(ROCK_RANGE / 5);
			y += rand.nextInt(ROCK_RANGE / 5);
			minX = Math.min(x, minX);
			minY = Math.min(y, minY);
			maxX = Math.max(x, maxX);
			maxY = Math.max(y, maxY);
			float dx = (float)((rand.nextDouble() - 0.5) / 25 + 0.5);
			float dy = (float)((rand.nextDouble() - 0.5) / 25 + 0.5);
			Rock rock = new Rock(x, y, dx, dy);
			addObject(rock);
		}
		this.bounds = new float[] { minX, minY, maxX - minX, maxY - minY };
		Planet krank = new Planet(3000, -2000, 500, 10000, "Krank");
		Planet rinx = new Planet(-7500, 3000, 250, 5000, "Rinx");
		Planet gurt = new Planet(-10000, -5000, 1000, 50000, "Gurt");
		Planet jeez = new Planet(8000, -15000, 1500, 50000, "Jeez");
		addObject(krank);
		addObject(rinx);
		addObject(gurt);
		addObject(jeez);
	}

	/**
	 * Add an object to the universe. Usually just bullets.
	 * 
	 * @param obj the object to add.
	 */
	public void addObject(Renderable obj)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(obj.x, obj.y);
			
		if(obj.isStatic())
		{
			bodyDef.type = BodyType.STATIC;
			staticObjects.add(obj);
		}
		else
		{
			bodyDef.type = BodyType.DYNAMIC;
			dynamicObjects.add(obj);
		}
		if(obj.isMassive())
		{
			gravField.addMass(obj);
		}
		
		Body body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		Vec2[] poly = new Vec2[obj.poly.npoints];
		for(int i = 0; i < poly.length; ++i)
		{
			poly[i] = new Vec2(obj.poly.xpoints[i], obj.poly.ypoints[i]);
		}
		shape.set(poly, poly.length);
		body.createFixture(shape, 1);//(float)(obj.getMass() / (obj.getRadius() * obj.getRadius() * Math.PI)));
		obj.body = body;
	}

	/**
	 * Set the object to focus on.
	 * 
	 * @param obj
	 */
	public void setFocus(Renderable obj)
	{
		if (focusObj == null)
		{
			focusObj = obj;
			addObject(obj);
		}
	}

	/**
	 * Test an object to see if it is on the screen
	 * 
	 * @param x the location of the upper left corner of the screen
	 * @param y the location of the upper left corner of the screen
	 * @param obj the object to test
	 * @return true if the object is on the screen
	 */
	private boolean onScreen(double x, double y, BilliardBall obj)
	{
		return x <= obj.x && obj.x < x + Screen.width && y <= obj.y
				&& obj.y < y + Screen.height;
	}

	/**
	 * move a specific object and paint it if it is on screen
	 * 
	 * @param g the Graphics2D object to use for rendering
	 * @param list a list of objects to paint
	 * @param x the x component of the upper-left coordinate of the screen
	 * @param y the y component of the upper-left coordinate of the screen
	 */
	private void render(Graphics2D g, ArrayList<BilliardBall> list, double x,
			double y)
	{
		for (int i = 0; i < list.size(); ++i)
		{
			Renderable obj = (Renderable) list.get(i);
			if (obj.isMassive() || onScreen(x, y, obj))
			{
				obj.render(g);
			}
		}
	}

	/**
	 * paint any objects that are on screen
	 * 
	 * @param g the Graphics2D object to use for rendering
	 */
	public void render(Graphics2D g)
	{
		AffineTransform temp = null;
		if (g != null) temp = g.getTransform();
		double x = focusObj.x - Screen.width / 2;
		double y = focusObj.y - Screen.height / 2;
		if (g != null)
		{
			g.translate(-x, -y);
			render(g, decorations, x, y);
			render(g, staticObjects, x, y);
			render(g, dynamicObjects, x, y);
			g.setTransform(temp);
		}
	}
	private static float step = 1.0f / 60.0f; 
	public void update(float dt)
	{
		for (int i = dynamicObjects.size() - 1; i >= 0; --i)
		{
			BilliardBall obj = dynamicObjects.get(i);
			obj.update(dt);
			gravitate(obj, dt);
			if(obj.life <= 0)
			{
				dynamicObjects.remove(i);
			}
		}
		for(float temp = 0; temp <= dt; temp += step)
			world.step(step, 10, 10);
	}

	/**
	 * allow a specific object to be effected by the gravitational field for a
	 * certain amount of time.
	 * 
	 * @param obj the object to accelerate
	 * @param dt the amount of time to accelerate for
	 */
	private void gravitate(BilliardBall obj, float dt)
	{
		Vec2 v = gravField.getGravitation(obj);
		if (v != null)
		{
			obj.body.applyForce(v, obj.body.getPosition());
		}
	}
	
	public void sound(AudioOutput audio)
	{
		for (int i = 0; i < hitCount; ++i)
		{
			audio.playSound(rand.nextInt(6) + 1);
		}
	}

	/**
	 * @return the min x/y values and the width/height of the universe
	 */
	public float[] getBounds()
	{
		return this.bounds;
	}

	/**
	 * @return all the objects in the universe.
	 */
	public ArrayList<Renderable> getMapObjects()
	{
		ArrayList<Renderable> temp = new ArrayList<Renderable>();
		for (BilliardBall b : dynamicObjects)
		{
			temp.add((Renderable) b);
		}
		for (BilliardBall b : staticObjects)
		{
			temp.add((Renderable) b);
		}
		return temp;
	}
}
