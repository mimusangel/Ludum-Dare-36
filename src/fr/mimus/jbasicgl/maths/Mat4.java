package fr.mimus.jbasicgl.maths;

import java.nio.FloatBuffer;

import fr.mimus.jbasicgl.utils.DataBuffer;

/**
 * @author Mimus
 * @version 1.0b
 */
public class Mat4
{
	public static final int SIZE = 4 * 4;
	public float[] elements;
	
	private Mat4()
	{
		elements = new float[SIZE];
		for (int i = 0; i < SIZE; i++)
			elements[i] = 0.0f;
	}
	
	/**
	 * Fait une copie de la matrix
	 * @return retourne la matrix copier
	 */
	public Mat4 copy()
	{
		Mat4 m = new Mat4();
		
		for (int i = 0; i < SIZE; i++)
			m.elements[i] = elements[i];
		return (m);
	}
	
	/**
	 * Créer une matrix d'identiter
	 * @return retourne la matrix
	 */
	public static Mat4 identity()
	{
		Mat4 m = new Mat4();
		
		for (int i = 0; i < 4; i++)
			m.elements[i + i * 4] = 1.0f;
		return (m);
	}
	
	public static Vec4 transform(Mat4 m0, Vec4 v0)
	{
		Vec4 v = new Vec4();
		v.x = m0.elements[0 + 0 * 4] * v0.x + m0.elements[0 + 1 * 4] * v0.y + m0.elements[0 + 2 * 4] * v0.z + m0.elements[0 + 3 * 4] * v0.w;
		v.y = m0.elements[1 + 0 * 4] * v0.x + m0.elements[1 + 1 * 4] * v0.y + m0.elements[1 + 2 * 4] * v0.z + m0.elements[1 + 3 * 4] * v0.w;
		v.z = m0.elements[2 + 0 * 4] * v0.x + m0.elements[2 + 1 * 4] * v0.y + m0.elements[2 + 2 * 4] * v0.z + m0.elements[2 + 3 * 4] * v0.w;
		v.w = v0.w;
		return (v);
	}
	
	public Vec3 getRotation()
	{
		float yaw = 0.0f;
		float pitch = 0.0f;
		float roll = 0.0f;
		if(elements[0 + 0 * 4] == 1.0f || elements[0 + 0 * 4] == -1.0f)
		{
			yaw = (float)Math.atan2(elements[2 + 0 * 4], elements[3 + 2 * 4]);
		}
		else
		{
			yaw = (float) Math.atan2(-elements[0 + 2 * 4], elements[0 + 0 * 4]);
			pitch = (float) Math.asin(elements[0 + 1 * 4]);
			roll = (float) Math.atan2(-elements[2 + 1 * 4], elements[1 + 1 * 4]);
		}
		return (new Vec3(roll, yaw, pitch));
	}
	
	public Vec3 getScale()
	{
		return (new Vec3(elements[0 + 0 * 4], elements[1 + 1 * 4], elements[2 + 2 * 4]));
	}
	
	public Vec3 getPosition()
	{
		return (new Vec3(elements[0 + 3 * 4], elements[1 + 3 * 4], elements[2 + 3 * 4]));
	}
	
	/**
	 * Multiplie deux matrix entre elle.
	 * @param m0 Premier matrix a multiplier avec la second
	 * @param m1 Second matrix multiplier avec la premier
	 * @return Retourne une nouvelle matrix resultant du total de la multiplification des deux matrix.
	 */
	public static Mat4 multiply(Mat4 m0, Mat4 m1)
	{
		Mat4 m = new Mat4();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				float r = 0.0f;
				for (int e = 0; e < 4; e++)
				{
					r += m0.elements[j + e * 4] * m1.elements[e + i * 4];
				}
				m.elements[j + i * 4] = r;
			}
		}
		return (m);
	}
	
	/**
	 * Créer une matrix de déplacement par rapport a deux Axe
	 * @param vec Vecteur de deplacement
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 translate(Vec2 vec)
	{
		return (translate(vec.x, vec.y));
	}
	/**
	 * Créer une matrix de déplacement par rapport a deux Axe
	 * @param x Axe X
	 * @param y Axe Y
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 translate(float x, float y)
	{
		Mat4 m = identity();
		
		m.elements[0 + 3 * 4] = x;
		m.elements[1 + 3 * 4] = y;
		return (m);
	}
	
	/**
	 * Créer une matrix de déplacement par rapport a trois Axe
	 * @param vec Vecteur de deplacement
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 translate(Vec3 vec)
	{
		return (translate(vec.x, vec.y, vec.z));
	}
		
	/**
	 * Créer une matrix de déplacement par rapport a trois Axe
	 * @param x Axe X
	 * @param y Axe Y
	 * @param z Axe Z
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 translate(float x, float y, float z)
	{
		Mat4 m = identity();
		
		m.elements[0 + 3 * 4] = x;
		m.elements[1 + 3 * 4] = y;
		m.elements[2 + 3 * 4] = z;
		return (m);
	}
	
	/**
	 * Créer une matrix d'une echelles desirer
	 * @param v Valeur de l'echelle
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 scale(float v)
	{
		return (scale(v, v, v));
	}
	
	/**
	 * Créer une matrix d'une echelles desirer
	 * @param vec Vecteur des echelles selon les axes
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 scale(Vec3 vec)
	{
		return (scale(vec.x, vec.y, vec.z));
	}
	
	/**
	 * Créer une matrix d'une echelles desirer
	 * @param x Valeur de l'echelle de l'axe X
	 * @param y Valeur de l'echelle de l'axe Y
	 * @param z Valeur de l'echelle de l'axe Z
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 scale(float x, float y, float z)
	{
		Mat4 m = identity();
		
		m.elements[0 + 0 * 4] = x;
		m.elements[1 + 1 * 4] = y;
		m.elements[2 + 2 * 4] = z;
		return (m);
	}
	
	/**
	 * Créer une matrix de rotation sur l'axe X
	 * @param angle Angle en degree
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateX(float angle)
	{
		return (rotateXf((float) Math.toRadians(angle)));
	}
	
	/**
	 * Créer une matrix de rotation sur l'axe X
	 * @param rad Angle en Radian
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateXf(float rad)
	{
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		Mat4 m = identity();
		
		m.elements[1 + 1 * 4] = cos;
		m.elements[2 + 1 * 4] = -sin;
		m.elements[1 + 2 * 4] = sin;
		m.elements[2 + 2 * 4] = cos;
		return (m);
	}
	
	/**
	 * Créer une matrix de rotation sur l'axe Y
	 * @param angle Angle en degree
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateY(float angle)
	{
		return (rotateYf((float) Math.toRadians(angle)));
	}
	
	/**
	 * Créer une matrix de rotation sur l'axe Y
	 * @param rad Angle en Radian
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateYf(float rad)
	{
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		Mat4 m = identity();
		
		m.elements[0 + 0 * 4] = cos;
		m.elements[2 + 0 * 4] = sin;
		m.elements[0 + 2 * 4] = -sin;
		m.elements[2 + 2 * 4] = cos;
		return (m);
	}

	/**
	 * Créer une matrix de rotation sur l'axe Z
	 * @param angle Angle en degree
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateZ(float angle)
	{
		return (rotateZf((float) Math.toRadians(angle)));
	}
	
	/**
	 * Créer une matrix de rotation sur l'axe Z
	 * @param rad Angle en Radian
	 * @return Retourne la matrix resultante
	 */
	public static Mat4 rotateZf(float rad)
	{
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		Mat4 m = identity();
		
		m.elements[0 + 0 * 4] = cos;
		m.elements[1 + 0 * 4] = -sin;
		m.elements[0 + 1 * 4] = sin;
		m.elements[1 + 1 * 4] = cos;
		return (m);
	}
	
	public static Mat4 rotate(float x, float y)
	{
		return (multiply(rotateY(y), rotateX(x)));
	}
	
	public static Mat4 rotate(float x, float y, float z)
	{
		return (multiply(rotateX(x), multiply(rotateY(y), rotateZ(z))));
	}
	
	public static Mat4 rotatef(float x, float y)
	{
		return (multiply(rotateYf(y), rotateXf(x)));
	}
	
	public static Mat4 rotatef(float x, float y, float z)
	{
		return (multiply(rotateXf(x), multiply(rotateYf(y), rotateZf(z))));
	}
	
	public static Mat4 euler(float x, float y, float z)
	{
		return (eulerf((float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z)));
	}
	
	public static Mat4 eulerf(float x, float y, float z)
	{
		Mat4 m = Mat4.identity();
		
		float xcos = (float)Math.cos(x);
		float xsin = (float)Math.sin(x);
		float ycos = (float)Math.cos(y);
		float ysin = (float)Math.sin(y);
		float zcos = (float)Math.cos(z);
		float zsin = (float)Math.sin(z);
		float xcys = xcos * ysin;
		float xsys = xsin * ysin;

		m.elements[0 + 0 * 4] = ycos * zcos;
		m.elements[1 + 0 * 4] = -ycos * zsin;
		m.elements[2 + 0 * 4] = ysin;
		
		m.elements[0 + 1 * 4] = xsys * zcos + xcos * zsin;
		m.elements[1 + 1 * 4] = -xsys * zsin + xcos * zcos;
		m.elements[2 + 1 * 4] = -xsin * ycos;

		m.elements[0 + 2 * 4] = -xcys * zcos + xsin * zsin;
		m.elements[1 + 2 * 4] = xcys * zsin + xsin * zcos;
		m.elements[2 + 2 * 4] = xcos * ycos;
		
		return (m);
	}
	
	public static Mat4 axis(Vec3 forward, Vec3 up)
	{
		Mat4 m = Mat4.identity();
		Vec3 r = Vec3.cross(up, forward);
		Vec3 u = Vec3.cross(forward, r);
		
		m.elements[0 + 0 * 4] = r.x;
		m.elements[1 + 0 * 4] = r.y;
		m.elements[2 + 0 * 4] = r.z;
		
		m.elements[0 + 1 * 4] = u.x;
		m.elements[1 + 1 * 4] = u.y;
		m.elements[2 + 1 * 4] = u.z;

		m.elements[0 + 2 * 4] = forward.x;
		m.elements[1 + 2 * 4] = forward.y;
		m.elements[2 + 2 * 4] = forward.z;
		return (m);
	}
	
	public static Mat4 orthographic(float left, float bottom, float right, float top, float near, float far)
	{
		Mat4 m = identity();
		
		m.elements[0 + 0 * 4] = 2.0f / (right - left);
		m.elements[0 + 3 * 4] = (left - right) / (right - left);
		m.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		m.elements[1 + 3 * 4] = -(top + bottom) / (top - bottom);
		m.elements[2 + 2 * 4] = 2.0f / (far - near);
		m.elements[2 + 3 * 4] = (near - far) / (far - near);
		return (m);
	}
	
	public static Mat4 perspective(float fov, float aspect, float zNear, float zFar)
	{
		Mat4 m = new Mat4();
		float zRange = zNear - zFar;
		float tanFov = (float) Math.tan(Math.toRadians(fov / 2.0));

		m.elements[0 + 0 * 4] = 1.0f / (tanFov * aspect);
		m.elements[1 + 1 * 4] = 1.0f / tanFov;
		m.elements[2 + 2 * 4] = (-zNear - zFar) / zRange;
		m.elements[2 + 3 * 4] = (2.0f * zNear * zFar) / zRange;
		m.elements[3 + 2 * 4] = 1.0f;
		return (m);
	}
	
	private static FloatBuffer buffer = null;
	public FloatBuffer toBuffer()
	{
		if (buffer == null)
			buffer = DataBuffer.allocateFloatBuffer(this.elements.length);
		buffer.put(this.elements);
		buffer.flip();
		return (buffer);
	}
	
	public void print()
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(elements[j + i * 4] + " | ");
			}
			System.out.println();
		}
	}
}
