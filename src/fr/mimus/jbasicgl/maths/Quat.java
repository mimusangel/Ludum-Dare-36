package fr.mimus.jbasicgl.maths;

/**
 * @author Internet et Mimus
 * @version 1.0b
 * @source http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
 */
public class Quat
{
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quat()
	{
        this(0.0f, 0.0f, 0.0f, 1.0f, false);
	}

	public Quat(float x, float y, float z, float w, boolean normalize)
	{
		this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        if (normalize)
        	normalize();
	}
	
	public Quat(float angle, Vec3 axis)
	{
		this(angle, axis.x, axis.y, axis.z);
	}
	
	public Quat(float angle, float ax, float ay, float az)
	{
		float sin = (float) Math.sin(angle / 2);
		float cos = (float) Math.cos(angle / 2);

		this.x = ax * sin;
		this.y = ay * sin;
		this.z = az * sin;
		this.w = cos;
	}
	
	public Quat(Vec3 euler)
	{
		float c1 = (float) Math.cos(euler.x / 2);
		float s1 = (float) Math.sin(euler.x / 2);
		
		float c2 = (float) Math.cos(euler.y / 2);
		float s2 = (float) Math.sin(euler.y / 2);
		
		float c3 = (float) Math.cos(euler.z / 2);
		float s3 = (float) Math.sin(euler.z / 2);
		
		float c1c2 = c1 * c2;
		float s1s2 = s1 * s2;

		x = c1c2 * s3 + s1s2 * c3;
		y = s1 * c2 * c3 + c1 * s2 * s3;
		z = c1 * s2 * c3 - s1 * c2 * s3;
		w = c1c2 * c3 - s1s2 * s3;
	}
	
	public Quat(Mat4 m)
	{
		float trace = m.elements[0 + 0 * 4] + m.elements[1 + 1 * 4] + m.elements[2 + 2 * 4];
		if (trace > 0) {
			float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
			w = 0.25f / s;
			x = (m.elements[1 + 2 * 4] - m.elements[2 + 1 * 4]) * s;
			y = (m.elements[2 + 0 * 4] - m.elements[0 + 2 * 4]) * s;
			z = (m.elements[0 + 1 * 4] - m.elements[1 + 0 * 4]) * s;
		} else {
			if (m.elements[0 + 0 * 4] > m.elements[1 + 1 * 4] && m.elements[0 + 0 * 4] > m.elements[2 + 2 * 4]) {
				float s = 2.0f * (float) Math.sqrt(1.0f + m.elements[0 + 0 * 4] - m.elements[1 + 1 * 4] - m.elements[2 + 2 * 4]);
				w = (m.elements[1 + 2 * 4] - m.elements[2 + 1 * 4]) / s;
				x = 0.25f * s;
				y = (m.elements[1 + 0 * 4] + m.elements[0 + 1 * 4]) / s;
				z = (m.elements[2 + 0 * 4] + m.elements[0 + 2 * 4]) / s;
			} else if (m.elements[1 + 1 * 4] > m.elements[2 + 2 * 4]) {
				float s = 2.0f * (float) Math.sqrt(1.0f + m.elements[1 + 1 * 4] - m.elements[0 + 0 * 4] - m.elements[2 + 2 * 4]);
				w = (m.elements[2 + 0 * 4] - m.elements[0 + 2 * 4]) / s;
				x = (m.elements[1 + 0 * 4] + m.elements[0 + 2 * 4]) / s;
				y = 0.25f * s;
				z = (m.elements[2 + 1 * 4] + m.elements[1 + 2 * 4]) / s;
			} else {
				float s = 2.0f * (float) Math.sqrt(1.0f + m.elements[2 + 2 * 4] - m.elements[0 + 0 * 4] - m.elements[1 + 1 * 4]);
				w = (m.elements[0 + 1 * 4] - m.elements[1 + 0 * 4]) / s;
				x = (m.elements[2 + 0 * 4] + m.elements[0 + 2 * 4]) / s;
				y = (m.elements[1 + 2 * 4] + m.elements[2 + 1 * 4]) / s;
				z = 0.25f * s;
			}
		}
		normalize();
	}
	
    public Quat conjugate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return (this);
    }
    
    public Quat negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return (this);
    }

    
    public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}
    
    public Quat normalize()
    {
        float norm = (float) magnitude();
        if (norm > 0.0f)
        {
            this.x /= norm;
            this.y /= norm;
            this.z /= norm;
            this.w /= norm;
        }
        else
        {
			this.x = 0.0f;
			this.y = 0.0f;
			this.z = 0.0f;
			this.w = 1.0f;
        }
        return (this);
    }
    
    public Quat mul(float value) {
		return new Quat(x * value, y * value, z * value, w * value);
	}
	
	public Quat mul(Quat q) {
		float nw = w * q.w - x * q.x - y * q.y - z * q.z;
		float nx = x * q.w + w * q.x + y * q.z - z * q.y;
		float ny = y * q.w + w * q.y + z * q.x - x * q.z;
		float nz = z * q.w + w * q.z + x * q.y - y * q.x;

		return new Quat(nx, ny, nz, nw);
	}

	public Quat mul(Vec3 v) {
		float nw = -x * v.x - y * v.y - z * v.z;
		float nx = 	w * v.x + y * v.z - z * v.y;
		float ny = 	w * v.y + z * v.x - x * v.z;
		float nz = 	w * v.z + x * v.y - y * v.x;

		return new Quat(nx, ny, nz, nw);
	}

    public Mat4 toMatrix()
    {
    	Mat4 m = Mat4.identity();
    	float xx = x * x;
    	float xy = x * y;
    	float xz = x * z;
    	float xw = x * w;
    	float yy = y * y;
    	float yz = y * z;
    	float yw = y * w;
    	float zz = z * z;
    	float zw = z * w;

    	m.elements[0 + 0 * 4] = 1 - 2 * ( yy + zz );
    	m.elements[1 + 0 * 4] =     2 * ( xy - zw );
    	m.elements[2 + 0 * 4] =     2 * ( xz + yw );
    	
    	m.elements[0 + 1 * 4] =     2 * ( xy + zw );
    	m.elements[1 + 1 * 4] = 1 - 2 * ( xx + zz );
    	m.elements[2 + 1 * 4] =     2 * ( yz - xw );
    	
    	m.elements[0 + 2 * 4] =     2 * ( xz - yw );
    	m.elements[1 + 2 * 4] =     2 * ( yz + xw );
    	m.elements[2 + 2 * 4] = 1 - 2 * ( xx + yy );

    	return (m);
    }
}
