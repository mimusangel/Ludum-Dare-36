package fr.ld36;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import fr.ld36.entities.*;
import fr.ld36.entities.spe.IActivable;
import fr.ld36.entities.spe.IBlock;
import fr.ld36.entities.spe.IMovable;
import fr.ld36.entities.spe.IWalkable;
import fr.ld36.items.Item;
import fr.ld36.map.Map;
import fr.ld36.render.Renderer;
import fr.ld36.utils.Audio;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.input.Mouse;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Game
{
	Mat4 ortho;
	public Shaders main;
	public Shaders hud;
	public Map map;
	public EntityPlayer player;
	public ArrayList<Entity> entities;
	ArrayList<Entity> entitiesView;
	Vec2 offset;
	
	Texture hudHeart;
	Mesh	meshHeart;
	Texture hudStamina;
	Mesh	meshStamina;
	Texture hudCoin;
	Mesh	meshCoin;
	
	Audio sfxAction;
	Audio sfxActionFail;
	
	public Game()
	{
		ortho = Mat4.orthographic(0, 720 * 9 / 16, 720, 0, -1f, 1f);
		main = new Shaders("rsc/shaders/main.vert", "rsc/shaders/main.frag");
		hud = new Shaders("rsc/shaders/main.vert", "rsc/shaders/hud.frag");
		entities = new ArrayList<Entity>();
		entitiesView = new ArrayList<Entity>();
		entities.add(player = new EntityPlayer(new Vec2(48, 80)));
		map = new Map(this, "rsc/maps/map0.txt");
		map.createMap();
		player.pos = map.spawn.copy();
		offset = new Vec2();
		hudHeart = Res.images.get("heart");
		meshHeart = new Mesh(4);
		meshHeart.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		meshHeart.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		meshHeart.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1);
		meshHeart.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1);
		meshHeart.buffering();
		hudStamina = Res.images.get("stamina");
		meshStamina = new Mesh(4);
		meshStamina.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		meshStamina.addVertices(128, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		meshStamina.addVertices(128, 16).addColor(Color4f.WHITE).addTexCoord2f(1f, 0.5f);
		meshStamina.addVertices(0, 16).addColor(Color4f.WHITE).addTexCoord2f(0, 0.5f);
		meshStamina.buffering();
		hudCoin = Res.images.get("coin");
		meshCoin = new Mesh(4);
		meshCoin.addVertices(0, 0).addColor(Color4f.WHITE).addTexCoord2f(0, 0);
		meshCoin.addVertices(32, 0).addColor(Color4f.WHITE).addTexCoord2f(1f, 0);
		meshCoin.addVertices(32, 32).addColor(Color4f.WHITE).addTexCoord2f(1f, 1f);
		meshCoin.addVertices(0, 32).addColor(Color4f.WHITE).addTexCoord2f(0, 1f);
		meshCoin.buffering();
		sfxAction = Audio.list.get("rsc/sounds/actionDefault.wav");
		sfxActionFail = Audio.list.get("rsc/sounds/actionFail.wav");
	}
	
	public void initRender()
	{
		main.bind();
		main.setUniformMat4f("m_proj", ortho);
		main.setUniformMat4f("m_offset", Mat4.translate(offset));
		if (player.grab != null)
		{
			main.setUniform1f("ligthDist", 240f);
			main.setUniform2f("ligthPos", player.pos.copy().add(16, 32));
		}
		else
		{
			main.setUniform1f("ligthDist", 320f);
			main.setUniform2f("ligthPos", player.getLightPos());
		}
		main.setUniformMat4f("m_view", Mat4.identity());
	}
	
	public void render(double elapse)
	{
		initRender();
		map.render(elapse, main);
		
		if (player.editMode)
		{
			player.render(main);
			map.renderEditMode(main);
		}
		else
		{
			int i = 0;
			while (i < entitiesView.size())
			{
				entitiesView.get(i).render(main);
				i++;
			}
			player.renderGrab(main);
		}
		renderHUD(elapse);
	}
	
	public void initRenderHUD()
	{
		hud.bind();
		hud.setUniformMat4f("m_proj", ortho);
		hud.setUniformMat4f("m_offset", Mat4.identity());
		hud.setUniform4f("color", Color4f.WHITE.toVector4());
		hud.setUniform2f("mulTexture", new Vec2(1, 1));
		hud.setUniform2f("offsetTexture", new Vec2());
		hud.setUniformMat4f("m_view", Mat4.identity());
	}
	public void renderHUD(double elapse)
	{
		Texture.unbind();
		initRenderHUD();
		hudHeart.bind();
		for (int i = 0; i < player.getLife(); i++)
		{
			hud.setUniformMat4f("m_view", Mat4.multiply(Mat4.scale(0.75f), Mat4.translate(5 + i * 32, 5)));
			meshHeart.render(GL11.GL_QUADS);
		}
		Texture.unbind();
		hudStamina.bind();
		hud.setUniform2f("offsetTexture", new Vec2(0.0f, 0.5f));
		hud.setUniformMat4f("m_view", Mat4.translate(5, 405 - 21));
		meshStamina.render(GL11.GL_QUADS);
		hud.setUniform2f("offsetTexture", new Vec2(0.0f, 0.0f));
		float p = ((float)player.getStamina() / 100f);
		hud.setUniform2f("mulTexture", new Vec2(p, 1));
		hud.setUniformMat4f("m_view", Mat4.multiply(Mat4.scale(p, 1f, 1f), Mat4.translate(5, 405 - 21)));
		meshStamina.render(GL11.GL_QUADS);
		hud.setUniform2f("mulTexture", new Vec2(1f, 1f));
		if (player.debugMode)
		{
			Renderer.drawString(hud, "e: " + entitiesView.size() + " / " + entities.size(), new Vec2(5, 30), player.editMode ? Color4f.LIGHT_RED : Color4f.WHITE);
			Renderer.drawString(hud, "x: " + player.pos.x, new Vec2(5, 40), Color4f.WHITE);
			Renderer.drawString(hud, "y: " + player.pos.y, new Vec2(5, 50), Color4f.WHITE);
			if (player.editMode)
			{
				Renderer.drawString(hud, "noclip: " + player.noclip, new Vec2(5, 60), Color4f.WHITE);
				Vec2 m = getMousePos();
				Vec2 mp = getMousePosInMap();
				Renderer.drawString(hud, "- grid: " + player.gridAlign, new Vec2(5, 70), Color4f.WHITE);
				Renderer.drawString(hud, "- mx: " + mp.x + " ( " + m.x + " )", new Vec2(5, 80), Color4f.WHITE);
				Renderer.drawString(hud, "- my: " + mp.y + " ( " + m.y + " )", new Vec2(5, 90), Color4f.WHITE);
			}
			hud.setUniform4f("color", Color4f.WHITE.toVector4());
		}
		hudCoin.bind();
		hud.setUniform2f("offsetTexture", new Vec2(0f, 0f));
		hud.setUniformMat4f("m_view", Mat4.multiply(Mat4.scale(1f), Mat4.translate(LD36.WINDOW_WIDTH - 37, 5)));
		meshCoin.render(GL11.GL_QUADS);
		Renderer.drawString(hud, player.getMoney()+"", new Vec2(LD36.WINDOW_WIDTH-50 - ((player.getMoney()+"").length() * 6.2f), 20), Color4f.WHITE);
		Texture.unbind();
		
		Item i;
		hud.setUniformMat4f("m_view", Mat4.multiply(Mat4.scale(1f), Mat4.translate(LD36.WINDOW_WIDTH / 2 - 64, 5)));
		i = player.getInv().getItem(0);
		if(i != null)
			i.getAnimation().render(main, new Vec2(0));
		
		hud.setUniformMat4f("m_view", Mat4.multiply(Mat4.scale(1f), Mat4.translate(LD36.WINDOW_WIDTH / 2 - 32, 5)));
		i = player.getInv().getItem(1);
		if(i != null)
			i.getAnimation().render(main, new Vec2(0));
	}
	
	public void update(int tick, double elapse)
	{
		map.update(tick, elapse);
		int i = 0;
		entitiesView.clear();
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e.entityAlive())
			{
				double dist = e.pos.copy().sub(player.pos).lengthSqrd();
				if (dist <= 420f * 420f)
					entitiesView.add(e);
				Vec2 last = e.pos.copy();
				e.update(this, tick, elapse);
				/*
				 *  MAP COLLID
				 */
				if (e instanceof EntityPlayer)
				{
					if (!((EntityPlayer)e).noclip)
					{
						if (map.checkCollid(e, new AABB((int)last.x + 4, (int)e.pos.y + 16, 24, 48)))
						{
							e.pos.y = last.y;
							e.velocity.y = 0;
						}
						if (map.checkCollid(e, new AABB((int)e.pos.x + 4, (int)last.y + 16, 24, 48)))
						{
							e.pos.x = last.x;
						}
						if (entityCollid(e, new AABB((int)last.x + 4, (int)e.pos.y + 16, 24, 48)))
						{
							e.pos.y = last.y;
							e.velocity.y = 0;
						}
						if (entityCollid(e, new AABB((int)e.pos.x + 4, (int)last.y + 16, 24, 48)))
						{
							e.pos.x = last.x;
							e.velocity.x = 0;
						}
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
							e.velocity.y = 0;
						}
						if (map.checkCollid(e, new AABB((int)e.pos.x, (int)last.y, aabb.w, aabb.h)))
						{
							e.pos.x = last.x;
							e.velocity.x = 0;
						}
						if (e.spawnFront())
						{
							if (entityCollid(e, new AABB((int)last.x, (int)e.pos.y, aabb.w, aabb.h)))
							{
								e.pos.y = last.y;
								e.velocity.y = 0;
							}
							if (entityCollid(e, new AABB((int)e.pos.x, (int)last.y, aabb.w, aabb.h)))
							{
								e.pos.x = last.x;
								e.velocity.x = 0;
							}
						}
					}
				}
				AABB box = e.getBox();
				if (box != null)
					e.inFloor = map.floorDetect(e, box) || entityFloor(e, new AABB(box.x + 8, box.y + box.h - 6, 16, 8));
				map.checkDamage(e);
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
					if (e0.velocity.y >= 0 && e0box.y + e0box.h <= e1box.y + 4 + e0.velocity.y && e1 instanceof IWalkable)
					{
						if (e0 instanceof EntityPlayer)
							e0.pos.y = e1box.y - 64;
						else
							e0.pos.y = e1box.y - e0box.h;
						e0.velocity.y = 0;
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
		boolean actionFail = true;
		AABB e0box = entity.getBox();
		while (i < entities.size())
		{
			Entity e = entities.get(i);
			if (e == entity || !(e instanceof IActivable))
			{
				if(!(e instanceof EntityItem && (((EntityItem)e).getItem() instanceof IActivable))){
					i++;
					continue;
				}
			}
			AABB e1box = e.getBox();
			Vec2 collid = e0box.collided(e1box);
			if (collid != null)
			{
				actionFail = false;
				//Ramassage Item
				if(e instanceof EntityItem){
					EntityItem ei = (EntityItem) e;
					player.getInv().addItem(ei.getItem());
					e.setLife(0);
				}
				//Activation entity
				else{
					((IActivable)e).action(entity);
				}
			}
			
			i++;
		}
		if (actionFail)
			sfxActionFail.setVolume(0.65f).play();
		else
			sfxAction.setVolume(0.65f).play();
	}

	
	public Vec2 getMousePos()
	{
		LD36 ld36 = LD36.getInstance();
		Mouse m = ld36.win.getMouse();
		Vec2 mp = new Vec2((float)m.getX(), (float)m.getY());
		if (player != null)
			mp.sub(player.getOffset());
		mp.x = (float) Math.floor(mp.x);
		mp.y = (float) Math.floor(mp.y);
		return (mp);
	}
	
	public Vec2 getMousePosInMap()
	{
		LD36 ld36 = LD36.getInstance();
		Mouse m = ld36.win.getMouse();
		Vec2 mp = new Vec2((float)m.getX(), (float)m.getY());
		if (player != null)
			mp.sub(player.getOffset());
		mp.div(32f * ld36.getScaleX(), 32f * ld36.getScaleY());
		mp.x = (float) Math.floor(mp.x);
		mp.y = (float) Math.floor(mp.y);
		return (mp);
	}
	
	public void dispose()
	{
		while (entities.size() > 0)
		{
			entities.get(0).dispose();
			entities.remove(0);
		}
		meshHeart.dispose();
		meshStamina.dispose();
		meshCoin.dispose();
		if (map != null)
			map.dispose();
	}
	
}
