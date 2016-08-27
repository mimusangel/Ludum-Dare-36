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
	public float gravity = 0;
	public boolean inFloor = false;
	
	public Entity(Vec2 pos)
	{
		this.pos = pos;
	}
	
	public abstract void createEntity();
	public abstract boolean entityAlive();
	public abstract AABB getBox();
	
	public void render(Shaders shader, Vec2 offset)
	{
		if (texture != null)
			texture.bind();
		if (mesh != null)
		{
			Vec2 matPos = pos.copy().add(offset);
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
		if (gravity == 0)
			gravity = 0.9f;
		if (gravity > 0)
			gravity *= 1.15f;
		if (gravity < 0)
			gravity *= 0.85f;
		if (gravity > 16)
			gravity = 16;
		if (gravity < 0.1f && gravity > -0.1f)
			gravity = 0;
		pos.y += gravity;
	}
		
	public void dispose()
	{
		if (mesh != null)
			mesh.dispose();
		if (texture != null)
			texture.dispose();
	}
	
	public boolean spawnFront()
	{
		return (true);
	}
}
