package gurt.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import gurt.util.ISpatialObject;

public class QuadTreeNode
{
	private static final int	MODE_NONE	= 0;
	private static final int	MODE_LEAF	= 1;
	private static final int	MODE_NODE	= 2;
	private int					mode;
	private ISpatialObject		here;
	private Rectangle			bounds;
	private QuadTreeNode[]		sub;

	public QuadTreeNode(Rectangle bounds)
	{
		this.bounds = bounds;
		this.mode = MODE_NONE;
		this.here = null;
		this.sub = null;
	}

	public void add(ISpatialObject obj)
	{
		if (this.mode == MODE_NONE)
		{
			this.mode = MODE_LEAF;
			this.here = obj;
		} else if (this.mode == MODE_LEAF)
		{
			this.mode = MODE_NODE;
			ISpatialObject temp = this.here;
			this.here = null;
			this.sub = new QuadTreeNode[4];
			int w = this.bounds.width / 2;
			int h = this.bounds.height / 2;
			this.sub[0] = new QuadTreeNode(new Rectangle(this.bounds.x,
					this.bounds.y, w, h));
			this.sub[1] = new QuadTreeNode(new Rectangle(this.bounds.x + w,
					this.bounds.y, this.bounds.width - w, h));
			this.sub[2] = new QuadTreeNode(new Rectangle(this.bounds.x,
					this.bounds.y + h, w, this.bounds.height - h));
			this.sub[3] = new QuadTreeNode(new Rectangle(this.bounds.x + w,
					this.bounds.y + h, this.bounds.width - w,
					this.bounds.height - h));
			add(temp);
			add(obj);
		} else if (mode == MODE_NODE)
		{
			for (QuadTreeNode n : this.sub)
			{
				if (n.bounds.contains(obj.getPoint()))
				{
					n.add(obj);
					break;
				}
			}
		}
	}

	public void findBad(ArrayList<ISpatialObject> list)
	{
		if (this.mode == MODE_LEAF)
		{
			if (!this.bounds.contains(this.here.getPoint()))
			{
				list.add(this.here);
			}
		} else if (this.mode == MODE_NODE)
		{
			for (QuadTreeNode n : sub)
			{
				n.findBad(list);
			}
		}
	}

	public ISpatialObject get()
	{
		return this.here;
	}

	public Rectangle getBounds()
	{
		return this.bounds;
	}

	public void findPoints(Rectangle r, ArrayList<ISpatialObject> list)
	{
		if (this.mode == MODE_LEAF)
		{
			if (r.contains(this.here.getPoint()))
			{
				list.add(this.here);
			}
		} else if (this.mode == MODE_NODE)
		{
			for (QuadTreeNode n : this.sub)
			{
				if (r.intersects(n.bounds) || r.contains(n.bounds))
				{
					n.findPoints(r, list);
				}
			}
		}
	}

	public boolean remove(ISpatialObject p)
	{
		if (this.mode == MODE_LEAF)
		{
			if (this.here == p)
			{
				this.mode = MODE_NONE;
				this.here = null;
				return true;
			}
		} else if (this.mode == MODE_NODE)
		{
			boolean didRemove = false;
			for (QuadTreeNode n : this.sub)
			{
				if (n.remove(p))
				{
					didRemove = true;
					break;
				}
			}
			if (didRemove)
			{
				int count = 0;
				QuadTreeNode last = null;
				for (QuadTreeNode n : this.sub)
				{
					if (!n.isEmpty())
					{
						last = n;
						count++;
					}
				}
				if (count == 0)
				{
					this.sub = null;
					this.mode = MODE_NONE;
				} else if (count == 1)
				{
					this.here = last.here;
					this.sub = null;
					this.mode = MODE_LEAF;
				}
			}
			return didRemove;
		}
		return false;
	}

	public void draw(Graphics2D g)
	{
		if (this.mode == MODE_NONE)
		{
			g.setColor(Color.RED);
			g.drawLine(this.bounds.x, this.bounds.y, this.bounds.x
					+ this.bounds.width, this.bounds.y + this.bounds.height);
		} else if (this.mode == MODE_LEAF)
		{
			g.setColor(Color.GREEN);
			Point p = this.here.getPoint();
			g.drawOval(p.x - 2, p.y - 2, 4, 4);
		} else if (this.mode == MODE_NODE)
		{
			int w = this.bounds.width / 2;
			int h = this.bounds.height / 2;
			g.setColor(Color.YELLOW);
			g.drawLine(this.bounds.x, this.bounds.y + h, this.bounds.x
					+ this.bounds.width, this.bounds.y + h);
			g.drawLine(this.bounds.x + w, this.bounds.y, this.bounds.x + w,
					this.bounds.y + this.bounds.height);
			for (QuadTreeNode n : this.sub)
			{
				n.draw(g);
			}
		}
	}

	public boolean isEmpty()
	{
		boolean empty = true;
		if (this.mode == MODE_LEAF)
		{
			empty = false;
		} else if (this.mode == MODE_NODE)
		{
			for (QuadTreeNode n : sub)
			{
				empty &= n.isEmpty();
			}
		}
		return empty;
	}
}
