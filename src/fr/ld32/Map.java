package fr.ld32;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import fr.ld32.entities.Entity;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Map
{
	Game game;
	Mesh mesh;
	int data[][];
	
	public Map(Game game)
	{
		this.game = game;
	}
	
	public void createMap()
	{
		data = new int[][] {
			new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		};
		ArrayList<Vec2> pointMeshList = new ArrayList<Vec2>();
		for (int y = 0; y < data.length; y++)
		{
			for (int x = 0; x < data[0].length; x++)
			{
				if (data[y][x] > 0)
				{
					pointMeshList.add(new Vec2(x * 32, y * 32));
					pointMeshList.add(new Vec2(x * 32 + 32, y * 32));
					pointMeshList.add(new Vec2(x * 32 + 32, y * 32 + 32));
					pointMeshList.add(new Vec2(x * 32 + 32, y * 32 + 32));
					pointMeshList.add(new Vec2(x * 32, y * 32 + 32));
					pointMeshList.add(new Vec2(x * 32, y * 32));
				}
			}
		}
		mesh = new Mesh(pointMeshList.size());
		for (int i = 0; i < pointMeshList.size(); i++)
		{
			mesh.addVertices(pointMeshList.get(i));
			mesh.addColor(Color4f.LIGHT_GRAY);
		}
		mesh.buffering();
	}
	
	public void render(double elapse, Shaders shader, Vec2 offset)
	{
		shader.setUniformMat4f("m_view", Mat4.translate(offset));
		if (mesh != null)
		{
			mesh.render(GL11.GL_TRIANGLES);
		}
	}
	
	public void update(int tick, double elapse)
	{
		
	}
	
	public Vec2 checkCollid(Entity e)
	{
		AABB aabb = e.getBox();
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
					if (data[y][x] > 0)
					{
						Vec2 collid = aabb.collided(x * 32, y * 32, 32, 32);
						if (collid.x != 0 || collid.y != 0)
						{
							if (Math.abs(collid.x) > Math.abs(collid.y))
								collid.y = 0;
							else
								collid.x = 0;
							return (collid);
						}
					}
				}
			}
			
		}
		return (null);
	}
}
