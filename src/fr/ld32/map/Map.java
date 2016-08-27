package fr.ld32.map;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import fr.ld32.AABB;
import fr.ld32.Game;
import fr.ld32.entities.*;
import fr.ld32.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Map
{
	Game game;
	Mesh mesh;
	int data[][];
	Texture texture;
	public Vec2 spawn;

	public Map(Game game, String mapPath)
	{
		this.game = game;
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
					data[y][x] = (color > 0 ? 0 : 1);
				}
			}
		} catch (IOException e) {
			return (false);
		}
		return (true);
	}
	
	private boolean reader(String path)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			ArrayList<Entity> entities = new ArrayList<Entity>();
			while ((line = reader.readLine()) != null)
			{
				line = line.toLowerCase();
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
					else if (data[3].equalsIgnoreCase("last1"))
						id = entities.size() - 2;
					else if (data[3].equalsIgnoreCase("last2"))
						id = entities.size() - 3;
					else if (data[3].equalsIgnoreCase("last3"))
						id = entities.size() - 4;
					else if (data[3].equalsIgnoreCase("last4"))
						id = entities.size() - 5;
					else if (data[3].equalsIgnoreCase("last5"))
						id = entities.size() - 6;
					else
						id = Integer.parseInt(data[3]);
					if (id >= 0 && id < entities.size())
					{
						if (entities.get(id) instanceof IActivableLink)
						{
							e = entities.get(id);
							entities.add(new EntityLever(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), (IActivableLink) e));
						}
					}
					if (e == null)
						entities.add(new EntityLever(new Vec2(Float.parseFloat(data[1]), Float.parseFloat(data[2])), null));
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
	
	public void createMap()
	{
		Random rand = new Random();
		long seed = System.nanoTime();
		mesh = new Mesh(data.length * data[0].length * 4 * 2);
		Vec2 addUVx = new Vec2(1f / 3f, 0);
		Vec2 addUVy = new Vec2(0, 1f / 3f);
		Vec2 addUV = new Vec2(1f / 3f, 1f / 3f);
		for (int y = 0; y < data.length; y++)
		{
			for (int x = 0; x < data[0].length; x++)
			{
				Vec2 pos = new Vec2(x * 32, y * 32);
				Color4f color = Color4f.WHITE;
				Vec2 uv = new Vec2();
				if (data[y][x] == 2) // ladder right
				{
					color = Color4f.GRAY;
					mesh.addVertices(pos).addColor(color).addTexCoord2f(uv);
					mesh.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
					mesh.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
					mesh.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
					color = Color4f.WHITE;
					uv = new Vec2(addUV.x * 2, addUV.y);
				}
				else if (data[y][x] == 3) // ladder left
				{
					color = Color4f.GRAY;
					mesh.addVertices(pos).addColor(color).addTexCoord2f(uv);
					mesh.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
					mesh.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
					mesh.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
					color = Color4f.WHITE;
					uv = new Vec2(addUV.x * 2, addUV.y * 2);
				}
				else if (data[y][x] == 0)
				{
					rand.setSeed(seed + y << 32 + x);
					if (rand.nextFloat() < 0.05f)
						uv.add(addUVx);
					color = Color4f.GRAY;
				}
				else
				{
					rand.setSeed(seed + y << 32 + x);
					if (rand.nextFloat() < 0.025f)
						uv.add(addUVx);
				}
				mesh.addVertices(pos).addColor(color).addTexCoord2f(uv);
				mesh.addVertices(pos.copy().add(32, 0)).addColor(color).addTexCoord2f(uv.copy().add(addUVx));
				mesh.addVertices(pos.copy().add(32, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUV));
				mesh.addVertices(pos.copy().add(0, 32)).addColor(color).addTexCoord2f(uv.copy().add(addUVy));
			}
		}
		mesh.buffering();
		texture = Res.images.get("tiles");
	}
	
	public void render(double elapse, Shaders shader, Vec2 offset)
	{
		shader.setUniformMat4f("m_view", Mat4.translate(offset));
		shader.setUniform2f("anim", new Vec2());
		texture.bind();
		if (mesh != null)
		{
			mesh.render(GL11.GL_QUADS);
		}
		Texture.unbind();
	}
	
	public void update(int tick, double elapse)
	{
		
	}
	
	public boolean checkCollid(AABB aabb)
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
				}
			}
			
		}
		return (false);
	}
	
	public boolean floorDetect(AABB aabb)
	{
		int minX = (int) (aabb.x + 8) / 32;
		int maxX = (int) (aabb.x + aabb.w - 8) / 32;
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
			}
		}
		return (false);
	}
}
