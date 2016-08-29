package fr.ld36.entities;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public abstract class Entity
{
	public Vec2 pos;
	protected Mesh mesh;
	protected Texture texture;
	public Vec2 velocity;
	public boolean inFloor = false;
	int life;
	long lifeTime;
	
	public Entity(Vec2 pos)
	{
		velocity = new Vec2();
		this.pos = pos;
		life = 1;
		lifeTime = System.currentTimeMillis();
	}

	public abstract void createEntity();
	public abstract void giveDamage(Entity src, int dmg);
	public abstract void giveDamage(int x, int y, int dmg);
	public abstract AABB getBox();
	public abstract Entity copy();
	public boolean entityAlive()
	{
		return (life > 0);
	}
	
	public void render(Shaders shader)
	{
		if (texture != null)
			texture.bind();
		if (mesh != null)
		{
			Vec2 matPos = pos.copy();
			matPos.x = (float) Math.floor(matPos.x);
			matPos.y = (float) Math.floor(matPos.y);
			shader.setUniformMat4f("m_view", Mat4.translate(matPos));
			shader.setUniform2f("anim", new Vec2());
			mesh.render(GL11.GL_QUADS);
		}
		else
		{
			shader.setUniformMat4f("m_view", Mat4.identity());
			shader.setUniform2f("anim", new Vec2());
			createEntity();
		}
		Texture.unbind();
	}
	
	public void update(Game game, int tick, double elapse)
	{
		if (velocity.y == 0)
			velocity.y = 0.9f;
		if (velocity.y > 0)
			velocity.y *= 1.15f;
		if (velocity.y < 0)
			velocity.y *= 0.85f;
		if (velocity.y > 16)
			velocity.y = 16;
		if (velocity.y < 0.1f && velocity.y > -0.1f)
			velocity.y = 0;
		pos.add(velocity);
		velocity.x *= 0.75;
		if (Math.abs(velocity.x) < 0.5)
			velocity.x = 0;
	}
		
	public void dispose()
	{
		if (mesh != null)
			mesh.dispose();
	}
	
	public boolean spawnFront()
	{
		return (true);
	}
	
	public void setLife(int life)
	{
		this.life = life;
	}
	
	public int getLife()
	{
		return (life);
	}
	
	public int distance(Entity e){
		return (int) e.pos.copy().sub(this.pos).length();
	}
	public int distance(Entity e, Vec2 offSet){
		return (int) e.pos.copy().sub(this.pos).length();
	}
	public int distance(Entity e, Vec2 offSet, Vec2 offSetpam){
		return (int) e.pos.copy().add(offSetpam).sub(this.pos.copy().add(offSet)).length();
	}
	
	public AABB getSelectBox()
	{
		return (new AABB((int)pos.x, (int)pos.y, 32, 32));
	}
}
