package fr.ld32.entities;

import fr.ld32.AABB;
import fr.ld32.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityBox extends Entity implements IMovable, IWalkable
{
	Vec2 velocity;
	public EntityBox(Vec2 pos)
	{
		super(pos);
		velocity = new Vec2();
	}

	public void createEntity()
	{
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		mesh.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1f);
		mesh.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		mesh.buffering();
		texture = Res.images.get("box");
	}

	public boolean entityAlive()
	{
		return true;
	}

	public AABB getBox()
	{
		return new AABB((int)pos.x, (int)pos.y, 32, 32);
	}

	public void move(Vec2 vec)
	{
		pos.x = vec.x;
		pos.y = vec.y;
	}

}
