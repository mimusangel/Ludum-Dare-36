package fr.ld36.entities;


import fr.ld36.AABB;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntitySarcophage extends Entity implements IActivable{
	
	public EntitySarcophage(Vec2 pos) {
		super(pos);
		
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(64, 0).addColor(Color4f.WHITE).addTexCoord2f(1f / 8f, 0);
		mesh.addVertices(64, 32).addColor(Color4f.WHITE).addTexCoord2f(1f / 8f, 1);
		mesh.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		mesh.buffering();
		
		this.texture = Res.images.get("tomb");
	}

	@Override
	public void action(Entity e) {
	}

	@Override
	public void createEntity() {
	}
	
	@Override
	public void giveDamage(Entity src, int dmg) {
	}

	@Override
	public void giveDamage(int x, int y, int dmg) {
	}

	@Override
	public AABB getBox() {
		return new AABB((int)pos.x, (int)pos.y, 64, 32);
	}

	@Override
	public Entity copy() {
		return new EntitySarcophage(pos.copy());
	}

	@Override
	public boolean spawnFront() {
		return false;
	}

}
