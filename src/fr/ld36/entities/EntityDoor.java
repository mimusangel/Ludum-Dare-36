package fr.ld36.entities;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.spe.IActivableLink;
import fr.ld36.entities.spe.IBlock;
import fr.ld36.render.Animation;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntityDoor extends Entity implements IBlock, IActivableLink {

	Animation anim;
	int unlock;
	int nbLock;
	boolean lastStat;
	public EntityDoor(Vec2 pos, int nbLock)
	{
		super(pos);
		this.nbLock = nbLock;
		unlock = 0;
		lastStat = false;
	}

	public void toggle(Entity e, boolean toggle)
	{
		if (toggle)
			unlock++;
		else
			unlock--;
		boolean islock = (unlock == nbLock);
		if (lastStat != islock)
		{
			lastStat = islock;
			anim.setReverse(!islock);
			anim.setPause(false);
		}
	}

	public boolean isReady()
	{
		if (anim != null)
			return anim.isEnded();
		return (false);
	}

	public void createEntity()
	{
		anim = new Animation(24, 32, 64, Res.images.get("vDoor"), 0.05f);
		anim.setPause(true);
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(16, 0).addColor(Color4f.WHITE).addTexCoord2f(0.5f, 0);
		mesh.addVertices(16, 16).addColor(Color4f.WHITE).addTexCoord2f(0.5f, 1f);
		mesh.addVertices(0, 16).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		mesh.buffering();
	}
	
	public void update(Game game, int tick, double elapse)
	{
		if (anim != null)
		{
			anim.update();
		}
		else
			createEntity();
	}
	
	public void render(Shaders shader, Vec2 offset)
	{
		Vec2 v = pos.copy().add(offset);
		if (anim != null)
			anim.render(shader, v);
		if (mesh != null && nbLock > 0)
		{
			float winW = (LD36.WINDOW_WIDTH * 9f / 16f) /2f;
			Vec2 t = v.copy().sub(new Vec2(LD36.WINDOW_WIDTH / 2f, winW));
			double dist = t.length();
			if (dist < 100f)
			{
				Texture tex = Res.images.get("lock");
				float w = nbLock * 18f - 2f;
				v.add(16f, 32f).sub(w / 2f, 8f);
				tex.bind();
				for (int i = 0; i < nbLock; i++)
				{
					shader.setUniformMat4f("m_view", Mat4.translate(v.add(i * 18f, 0)));
					if (i < unlock)
						shader.setUniform2f("anim", new Vec2(0.5f, 0));
					else
						shader.setUniform2f("anim", new Vec2(0, 0));
					mesh.render(GL11.GL_QUADS);
				}
			}
		}
	}

	public boolean entityAlive()
	{
		return true;
	}

	public AABB getBox()
	{
		if (anim != null)
			return new AABB((int)pos.x + 12, (int)pos.y, 8, 64 - (anim.getFrame() * 59 / 24));
		return new AABB((int)pos.x + 12, (int)pos.y, 8, 64);
	}

}
