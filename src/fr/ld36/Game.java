package fr.ld36;

import java.util.ArrayList;

import fr.ld36.entities.*;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.entities.spe.IBlock;
import fr.ld36.entities.spe.IMovable;
import fr.ld36.entities.spe.IWalkable;
import fr.ld36.map.Map;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Game
{
	Mat4 ortho;
	Shaders main;
	Map map;
	public EntityPlayer player;
	ArrayList<Entity> entities;
	Vec2 offset;
	
	public Game()
	{
		Res.autoLoadRsc();
		ortho = Mat4.orthographic(0, 720 * 9 / 16, 720, 0, -1f, 1f);
		main = new Shaders("rsc/shaders/main.vert", "rsc/shaders/main.frag");
		entities = new ArrayList<Entity>();
		entities.add(player = new EntityPlayer(new Vec2(48, 80)));
		map = new Map(this, "rsc/maps/map0.txt");
		map.createMap();
		player.pos = map.spawn.copy();
		offset = new Vec2();
	}
	
	public void render(double elapse)
	{
		main.bind();
		main.setUniformMat4f("m_proj", ortho);
		map.render(elapse, main, offset);
		
		int i = 0;
		while (i < entities.size())
		{
			entities.get(i).render(main, offset);
			i++;
		}
	}
	
	public void update(int tick, double elapse)
	{
		map.update(tick, elapse);
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
					if (map.checkCollid(e, new AABB((int)last.x + 4, (int)e.pos.y + 16, 24, 48)))
					{
						e.pos.y = last.y;
						e.gravity = 0;
					}
					if (map.checkCollid(e, new AABB((int)e.pos.x + 4, (int)last.y + 16, 24, 48)))
					{
						e.pos.x = last.x;
					}
					if (entityCollid(e, new AABB((int)last.x + 4, (int)e.pos.y + 16, 24, 48)))
					{
						e.pos.y = last.y;
						e.gravity = 0;
					}
					if (entityCollid(e, new AABB((int)e.pos.x + 4, (int)last.y + 16, 24, 48)))
					{
						e.pos.x = last.x;
					}
					
				}
				else
				{
					AABB aabb = e.getBox();
					if (aabb != null)
					{
						if (map.checkCollid(e, new AABB((int)last.x, (int)e.pos.y, aabb.w, aabb.h)))
						{
							e.pos.y = last.y;
							e.gravity = 0;
						}
						if (map.checkCollid(e, new AABB((int)e.pos.x, (int)last.y, aabb.w, aabb.h)))
						{
							e.pos.x = last.x;
						}
						if (!e.spawnFront())
						{
							if (entityCollid(e, new AABB((int)last.x, (int)e.pos.y, aabb.w, aabb.h)))
							{
								e.pos.y = last.y;
								e.gravity = 0;
							}
							if (entityCollid(e, new AABB((int)e.pos.x, (int)last.y, aabb.w, aabb.h)))
							{
								e.pos.x = last.x;
							}
						}
					}
				}
				AABB box = e.getBox();
				if (box != null)
					e.inFloor = map.floorDetect(e, box) || entityFloor(e, new AABB(box.x + 8, box.y + box.h - 2, 16, 4));
				i++;
			}
			else
			{
				e.dispose();
				entities.remove(i);
			}
		}
		i = 0;
		while (i < entities.size())
		{
			Entity e0 = entities.get(i);
			AABB e0box = e0.getBox();
			if (e0box == null || !e0.spawnFront() || e0 instanceof IBlock)
			{
				i++;
				continue;
			}
			int j = 0;
			while (j < entities.size())
			{
				if (i == j)
				{
					j++;
					continue;
				}
				Entity e1 = entities.get(j);
				AABB e1box = e1.getBox();
				if (e1box == null || !e1.spawnFront())
				{
					j++;
					continue;
				}
				Vec2 collid = e0box.collided(e1box);
				if (collid != null)
				{
					if (e0.gravity >= 0 && e0box.y + e0box.h <= e1box.y + 4 + e0.gravity && e1 instanceof IWalkable)
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
	public void addFirstEntity(Entity entity)
	{
		entity.inFloor = false;
		entities.add(0, entity);
	}

	public Entity checkGrab(Entity entity) {
		int i = 0;
		AABB e0box = entity.getBox();
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity || !(e instanceof IMovable))
			{
				i++;
				continue;
			}
			AABB e1box = e.getBox();
			Vec2 collid = e0box.collided(e1box);
			if (collid != null)
			{
				Vec2 v = entity.pos.copy().sub(e.pos).add(0, 16f);
				if (v.length() <= 30)
					return (e);
			}
			
			i++;
		}
		return (null);
	}
	
	public boolean entityCollid(Entity entity, AABB aabb)
	{
		int i = 0;
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity || !(e instanceof IBlock))
			{
				i++;
				continue;
			}
			AABB e1box = e.getBox();
			if (e1box != null)
			{
				Vec2 collid = aabb.collided(e1box);
				if (collid != null)
					return (true);
			}
			i++;
		}
		return (false);
	}
	
	public boolean entityFloor(Entity entity, AABB aabb)
	{
		int i = 0;
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity || !(e instanceof IBlock))
			{
				i++;
				continue;
			}
			AABB e1box = e.getBox();
			if (e1box != null)
			{
				Vec2 collid = aabb.collided(e1box);
				if (collid != null)
					return (true);
			}
			i++;
		}
		return (false);
	}
	
	public void action(Entity entity) {
		int i = 0;
		AABB e0box = entity.getBox();
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity || !(e instanceof IActivable))
			{
				i++;
				continue;
			}
			AABB e1box = e.getBox();
			Vec2 collid = e0box.collided(e1box);
			if (collid != null)
			{
				((IActivable)e).action(entity);
			}
			
			i++;
		}
	}
	
}
