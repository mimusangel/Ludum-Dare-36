package fr.ld36.map;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
import fr.ld36.LD36;
import fr.ld36.entities.*;
import fr.ld36.entities.spe.IActivableLink;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Map
{
	Game game;
	Mesh meshBack;
	Mesh meshFront;
	Mesh meshSystem;
	ArrayList<Vec2> link;
	int data[][];
	Texture texture;
	public Vec2 spawn;
	String set;
	ArrayList<Entity> entitiesOrigin;

	public Map(Game game, String mapPath)
	{
		this.game = game;
		set = "";
		entitiesOrigin = new ArrayList<Entity>();
		texture = Res.images.get("tiles");
		reader(mapPath);
	}
	
	private boolean loadmap(String path)
	{
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			data = new int[image.getHeight()][image.getWidth()];
			for (int y = 0; y < data.length; y++)
			{
				for (int x = 0; x < data[0].length; x++)
				{
					int color = image.getRGB(x, y) & 0xffffff;
					data[y][x] = 0;
					if (color == 0)
						data[y][x] = 1;
					else if (color == 0x008000)
						data[y][x] = 2;
					else if (color == 0x808000)
						data[y][x] = 3;
					else if (color == 0xff0000)
						data[y][x] = 4;
					else if (color == 0x00ff00)
						data[y][x] = 5;
					else if (color == 0x808080)
						data[y][x] = 6;
					else if (color == 0x800080)
						data[y][x] = 7;
					else if (color == 0xc0c0c0)
						data[y][x] = 8;
					else if (color == 0xff8000)
						data[y][x] = 9;
					else if (color == 0x0000ff)
						data[y][x] = 10;
				}
			}
		} catch (IOException e) {
			return (false);
		}
		return (true);
	}
	
	private void createLink(Vec2 pos, Entity e)
	{
		if (e == null)
			return;
		link.add(pos);
		if (e instanceof EntityDoor)
			link.add(e.pos.copy().add(16, 2));
		else if (e instanceof EntityTrap)
		{
			if (e.pos.x + 32 < pos.x + 16)
				link.add(e.pos.copy().add(62, 2));
			else
				link.add(e.pos.copy().add(2, 2));
		}
	}
	
	private boolean reader(String path)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			link = new ArrayList<Vec2>();
			ArrayList<Entity> entities = new ArrayList<Entity>();
			while ((line = reader.readLine()) != null)
			{
				String data[];
				data = line.split(" ");
				if (data[0].equalsIgnoreCase("set"))
				{
					set = data[1];
					loadmap(data[1]);
				}
				else if (data[0].equalsIgnoreCase("spawn"))
					spawn = new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
				else if (data[0].equalsIgnoreCase("spawnSign"))
				{
					int ts = line.indexOf("\"");
					String txt = "";
					if (ts >= 0)
					{
						txt = line.substring(ts + 1, line.length() - 1);
					}
					entities.add(new EntitySign(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), txt));
				}
				else if (data[0].equalsIgnoreCase("spawnBox"))
					entities.add(new EntityBox(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				else if (data[0].equalsIgnoreCase("spawnBones"))
					entities.add(new EntityBones(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				else if (data[0].equalsIgnoreCase("spawnMummy"))
					entities.add(new EntityMummy(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				else if (data[0].equalsIgnoreCase("spawnDoor"))
					entities.add(new EntityDoor(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), Integer.parseInt(data[3])));
				else if (data[0].equalsIgnoreCase("spawnTrap"))
					entities.add(new EntityTrap(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), Integer.parseInt(data[3])));
				else if (data[0].equalsIgnoreCase("spawnLever"))
				{
					Entity e = null;
					int id;
					if (data[3].equalsIgnoreCase("last") || data[3].equalsIgnoreCase("last0"))
						id = entities.size() - 1;
					else if (data[3].startsWith("last"))
					{
						id = entities.size() - 1 - Integer.parseInt(data[3].substring(4));
					}
					else if (data[3].equalsIgnoreCase("first") || data[3].equalsIgnoreCase("first0"))
						id = entities.size() - 1;
					else if (data[3].startsWith("first"))
					{
						id = entities.size() - 1 - Integer.parseInt(data[3].substring(5));
					}
					else
						id = Integer.parseInt(data[3]);
					if (id >= 0 && id < entities.size())
					{
						if (entities.get(id) instanceof IActivableLink)
						{
							e = entities.get(id);
							Vec2 pos = new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
							createLink(pos.copy().add(16), e);
							entities.add(new EntityLever(pos, (IActivableLink) e));
						}
					}
					if (e == null)
						entities.add(new EntityLever(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), null));
				}
				else if (data[0].equalsIgnoreCase("spawnPlate"))
				{
					Entity e = null;
					int id;
					if(data.length > 3)
					{
						if (data[3].equalsIgnoreCase("last") || data[3].equalsIgnoreCase("last0"))
							id = entities.size() - 1;
						else if (data[3].startsWith("last"))
						{
							id = entities.size() - 1 - Integer.parseInt(data[3].substring(4));
						}
						else if (data[3].equalsIgnoreCase("first") || data[3].equalsIgnoreCase("first0"))
							id = entities.size() - 1;
						else if (data[3].startsWith("first"))
						{
							id = entities.size() - 1 - Integer.parseInt(data[3].substring(5));
						}
						else
							id = Integer.parseInt(data[3]);
					}else{
						id = -1;
					}
					if (id >= 0 && id < entities.size())
					{
						if (entities.get(id) instanceof IActivableLink)
						{
							e = entities.get(id);
							Vec2 pos = new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
							createLink(pos.copy().add(16, 30), e);
							entities.add(new EntityPlate(pos, (IActivableLink) e));
						}
					}
					if (e == null)
						entities.add(new EntityPlate(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), null));
				}
				else if (data[0].equalsIgnoreCase("spawnChest"))
				{
					/*int rarity = 1;
					ArrayList<Item> items = new ArrayList<Item>();
					try{
						if(data.length > 3) rarity = Integer.valueOf(data[3]);
					}catch(NumberFormatException e){}
					if(data.length > 4){
						for(int i = 4; i < data.length; i++){
							if(data[i].equalsIgnoreCase("flashLight")) items.add(new ItemFlashlight());
							if(data[i].equalsIgnoreCase("sword")) items.add(new ItemSword());
						}
					}
					Item[] itemArray = new Item[items.size()];
					items.toArray(itemArray);*/
					entities.add(new EntityChest(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				}
				else if (data[0].equalsIgnoreCase("spawnSarcophage"))
					entities.add(new EntitySarcophage(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				
			}
			
			reader.close();
			for (Entity e : entities)
			{
				entitiesOrigin.add(e.copy());
				entitiesOrigin.get(entitiesOrigin.size() - 1).createEntity();
				if (e.spawnFront())
					game.addEntity(e);
				else
					game.addFirstEntity(e);
					
			}
			for (Entity e : entitiesOrigin)
			{
				if (e instanceof EntityLever)
				{
					for (int j = 0; j < entities.size(); j++)
					{
						if (((EntityLever)e).link == entities.get(j))
						{
							((EntityLever)e).link = (IActivableLink) entitiesOrigin.get(j);
							break;
						}
					}
				}
				if (e instanceof EntityPlate)
				{
					for (int j = 0; j < entities.size(); j++)
					{
						if (((EntityPlate)e).link == entities.get(j))
						{
							((EntityPlate)e).link = (IActivableLink) entitiesOrigin.get(j);
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			return (false);
		}
		return (true);
	}

	public int getHeight()
	{
		return (data.length);
	}
	
	public int getWidth()
	{
		return (data[0].length);
	}
	
	public void createMap()
	{
		float vx = 32f / (float)texture.getWidth();
		float vy = 32f / (float)texture.getHeight();
		Vec2 addUVx = new Vec2(vx, 0);
		Vec2 addUVy = new Vec2(0, vy);
		Vec2 addUV = new Vec2(vx, vy);
		meshBack = new Mesh(data.length * data[0].length * 4);
		meshFront = new Mesh(data.length * data[0].length * 4);
		Random rand = new Random();
		for (int y = 0; y < data.length; y++)
		{
			for (int x = 0; x < data[0].length; x++)
			{
				Vec2 pos = new Vec2(x * 32, y * 32);
				Color4f color = Color4f.WHITE;
				Vec2 uv = new Vec2();
				color = Color4f.GRAY;
				if (data[y][x] == 8)
				{
					uv.add(addUV.x * (float)rand.nextInt(5), addUV.y * (4f + (float)rand.nextInt(4)));
				}
				if (data[y][x] == 9)
				{
					if (y > 0 && data[y - 1][x] == 9)
						uv.add(addUV.x * 1, addUV.y * 2);
					else
						uv.add(addUV.x * 1, addUV.y * 1);
				}
				meshBack.addVertices(pos).addColor(color).addTexCoord2f(uv);
				meshBack.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
				meshBack.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
				meshBack.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
			}
		}
		meshBack.buffering();
		for (int y = 0; y < data.length; y++)
		{
			for (int x = 0; x < data[0].length; x++)
			{
				if (data[y][x] <= 0 || data[y][x] == 8 || data[y][x] == 9)
					continue;
				Vec2 pos = new Vec2(x * 32, y * 32);
				Color4f color = Color4f.WHITE;
				Vec2 uv = new Vec2();
				if (data[y][x] >= 2)
				{
					if (data[y][x] == 2)
						uv = new Vec2(addUV.x * 2, addUV.y);
					else if (data[y][x] == 3)
						uv = new Vec2(addUV.x * 2, addUV.y * 2);
					else if (data[y][x] == 4)
						uv = new Vec2(addUV.x * 2, addUV.y * 3);
					else if (data[y][x] == 5)
					{
						if (y == 0 || (y > 0 && data[y - 1][x] != 5))
							uv = new Vec2(0, addUV.y);
						else if (y == data.length - 1 || (y < data.length - 1 && data[y + 1][x] != 5))
							uv = new Vec2(0, addUV.y * 3);
						else
							uv = new Vec2(0, addUV.y * 2);
					}
					else if (data[y][x] == 6)
						uv = new Vec2(addUV.x, addUV.y * 3);
					else if (data[y][x] == 7)
						uv = new Vec2(addUV.x * 2, 0);
				}
				if (data[y][x] == 10)
				{
					meshFront.addVertices(pos.copy().add(-16, 0)).addColor(color).addTexCoord2f(addUV.x * 3, addUV.y * 1);
					meshFront.addVertices(pos.copy().add(48, 0)).addColor(color).addTexCoord2f(addUV.x * 5, addUV.y * 1);
					meshFront.addVertices(pos.copy().add(48, 64)).addColor(color).addTexCoord2f(addUV.x * 5, addUV.y * 3);
					meshFront.addVertices(pos.copy().add(-16, 64)).addColor(color).addTexCoord2f(addUV.x * 3, addUV.y * 3);
				}
				else
				{
					meshFront.addVertices(pos).addColor(color).addTexCoord2f(uv);
					meshFront.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
					meshFront.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
					meshFront.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
				}
			}
		}
		meshFront.buffering();
		meshSystem = new Mesh(link.size());
		Color4f c = new Color4f(Color4f.DARK_GRAY);
		c.alpha = 0.5f;
		for (int i = 0; i < link.size(); i += 2)
		{
			Vec2 p0 = link.get(i);
			Vec2 p1 = link.get(i + 1);
			meshSystem.addVertices(p0).addColor(c);
			meshSystem.addVertices(p1).addColor(c);
		}
		meshSystem.buffering();
	}
	
	public void render(double elapse, Shaders shader)
	{
		shader.setUniformMat4f("m_view", Mat4.identity());
		shader.setUniform2f("anim", new Vec2());
		texture.bind();
		if (meshBack != null)
			meshBack.render(GL11.GL_QUADS);
		if (meshSystem != null && LD36.getInstance().game.player.viewSystem())
		{
			shader.setUniform1i("disableTexture", 1);
			meshSystem.render(GL11.GL_LINES);
			shader.setUniform1i("disableTexture", 0);
		}
		if (meshFront != null)
			meshFront.render(GL11.GL_QUADS);
		
		Texture.unbind();
	}
	
	public void update(int tick, double elapse)
	{
		
	}
	
	public boolean checkCollid(Entity e, AABB aabb)
	{
		if (aabb != null)
		{
			int minX = (int) aabb.x / 32;
			int maxX = (int) (aabb.x + aabb.w) / 32;
			int minY = (int) aabb.y / 32;
			int maxY = (int) (aabb.y + aabb.h) / 32;
			if (minX < 0)
				minX = 0;
			if (maxX >= data[0].length)
				maxX = data[0].length - 1;
			if (minY < 0)
				minY = 0;
			if (maxY >= data.length)
				maxY = data.length - 1;
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (data[y][x] == 1)
					{
						boolean collid = aabb.collided(x * 32, y * 32, 32, 32);
						if (collid)
							return (true);
					}
					if (data[y][x] == 6)
					{
						boolean collid = aabb.collided(x * 32, y * 32, 32, (int) (8 + e.velocity.y));
						if (collid && e.velocity.y > 0 && aabb.y + aabb.h <= y * 32 + 4 + e.velocity.y)
							return (true);
					}
				}
			}
			
		}
		return (false);
	}
	
	public boolean floorDetect(Entity e, AABB aabb)
	{
		int minX = (int) (aabb.x + 4) / 32;
		int maxX = (int) (aabb.x + aabb.w - 4) / 32;
		int minY = (int) (aabb.y + aabb.h - 16) / 32;
		int maxY = (int) (aabb.y + aabb.h + 1) / 32;
		for (int y = minY; y <= maxY; y++)
		{
			if (y < 0) continue;
			if (y >= data.length) break;
			for (int x = minX; x <= maxX; x++)
			{
				if (x < 0) continue;
				if (x >= data[0].length) break;
				if (data[y][x] == 2 && aabb.collided(x * 32 + 24, y * 32, 8, 32))
					return (true);
				if (data[y][x] == 3 && aabb.collided(x * 32, y * 32, 8, 32))
					return (true);
				if (data[y][x] == 1)
					return (true);
				if (data[y][x] == 6)
				{
					AABB tmp = new AABB(aabb.x, aabb.y + aabb.h - 2, aabb.w, 4);
					if (tmp.collided(x * 32, y * 32, 32, 4))
					return (true);
				}
			}
		}
		return (false);
	}
	
	public void checkDamage(Entity e)
	{
		AABB aabb = e.getBox();
		if (aabb == null)
			return ;
		int minX = (int) (aabb.x) / 32;
		int maxX = (int) (aabb.x + aabb.w) / 32;
		int minY = (int) (aabb.y) / 32;
		int maxY = (int) (aabb.y + aabb.h) / 32;
		for (int y = minY; y <= maxY; y++)
		{
			if (y < 0) continue;
			if (y >= data.length) break;
			for (int x = minX; x <= maxX; x++)
			{
				if (x < 0) continue;
				if (x >= data[0].length) break;
				if (data[y][x] == 7 && aabb.collided(x * 32, y * 32 + 18, 32, 14))
				{
					e.giveDamage(x, y, 1);
					return;
				}
			}
		}
	}
	private int getEntityID(IActivableLink link)
	{
		for (int i = 0; i < entitiesOrigin.size(); i++)
		{
			if (link == entitiesOrigin.get(i))
			{
				return (i);
			}
		}
		return (-1);
	}
	public void adminEditSave()
	{
		String p = set.substring(0, set.lastIndexOf(".")) + ".tmp";
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(p));
			write.write("set " + set + "\n");
			write.write("spawn " + spawn.x + " " + spawn.y + "\n");
			for (Entity e : entitiesOrigin)
			{
				if (e instanceof EntitySign)
					write.write("spawnSign " + e.pos.x + " " + e.pos.y + " \"" + ((EntitySign)e).text + "\"" + "\n");
				else if (e instanceof EntityBox)
					write.write("spawnBox " + e.pos.x + " " + e.pos.y + "\n");
				else if (e instanceof EntityBones)
					write.write("spawnBones " + e.pos.x + " " + e.pos.y + "\n");
				else if (e instanceof EntityDoor)
					write.write("spawnDoor " + e.pos.x + " " + e.pos.y + " " + ((EntityDoor)e).nbLock + "\n");
				else if (e instanceof EntityTrap)
					write.write("spawnTrap " + e.pos.x + " " + e.pos.y + " " + ((EntityTrap)e).nbLock + "\n");
				else if (e instanceof EntityLever)
					write.write("spawnLever " + e.pos.x + " " + e.pos.y + " " + getEntityID(((EntityLever)e).link) + "\n");
				else if (e instanceof EntityPlate)
					write.write("spawnPlate " + e.pos.x + " " + e.pos.y + " " + getEntityID(((EntityPlate)e).link) + "\n");
				else if (e instanceof EntityChest)
					write.write("spawnChest " + e.pos.x + " " + e.pos.y + "\n");
				else if (e instanceof EntityMummy)
					write.write("spawnMummy " + e.pos.x + " " + e.pos.y + "\n");
				else if (e instanceof EntitySarcophage)
					write.write("spawnSarcophage " + e.pos.x + " " + e.pos.y + "\n");
			}
			write.close();
			System.out.println("Map saved! " + p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void renderEditMode(Shaders shader)
	{
		int i = 0;
		while (i < entitiesOrigin.size())
		{
			entitiesOrigin.get(i).render(shader);
			i++;
		}
	}
	
	public void dispose()
	{
		meshBack.dispose();
		meshFront.dispose();
		meshSystem.dispose();
	}

	public void refreshMeshSystem()
	{
		if (meshSystem != null)
			meshSystem.dispose();
		link.clear();
		for (Entity e : entitiesOrigin)
		{
			if (e instanceof EntityLever)
				createLink(e.pos.copy().add(16f), (Entity)((EntityLever)e).link);
			if (e instanceof EntityPlate)
				createLink(e.pos.copy().add(16, 30), (Entity)((EntityPlate)e).link);
		}
		meshSystem = new Mesh(link.size());
		Color4f c = new Color4f(Color4f.DARK_GRAY);
		c.alpha = 0.5f;
		for (int i = 0; i < link.size(); i += 2)
		{
			Vec2 p0 = link.get(i);
			Vec2 p1 = link.get(i + 1);
			meshSystem.addVertices(p0).addColor(c);
			meshSystem.addVertices(p1).addColor(c);
		}
		meshSystem.buffering();
	}
	
	public void addSelectEntity(Entity e)
	{
		if (e != null)
		{
				entitiesOrigin.add(e);
		}
	}
	
	public int getSelectEntity(Vec2 p)
	{
		int i = 0;
		while (i < entitiesOrigin.size())
		{
			if (entitiesOrigin.get(i).getSelectBox().collided(p))
				return (i);
			i++;
		}
		return (-1);
	}
	
	public Entity getEntity(int id)
	{
		if (id < 0 || id >= entitiesOrigin.size())
			return (null);
		return (entitiesOrigin.get(id));
	}

	public void deleteSelectEntity(int id)
	{
		Entity e = getEntity(id);
		if (e != null)
		{
			e.dispose();
			entitiesOrigin.remove(id);
		}
	}
}
