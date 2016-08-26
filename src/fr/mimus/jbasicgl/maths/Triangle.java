package fr.mimus.jbasicgl.maths;

import fr.mimus.jbasicgl.graphics.Color4f;

public class Triangle
{
	Vec3 	A;
	Vec3 	B;
	Vec3 	C;
	Color4f		color;
	Vec3	normal;
	Vec2	uvA;
	Vec2	uvB;
	Vec2	uvC;
	
	public Triangle(Vec3 a, Vec3 b, Vec3 c, Color4f color)
	{
		A = a;
		B = b;
		C = c;
		this.color = color;
		normal = Vec3.cross(A, B, C);
		Vec3 pA = A.copy().normalize();
		Vec3 pB = B.copy().normalize();
		Vec3 pC = C.copy().normalize();
		uvA = new Vec2((float)(Math.atan2(pA.x, pA.z) / (Math.PI * 2.0)) + 0.5f,
				(float)(Math.asin(pA.y) / Math.PI) + 0.5f);
		uvB = new Vec2((float)(Math.atan2(pB.x, pB.z) / (Math.PI * 2.0)) + 0.5f,
				(float)(Math.asin(pB.y) / Math.PI) + 0.5f);
		uvC = new Vec2((float)(Math.atan2(pC.x, pC.z) / (Math.PI * 2.0)) + 0.5f,
				(float)(Math.asin(pC.y) / Math.PI) + 0.5f);
	}

	public Vec3 getA() {
		return A;
	}

	public void setA(Vec3 a) {
		A = a;
	}

	public Vec3 getB() {
		return B;
	}

	public void setB(Vec3 b) {
		B = b;
	}

	public Vec3 getC() {
		return C;
	}

	public void setC(Vec3 c) {
		C = c;
	}

	public Color4f getColor() {
		return color;
	}

	public void setColor(Color4f color) {
		this.color = color;
	}

	public Vec3 getNormal() {
		return normal;
	}

	public void setNormal(Vec3 normal) {
		this.normal = normal;
	}

	public Vec2 getUvA() {
		return uvA;
	}

	public void setUvA(Vec2 uvA) {
		this.uvA = uvA;
	}

	public Vec2 getUvB() {
		return uvB;
	}

	public void setUvB(Vec2 uvB) {
		this.uvB = uvB;
	}

	public Vec2 getUvC() {
		return uvC;
	}

	public void setUvC(Vec2 uvC) {
		this.uvC = uvC;
	}

}
