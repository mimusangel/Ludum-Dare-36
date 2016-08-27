package fr.ld32;

import java.util.ArrayList;

import fr.ld32.entities.Entity;
import fr.ld32.entities.EntityPlayer;
import fr.ld32.map.Map;
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
				Vec2 last = e.pos.copy();
				e.update(tick, elapse);
				if (maptest.checkCollid(e.getBox()))
				{
					AABB aabbx = e.getBox();
					AABB aabby = e.getBox();
					aabbx.x = (int) last.x;
					if (!maptest.checkCollid(aabbx))
						e.pos.x = last.x;
					aabby.y = (int) last.y;
					if (!maptest.checkCollid(aabby))
					{
						e.pos.y = last.y;
					}
					if (maptest.checkCollid(e.getBox()))
					{
						e.pos.x = last.x;
						e.pos.y = last.y;
					}
				}
				e.inFloor = maptest.floorDetect(e.getBox());
				if (e.inFloor)
					e.gravity = 0;
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
