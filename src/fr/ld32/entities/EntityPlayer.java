package fr.ld32.entities;

import fr.ld32.AABB;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityPlayer extends Entity {

	public EntityPlayer(Vec2 pos)
	{
		super(pos);
	}

	public void createEntity()
	{
		mesh = new Mesh(6);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE);
		mesh.addVertices(32, 64).addColor(Color4f.WHITE);
		mesh.addVertices(32, 64).addColor(Color4f.WHITE);
		mesh.addVertices(0, 64).addColor(Color4f.WHITE);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE);
		mesh.buffering();
	}

	public boolean entityAlive()
	{
		return true;
	}

	public void update(int tick, double elapse)
	{
		// Controler ici
		super.update(tick, elapse);
	}

	public Vec2 getOffset()
	{
		Vec2 offset = new Vec2(360 - 16, 202 - 32);
		offset.sub(pos);
		return (offset);
	}

	public AABB getBox() {
		return new AABB((int)pos.x, (int)pos.y, 32, 64);
	}
}
