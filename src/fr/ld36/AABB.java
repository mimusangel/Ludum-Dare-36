package fr.ld36;

import fr.mimus.jbasicgl.maths.Vec2;

public class AABB
{
	public int x;
	public int y;
	public int w;
	public int h;
	public AABB(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Vec2 collided(AABB other)
	{
		Vec2 vec = new Vec2();
		if (x > other.x + other.w || y > other.y + other.h || x + w <= other.x || y + h <= other.y)
			return (null);

		int x0 = x + (w / 2);
		int x1 = other.x + (other.w / 2);
		int y0 = y + (h / 2);
		int y1 = other.y + (other.h / 2);
		if (x0 < x1)
			vec.x = (x + w) - other.x;
		else if (x0 > x1)
			vec.x = x - (other.x + other.w);
		if (y0 < y1)
			vec.y = (y + h) - other.y;
		else if (y0 > y1)
			vec.y = y - (other.y + other.h);
		return (vec);
	}
	
	public boolean collided(int ox, int oy, int ow, int oh)
	{
		if (x > ox + ow || y > oy + oh || x + w <= ox || y + h <= oy)
			return (false);
		return (true);
	}

	public boolean collided(Vec2 p)
	{
		return (p.x >= x && p.x < x + w && p.y >= y && p.y < y + h);
	}
}
