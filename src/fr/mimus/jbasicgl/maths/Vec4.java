package fr.mimus.jbasicgl.maths;

/**
 * @author Mimus
 * @version 1.0b
 */
public class Vec4
{
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vec4()
	{
		this(0, 0, 0, 0);
	}
	
	public Vec4(float v)
	{
		this(v, v, v, v);
	}
	
	public Vec4(Vec4 v)
	{
		this(v.x, v.y, v.z, v.w);
	}
	
	public Vec4(Vec2 v, float z, float w)
	{
		this(v.x, v.y, z, w);
	}
	
	public Vec4(Vec3 v, float w)
	{
		this(v.x, v.y, v.z, w);
	}
	
	public Vec4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vec3 xyz()
	{
		return (new Vec3(x, y, z));
	}
	
	public Vec4 copy()
	{
		return (new Vec4(this));
	}
	
	public float[] toArray()
	{
		return (new float[]{x, y, z, w});
	}
}
