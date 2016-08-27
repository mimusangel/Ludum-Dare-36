package fr.ld32;

import fr.mimus.jbasicgl.maths.Vec2;

public class AABB
{
	int x, y, w, h;
	public AABB(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Vec2 collided(AABB other)
	{
		return (collided(other.x, other.y, other.w, other.h));
	}
	
	public Vec2 collided(int ox, int oy, int ow, int oh)
	{
		Vec2 vec = new Vec2();
		if (x > ox + ow || y > oy + oh || x + w < ox || y + h < oy)
			return (vec);
		// Calculer la penetration pour le replacer
		if (y + h > oy) vec.y = oy - (y + h);
		return (vec);
	}
}
