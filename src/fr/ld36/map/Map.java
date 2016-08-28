package fr.ld36.map;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import fr.ld36.AABB;
import fr.ld36.Game;
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

	public Map(Game game, String mapPath)
	{
		this.game = game;
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
					else if (color == 0xff0000)
						data[y][x] = 4;
					else if (color == 0x00ff00)
						data[y][x] = 5;
					else if (color == 0x808080)
						data[y][x] = 6;
					else if (color == 0x800080)
						data[y][x] = 7;
				}
			}
		} catch (IOException e) {
			return (false);
		}
		return (true);
	}
	
	private void createLink(Vec2 pos, Entity e)
	{
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
			ArrayList<Entity> entities = new ArrayList<Entity>();
			link = new ArrayList<Vec2>();
			while ((line = reader.readLine()) != null)
			{
				String data[];
				data = line.split(" ");
				if (data[0].equalsIgnoreCase("set"))
				{
					loadmap(data[1]);
				}
				else if (data[0].equalsIgnoreCase("spawn"))
				{
					spawn = new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
				}
				else if (data[0].equalsIgnoreCase("ladder"))
				{
					if (data[1].equalsIgnoreCase("right"))
					{
						makeLadder(Integer.parseInt(data[2]), Integer.parseInt(data[3]), 2);
					}
					else
					{
						makeLadder(Integer.parseInt(data[2]), Integer.parseInt(data[3]), 3);
					}
				}
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
				{
					entities.add(new EntityBox(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2]))));
				}
				else if (data[0].equalsIgnoreCase("spawnDoor"))
				{
					entities.add(new EntityDoor(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), Integer.parseInt(data[3])));
				}
				else if (data[0].equalsIgnoreCase("spawnTrap"))
				{
					entities.add(new EntityTrap(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), Integer.parseInt(data[3])));
				}
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
							createLink(pos.copy().add(16), e);
							entities.add(new EntityPlate(pos, (IActivableLink) e));
						}
					}
					if (e == null)
						entities.add(new EntityPlate(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), null));
				}
			}
			
			reader.close();
			for (Entity e : entities)
			{
				if (e.spawnFront())
					game.addEntity(e);
				else
					game.addFirstEntity(e);
					
			}
		} catch (IOException e) {
			return (false);
		}
		return (true);
	}
	
	private void makeLadder(int x, int y, int id)
	{
		if (x < 0 || x >= data[0].length || y < 0 || y >= data.length)
			return;
		if (data[y][x] == 0)
		{
			data[y][x] = id;
			makeLadder(x, y + 1, id);
		}
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
		for (int y = 0; y < data.length; y++)
		{
			for (int x = 0; x < data[0].length; x++)
			{
				Vec2 pos = new Vec2(x * 32, y * 32);
				Color4f color = Color4f.WHITE;
				Vec2 uv = new Vec2();
				color = Color4f.GRAY;
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
				if (data[y][x] <= 0)
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
				meshFront.addVertices(pos).addColor(color).addTexCoord2f(uv);
				meshFront.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
				meshFront.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
				meshFront.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
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
		if (meshSystem != null)
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
		int minY = (int) (aabb.y + aabb.h) / 32;
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

	public void dispose()
	{
		meshBack.dispose();
		meshFront.dispose();
		meshSystem.dispose();
	}
}
