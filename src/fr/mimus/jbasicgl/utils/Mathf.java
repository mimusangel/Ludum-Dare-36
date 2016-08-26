package fr.mimus.jbasicgl.utils;

public class Mathf
{
	public static float rand(float min, float max)
	{
		float v = max - min;
		return (min + (float)(Math.random() * v));
	}
	
	public static float toRadians(double angdeg)
	{
		return (float) (Math.toRadians(angdeg));
	}
	
	public static float sin(double a)
	{
		return ((float) Math.sin(a));
	}
	
	public static float cos(double a)
	{
		return ((float) Math.cos(a));
	}
}
