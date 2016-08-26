package fr.mimus.jbasicgl.maths;

/**
 * @author Mimus
 * @version 1.0b
 */
public class Vec3
{
	public float x;
	public float y;
	public float z;
	
	/**
	 * Créé un vecteur à zero
	 */
	public Vec3()
	{
		this(0, 0, 0);
	}
	
	/**
	 * Créé un vecteur en defini les deux axes à la même valeur.
	 * @param v Valeur X, Y et Z
	 */
	public Vec3(float v)
	{
		this(v, v, v);
	}
	
	/**
	 * Copie les valeur d'un vecteur dans un nouveau vecteur.
	 * @param v Vecteur a copier
	 */
	public Vec3(Vec3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	/**
	 * On crée un vecteur a partir d'un vecteur 2f et d'une valeur
	 * @param v Vecteur2f
	 * @param z valeur axe Z
	 */
	public Vec3(Vec2 v, float z)
	{
		this(v.x, v.y, z);
	}
	
	/**
	 * Créer un vecteur en définisent chacun des axes.
	 * @param x Axe x
	 * @param y Axe y
	 * @param z Axe z
	 */
	public Vec3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 add(float v)
	{
		return (this.add(v, v, v));
	}
	
	public Vec3 add(Vec3 v)
	{
		return (this.add(v.x, v.y, v.z));
	}
	
	public Vec3 add(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		return (this);
	}
	
	public Vec3 sub(float v)
	{
		return (this.sub(v, v, v));
	}
	
	public Vec3 sub(Vec3 v)
	{
		return (this.sub(v.x, v.y, v.z));
	}
	
	public Vec3 sub(float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return (this);
	}
	
	public Vec3 div(float v)
	{
		return (this.div(v, v, v));
	}
	
	public Vec3 div(Vec3 v)
	{
		return (this.div(v.x, v.y, v.z));
	}
	
	public Vec3 div(float x, float y, float z)
	{
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return (this);
	}
	
	public Vec3 mul(float v)
	{
		return (this.mul(v, v, v));
	}
	
	public Vec3 mul(Vec3 v)
	{
		return (this.mul(v.x, v.y, v.z));
	}
	
	public Vec3 mul(float x, float y, float z)
	{
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return (this);
	}
	
	public Vec3 set(float v)
	{
		return (this.set(v, v, v));
	}
	
	public Vec3 set(Vec3 v)
	{
		return (this.set(v.x, v.y, v.z));
	}
	
	public Vec3 set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return (this);
	}
	
	public double lengthSqrd()
	{
		return (x * x + y * y + z * z);
	}
	
	public double length()
	{
		return (Math.sqrt(lengthSqrd()));
	}
	
	public Vec3 normalize()
	{
		double lenght = length();
		x = (float) (x / lenght);
		y = (float) (y / lenght);
		z = (float) (z / lenght);
		return (this); 
	}
	
	public Vec3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return (this); 
	}
	
	public float dot(Vec3 v1) {
		return (x * v1.x + y * v1.y + z * v1.z);
	}
	
	public static Vec3 cross(Vec3 v1, Vec3 v2)
	{
		Vec3 v = new Vec3();
		v.x = v1.y * v2.z - v1.z * v2.y;
		v.y = v1.z * v2.x - v1.x * v2.z;
		v.z = v1.x * v2.y - v1.y * v2.x;
		v.normalize();
		return (v); 
	}
	
	public static Vec3 cross(Vec3 v00, Vec3 v10, Vec3 v01)
	{
		Vec3 v1 = new Vec3(v01).sub(v00);
		Vec3 v2 = new Vec3(v10).sub(v00);
		return (Vec3.cross(v1, v2));
	}

	public Vec3 rotateX(int degree)
	{
		float tmp_y = y;
		float tmp_z = z;
		float rx = (float) Math.toRadians(degree);
		y = (float) (tmp_y * Math.cos(rx) - tmp_z * Math.sin(rx));
		z = (float) (tmp_y * Math.sin(rx) + tmp_z * Math.cos(rx));
		return (this);
	}
	
	public Vec3 rotateX(float rx)
	{
		float tmp_y = y;
		float tmp_z = z;
		
		y = (float) (tmp_y * Math.cos(rx) - tmp_z * Math.sin(rx));
		z = (float) (tmp_y * Math.sin(rx) + tmp_z * Math.cos(rx));
		return (this);
	}
	
	public Vec3 rotateY(int degree)
	{
		float tmp_x = x;
		float tmp_z = z;
		float ry = (float) Math.toRadians(degree);
		x = (float) (tmp_z * Math.sin(ry) + tmp_x * Math.cos(ry));
		z = (float) (tmp_z * Math.cos(ry) - tmp_x * Math.sin(ry));
		return (this);
	}
	
	public Vec3 rotateY(float ry)
	{
		float tmp_x = x;
		float tmp_z = z;
		
		x = (float) (tmp_z * Math.sin(ry) + tmp_x * Math.cos(ry));
		z = (float) (tmp_z * Math.cos(ry) - tmp_x * Math.sin(ry));
		return (this);
	}
	
	public Vec3 rotateZ(int degree)
	{
		float tmp_x = x;
		float tmp_y = y;
		float rz = (float) Math.toRadians(degree);
		x = (float) (tmp_x * Math.cos(rz) - tmp_y * Math.sin(rz));
		y = (float) (tmp_x * Math.sin(rz) + tmp_y * Math.cos(rz));
		return (this);
	}
	
	public Vec3 rotateZ(float rz)
	{
		float tmp_x = x;
		float tmp_y = y;
		
		x = (float) (tmp_x * Math.cos(rz) - tmp_y * Math.sin(rz));
		y = (float) (tmp_x * Math.sin(rz) + tmp_y * Math.cos(rz));
		return (this);
	}
	
	public Vec2 xy()
	{
		return (new Vec2(x, y));
	}
	
	public Vec2 xz()
	{
		return (new Vec2(x, z));
	}
	
	public Vec2 yz()
	{
		return (new Vec2(y, z));
	}
	
	public Vec3 copy()
	{
		return (new Vec3(this));
	}
	
	public float[] toArray()
	{
		return (new float[]{x, y, z});
	}

	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
