package fr.ld36.entities;

import fr.ld36.AABB;
import fr.ld36.entities.spe.IMovable;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityBones extends Entity implements IMovable
{
	public EntityBones(Vec2 pos)
	{
		super(pos);
	}

	public void createEntity()
	{
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		mesh.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1f);
		mesh.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		mesh.buffering();
		texture = Res.images.get("bones");
	}

	public AABB getBox()
	{
		return new AABB((int)pos.x, (int)pos.y, 32, 32);
	}

	public void move(Vec2 vec)
	{
		pos.x = vec.x;
		pos.y = vec.y - 16;
		velocity.x = 0;
		velocity.y = 0;
	}

	public void giveDamage(Entity src, int dmg) {}
	public void giveDamage(int x, int y, int dmg){}

	public Entity copy()
	{
		return new EntityBones(pos.copy());
	}
}
