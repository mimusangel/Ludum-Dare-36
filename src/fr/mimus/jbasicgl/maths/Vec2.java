package fr.mimus.jbasicgl.maths;

/**
 * @author Mimus
 * @version 1.0b
 */
public class Vec2
{
	public float x;
	public float y;
	
	/**
	 * Créé un vecteur à zero
	 */
	public Vec2()
	{
		this(0, 0);
	}
	
	/**
	 * Créé un vecteur en defini les deux axes à la même valeur.
	 * @param v Valeur X et Y
	 */
	public Vec2(float v)
	{
		this(v, v);
	}
	
	/**
	 * Copie les valeur d'un vecteur dans un nouveau vecteur.
	 * @param v Vecteur a copier
	 */
	public Vec2(Vec2 v)
	{
		this(v.x, v.y);
	}
	
	/**
	 * Créer un vecteur en définisent chacun des axes.
	 * @param x Axe X
	 * @param y Axe Y
	 */
	public Vec2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2 add(float v)
	{
		return (this.add(v, v));
	}
	
	public Vec2 add(Vec2 v)
	{
		return (this.add(v.x, v.y));
	}
	
	public Vec2 add(float x, float y)
	{
		this.x += x;
		this.y += y;
		return (this);
	}
	
	public Vec2 sub(float v)
	{
		return (this.sub(v, v));
	}
	
	public Vec2 sub(Vec2 v)
	{
		return (this.sub(v.x, v.y));
	}
	
	public Vec2 sub(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		return (this);
	}
	
	public Vec2 div(float v)
	{
		return (this.div(v, v));
	}
	
	public Vec2 div(Vec2 v)
	{
		return (this.div(v.x, v.y));
	}
	
	public Vec2 div(float x, float y)
	{
		this.x /= x;
		this.y /= y;
		return (this);
	}
	
	public Vec2 mul(float v)
	{
		return (this.mul(v, v));
	}
	
	public Vec2 mul(Vec2 v)
	{
		return (this.mul(v.x, v.y));
	}
	
	public Vec2 mul(float x, float y)
	{
		this.x *= x;
		this.y *= y;
		return (this);
	}
	
	public Vec2 set(float v)
	{
		return (this.set(v, v));
	}
	
	public Vec2 set(Vec3 v)
	{
		return (this.set(v.x, v.y));
	}
	
	public Vec2 set(float x, float y)
	{
		this.x = x;
		this.y = y;
		return (this);
	}
	
	public double lengthSqrd()
	{
		return (x * x + y * y);
	}
	
	public double length()
	{
		return (Math.sqrt(lengthSqrd()));
	}
	
	public Vec2 normalise()
	{
		double lenght = length();
		x = (float) (x / lenght);
		y = (float) (y / lenght);
		return this;
	}
	
	public Vec2 negate() {
		x = -x;
		y = -y;
		return this; 
	}
	
	public float dot(Vec2 v)
	{
		return (x * v.x + y * v.y);
	}
	
	public Vec2 cross()
	{
		Vec2 v = new Vec2(y, -x);
		return (v.normalise());
	}
	
	public float crossProduct(Vec2 v)
	{
		return (x * v.y - y * v.x);
	}
	
	public Vec2 copy()
	{
		return (new Vec2(this));
	}
	
	public float[] toArray()
	{
		return (new float[]{x, y});
	}
}
