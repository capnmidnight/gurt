package gurt.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Decal used for rendering the representation of a physical object.
 * 
 * A physical object in the game universe is modeled on a server. The game
 * client is merely an interface to that data on the server. Graphical elements
 * are then decals that are painted in the location of that object. The <b>Decal</b>
 * interface defines an interface for retrieving the data that defines the
 * appearance of the decal.
 * 
 * @author smcbeth
 * 
 */
public class Decal implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	private Polygon				poly;
	private Color				colFill, colStroke;
	private int[]				dim;
	private int					cap, join, strokeID;
	private float				lineWidth;
	private Decal[]				subDecal;

	public Decal(Polygon poly, Color fill, Color stroke, float lineWidth,
			int cap, int join, Decal[] subDecal)
	{
		this.poly = poly;
		this.dim = new int[2];
		int minx, maxx, miny, maxy;
		minx = miny = Integer.MAX_VALUE;
		maxx = maxy = Integer.MIN_VALUE;
		Rectangle temp = poly.getBounds();
		minx = Math.min(minx, (int) temp.getX());
		miny = Math.min(miny, (int) temp.getY());
		maxx = Math.max(maxx, (int) (temp.getX() + temp.getWidth()));
		maxy = Math.max(maxy, (int) (temp.getX() + temp.getHeight()));
		if (subDecal != null)
		{
			for (int i = 0; i < subDecal.length; ++i)
			{
				temp = subDecal[i].poly.getBounds();
				minx = Math.min(minx, (int) temp.getX());
				miny = Math.min(miny, (int) temp.getY());
				maxx = Math.max(maxx, (int) (temp.getX() + temp.getWidth()));
				maxy = Math.max(maxy, (int) (temp.getX() + temp.getHeight()));
			}
		}
		this.dim[0] = maxx - minx;
		this.dim[1] = maxy - miny;
		this.colFill = fill;
		this.colStroke = stroke;
		this.lineWidth = lineWidth;
		this.subDecal = subDecal;
		this.cap = cap;
		this.join = join;
		createStroke();
	}

	private void createStroke()
	{
		this.strokeID = StrokeManager.createStroke(this.lineWidth, this.cap,
				this.join);
	}

	public void render(Graphics2D g, int x, int y, double a)
	{
		AffineTransform temp = g.getTransform();
		g.translate(x, y);
		g.rotate(a);
		// sub decals are considered background to the main decal
		if (this.subDecal != null)
		{
			for (Decal d : this.subDecal)
			{
				d.render(g, 0, 0, 0);
			}
		}
		// draw the main decal
		if (this.colFill != null)
		{
			g.setStroke(StrokeManager.getStroke(this.strokeID));
			g.setColor(this.colFill);
			g.fillPolygon(this.poly);
		}
		if (this.colStroke != null)
		{
			g.setColor(this.colStroke);
			g.drawPolygon(this.poly);
		}
		g.setTransform(temp);
	}

	public static Decal loadDecal(InputStream is) throws IOException,
			ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(is);
		Decal d = (Decal) ois.readObject();
		d.createStroke();
		return d;
	}

	public static Decal loadDecal(String filename)
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		return loadDecal(new File(filename));
	}

	public static Decal loadDecal(File file) throws FileNotFoundException,
			IOException, ClassNotFoundException
	{
		return loadDecal(new FileInputStream(file));
	}

	public int[] getSize()
	{
		return this.dim;
	}

	public BasicStroke getStroke()
	{
		return StrokeManager.getStroke(this.strokeID);
	}

	public ArrayList<Decal> getSubDecals()
	{
		ArrayList<Decal> temp = new ArrayList<Decal>();
		for (Decal d : this.subDecal)
		{
			temp.add(d);
		}
		return temp;
	}
	
	public Color getStrokeColor()
	{
		return this.colStroke;
	}
	
	public Color getFillColor()
	{
		return this.colFill;
	}

	public ArrayList<Point> getPoints()
	{
		ArrayList<Point> temp = new ArrayList<Point>();
		for (int i = 0; i < poly.npoints; ++i)
		{
			temp.add(new Point(poly.xpoints[i], poly.ypoints[i]));
		}
		return temp;
	}

	public void save(File fout) throws FileNotFoundException, IOException
	{
		FileOutputStream fos = new FileOutputStream(fout);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
	}
}
