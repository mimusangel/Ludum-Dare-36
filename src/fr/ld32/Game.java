package fr.ld32;

import java.util.ArrayList;

import fr.ld32.entities.Entity;
import fr.ld32.entities.EntityPlayer;
import fr.ld32.entities.IWalkable;
import fr.ld32.map.Map;
import fr.ld32.utils.Res;
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
		Res.autoLoadRsc();
		ortho = Mat4.orthographic(0, 720 * 9 / 16, 720, 0, -1f, 1f);
		main = new Shaders("shaders/main.vert", "shaders/main.frag");
		entities = new ArrayList<Entity>();
		entities.add(player = new EntityPlayer(new Vec2(48, 80)));
		maptest = new Map(this);
		maptest.createMap();
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
				e.update(this, tick, elapse);
				/*
				 *  MAP COLLID
				 */
				if (e instanceof EntityPlayer)
				{
					if (maptest.checkCollid(new AABB((int)last.x + 4, (int)e.pos.y + 16, 24, 48)))
					{
						e.pos.y = last.y;
						e.gravity = 0;
					}
					if (maptest.checkCollid(new AABB((int)e.pos.x + 4, (int)last.y + 16, 24, 48)))
					{
						e.pos.x = last.x;
					}
				}
				else
				{
					AABB aabb = e.getBox();
					if (maptest.checkCollid(new AABB((int)last.x, (int)e.pos.y, aabb.w, aabb.h)))
					{
						e.pos.y = last.y;
						e.gravity = 0;
					}
					if (maptest.checkCollid(new AABB((int)e.pos.x, (int)last.y, aabb.w, aabb.h)))
					{
						e.pos.x = last.x;
					}
				}
				e.inFloor = maptest.floorDetect(e.getBox());
				i++;
			}
			else
			{
				e.dispose();
				entities.remove(i);
			}
		}
		/*
		 *  Entity COLLID
		 */
		i = 0;
		while (i < entities.size())
		{
			Entity e0 = entities.get(i);
			int j = 0;
			while (j < entities.size())
			{
				if (i == j)
				{
					j++;
					continue;
				}
				Entity e1 = entities.get(j);
				AABB e0box = e0.getBox();
				AABB e1box = e1.getBox();
				Vec2 collid = e0box.collided(e1box);
				if (collid != null)
				{
					if (e0box.y + e0box.h <= e1box.y + e1box.h / 2 && e1 instanceof IWalkable)
					{
						if (e0 instanceof EntityPlayer)
							e0.pos.y = e1box.y - 64;
						else
							e0.pos.y = e1box.y - e0box.h;
						e0.gravity = 0;
						e0.inFloor = true;
					}
				}
				j++;
			}
			i++;
		}
		offset = player.getOffset();
	}

	public void addEntity(Entity entity)
	{
		entity.inFloor = false;
		entities.add(entity);
	}

	public Entity checkGrab(Entity entity) {
		int i = 0;
		AABB e0box = entity.getBox();
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity)
			{
				i++;
				continue;
			}
			AABB e1box = e.getBox();
			Vec2 collid = e0box.collided(e1box);
			if (collid != null)
				return (e);
			i++;
		}
		return (null);
	}
}
