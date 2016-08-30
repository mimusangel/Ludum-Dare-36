package fr.ld36.render;

import org.lwjgl.opengl.GL11;

import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Mesh;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.graphics.Texture;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;

public class Renderer
{
	public static Texture font;
	private static String fontArray = "0123456789"
									+ "abcdefghij"
									+ "klmnopqrst"
									+ "uvwxyz    "
									+ "ABCDEFGHIJ"
									+ "KLMNOPQRST"
									+ "UVWXYZ    "
									+ ".:/\\?!,;[]"
									+ "=+-_><(){}"
									+ "'\"@       ";
	private static Mesh fontMesh;
	public static void load()
	{
		font = Res.images.get("font");
		float w = font.getWidth() / 10f; 
		float h = font.getHeight() / 10f;
		float tw = w / (float)font.getWidth(); 
		float th = h / (float)font.getHeight();
		fontMesh = new Mesh(4);
		fontMesh.addVertices(0, 0).addTexCoord2f(0f, 0f).addColor(Color4f.WHITE);
		fontMesh.addVertices(w, 0).addTexCoord2f(tw, 0f).addColor(Color4f.WHITE);
		fontMesh.addVertices(w, h).addTexCoord2f(tw, th).addColor(Color4f.WHITE);
		fontMesh.addVertices(0, h).addTexCoord2f(0f, th).addColor(Color4f.WHITE);
		fontMesh.buffering();
	}
	
	public static void drawString(Shaders shader, String txt, Vec2 pos, Color4f color)
	{
		font.bind();
		float w = font.getWidth() / 10f; 
		float h = font.getHeight() / 10f;
		float tw = w / (float)font.getWidth(); 
		float th = h / (float)font.getHeight();
		shader.setUniformMat4f("m_offset", Mat4.identity());
		shader.setUniform2f("mulTexture", new Vec2(1, 1));
		shader.setUniform4f("color", color.toVector4());
		Vec2 cursor = new Vec2();
		for (int i = 0; i <txt.length(); i++)
		{
			char c = txt.charAt(i);
			if (c == ' ')
			{
				cursor.x += w;
				continue;
			}
			if (c == '\n')
			{
				cursor.x = 0;
				cursor.y += h;
				continue;
			}
			int id = fontArray.indexOf(c);
			if (id < 0)
				continue;
			Vec2 offset = new Vec2((id % 10) * tw, (id / 10) * th);
			shader.setUniformMat4f("m_view", Mat4.translate(pos.copy().add(cursor)));
			shader.setUniform2f("offsetTexture", offset);
			fontMesh.render(GL11.GL_QUADS);
			cursor.x += w;
		}
		shader.setUniform2f("offsetTexture", new Vec2());
	}
	
	public static void drawString(Shaders shader, String txt, Vec2 pos, Color4f color, float scale)
	{
		font.bind();
		float w = font.getWidth() / 10f; 
		float h = font.getHeight() / 10f;
		float tw = w / (float)font.getWidth(); 
		float th = h / (float)font.getHeight();
		shader.setUniformMat4f("m_offset", Mat4.scale(scale));
		shader.setUniform2f("mulTexture", new Vec2(1, 1));
		shader.setUniform4f("color", color.toVector4());
		Vec2 cursor = new Vec2();
		for (int i = 0; i <txt.length(); i++)
		{
			char c = txt.charAt(i);
			if (c == ' ')
			{
				cursor.x += w;
				continue;
			}
			if (c == '\n')
			{
				cursor.x = 0;
				cursor.y += h;
				continue;
			}
			int id = fontArray.indexOf(c);
			if (id < 0)
				continue;
			Vec2 offset = new Vec2((id % 10) * tw, (id / 10) * th);
			shader.setUniformMat4f("m_view", Mat4.translate(pos.copy().add(cursor)));
			shader.setUniform2f("offsetTexture", offset);
			fontMesh.render(GL11.GL_QUADS);
			cursor.x += w;
		}
		shader.setUniform2f("offsetTexture", new Vec2());
	}
	
	public static Vec2 metricString(String txt)
	{
		float w = font.getWidth() / 10f; 
		float h = font.getHeight() / 10f;
		Vec2 cursor = new Vec2();
		Vec2 metric = new Vec2(0, 0);
		for (int i = 0; i <txt.length(); i++)
		{
			char c = txt.charAt(i);
			if (c == ' ')
			{
				cursor.x += w;
				metric.x = Math.max(metric.x, cursor.x);
				continue;
			}
			if (c == '\n')
			{
				cursor.x = 0;
				cursor.y += h;
				metric.y = Math.max(metric.y, cursor.y);
				continue;
			}
			int id = fontArray.indexOf(c);
			if (id < 0)
				continue;
			cursor.x += w;
			metric.x = Math.max(metric.x, cursor.x);
		}
		if (metric.x > 0)
			metric.y += h;
		return (metric);
	}
}
