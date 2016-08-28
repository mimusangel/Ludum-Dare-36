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

public class EntityTrap extends Entity implements IBlock, IActivableLink {

	Animation anim;
	int unlock;
	int nbLock;
	boolean lastStat;
	public EntityTrap(Vec2 pos, int nbLock)
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
		anim = new Animation(14, 64, Res.images.get("trapdoor"), 0.05f);
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
	
	public void render(Shaders shader)
	{
		Vec2 v = pos.copy();
		if (anim != null)
			anim.render(shader, v);
		if (mesh != null && nbLock > 0)
		{
			EntityPlayer player = LD36.getInstance().game.player;
			Vec2 t = v.copy().sub(player.pos);
			double dist = t.lengthSqrd();
			if (dist < 100f * 100f)
			{
				Texture tex = Res.images.get("lock");
				float w = nbLock * 18f - 2f;
				v.add(32f, 2.5f).sub(w / 2f, 8f);
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

	public AABB getBox()
	{
		if (anim != null && anim.getFrame() == 13)
			return (null);
		return new AABB((int)pos.x, (int)pos.y, 64, 5);
	}

	public void giveDamage(Entity src, int dmg) {}
	public void giveDamage(int x, int y, int dmg){}
}
