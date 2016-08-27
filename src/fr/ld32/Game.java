package fr.ld32;

import java.util.ArrayList;

import fr.ld32.entities.Entity;
import fr.ld32.entities.EntityPlayer;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Game
{
	Mat4 ortho;
	Shaders main;
	Map maptest;
	EntityPlayer player;
	ArrayList<Entity> entities;
	Vec2 offset;
	
	public Game()
	{
		ortho = Mat4.orthographic(0, 720 * 9 / 16, 720, 0, -1f, 1f);
		main = new Shaders("shaders/main.vert", "shaders/main.frag");
		maptest = new Map(this);
		maptest.createMap();
		entities = new ArrayList<Entity>();
		entities.add(player = new EntityPlayer(new Vec2(48, 80)));
		offset = new Vec2();
	}
	
	public void render(double elapse)
	{
		main.bind();
		main.setUniformMat4f("m_proj", ortho);
		maptest.render(elapse, main, offset);
		
		int i = 0;
		while (i < entities.size())
		{
			entities.get(i).render(main, offset);
			i++;
		}
	}
	
	public void update(int tick, double elapse)
	{
		maptest.update(tick, elapse);
		int i = 0;
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e.entityAlive())
			{
				e.update(tick, elapse);
				Vec2 collid = maptest.checkCollid(e);
				if (collid != null)
				{
					e.pos.add(collid);
					if (collid.y != 0)
					{
						e.gravity = 0;
						e.pos.y = (float) Math.floor(e.pos.y);
						e.inFloor = true;
					}
				}
				else
					e.inFloor = false;
				i++;
			}
			else
			{
				e.dispose();
				entities.remove(i);
			}
		}
		offset = player.getOffset();
	}
}
