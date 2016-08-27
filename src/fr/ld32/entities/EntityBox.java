package fr.ld32.entities;

import fr.ld32.AABB;
import fr.ld32.Game;
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

	public void update(Game game, int tick, double elapse)
	{
		pos.add(velocity);
		velocity.mul(0.75f);
		if (Math.abs(velocity.x) < 0.1f)
			velocity.x = 0;
		if (Math.abs(velocity.y) < 0.1f)
			velocity.y = 0;
		super.update(game, tick, elapse);
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
		velocity.x = vec.x;
		velocity.y = vec.y;
	}

}
