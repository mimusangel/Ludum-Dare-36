package fr.ld32;

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
	
	public boolean collided(AABB other)
	{
		return (collided(other.x, other.y, other.w, other.h));
	}
	
	public boolean collided(int ox, int oy, int ow, int oh)
	{
		if (x > ox + ow || y > oy + oh || x + w <= ox || y + h <= oy)
			return (false);
		return (true);
	}
}
