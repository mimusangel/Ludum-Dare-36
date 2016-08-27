package fr.ld32.map;

import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.maths.Vec2;

public class Quad
{
	public Vec2 pos;
	public Vec2 uv;
	public Color4f color;
	public Quad(Vec2 pos, Vec2 uv, Color4f color)
	{
		this.pos = pos;
		this.uv = uv;
		this.color = color;
	}
}
