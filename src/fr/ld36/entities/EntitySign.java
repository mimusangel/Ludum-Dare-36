package fr.ld36.entities;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.spe.IBlock;
import fr.ld36.render.Renderer;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class EntitySign extends Entity implements IBlock
{
	String text;
	public EntitySign(Vec2 pos, String msg)
	{
		super(pos);
		text = msg;
	}

	public void createEntity()
	{
		texture = Res.images.get("sign");
		mesh = new Mesh(4);
		mesh.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		mesh.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		mesh.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1f);
		mesh.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		mesh.buffering();
	}
	
	public void update(Game game, int tick, double elapse)
	{}
	
	public void render(Shaders shader)
	{
		if (mesh == null)
		{
			createEntity();
			return;
		}
		Vec2 v = pos.copy();
		texture.bind();
		shader.setUniformMat4f("m_view", Mat4.translate(v));
		shader.setUniform2f("anim", new Vec2(0, 0));
		mesh.render(GL11.GL_QUADS);
		if (mesh != null)
		{
			EntityPlayer player = LD36.getInstance().game.player;
			Vec2 t = v.copy().sub(player.pos);
			double dist = t.length();
			if (dist < 100f)
			{
				Game game = LD36.getInstance().game;
				Vec2 offset = game.player.getOffset();
				float px = pos.x + offset.x + 16;
				float py = pos.y + offset.y;
				game.initRenderHUD();
				Vec2 size = Renderer.metricString(text);
				Vec2 p = new Vec2(px - size.x / 2, py - size.y);
				p.x = (float) Math.floor(p.x);
				p.y = (float) Math.floor(p.y);
				Renderer.drawString(game.hud, text, p, Color4f.WHITE);
				game.initRender();
			}
		}
	}

	public void giveDamage(Entity src, int dmg) {}

	public void giveDamage(int x, int y, int dmg) {}

	public AABB getBox()
	{
		return null;
	}

	public boolean spawnFront()
	{
		return (false);
	}
}
