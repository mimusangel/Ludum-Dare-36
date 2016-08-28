package fr.ld36;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;

import fr.ld36.render.Renderer;
import fr.ld36.utils.Res;
import fr.mimus.jbasicgl.Window;
import fr.mimus.jbasicgl.graphics.Color4f;
import fr.mimus.jbasicgl.graphics.Shaders;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;
import fr.mimus.jbasicgl.utils.MemoryClass;

public class LD36
{
	public static final int		WINDOW_WIDTH = 720;
	private static final double	NS_PER_SECOND = 1000000000.0f;
	private static final double	NS_PER_TICK = NS_PER_SECOND / 60.0f;
	
	public Window win;
	public Game game;
	Shaders hud;
	public LD36()
	{
		win = new Window("LD36 - Ancient Technology", WINDOW_WIDTH, WINDOW_WIDTH * 9 / 16, true);
		win.create();
		win.sync(false);
		init();
	}
	
	private void init()
	{
		Res.autoLoadRsc();
		Renderer.load();
		game = new Game();
		hud = new Shaders("rsc/shaders/main.vert", "rsc/shaders/hud.frag");
	}
	
	private void loop()
	{
		long time = System.nanoTime();
		long timer = System.currentTimeMillis();
		long lastRenderTime = System.nanoTime();
		int tick = 0;
		int frame = 0;
		while(win != null && !win.isClose())
		{
			long now = System.nanoTime();
			long elapse = now - time;
			if (elapse >= NS_PER_TICK)
			{
				time += NS_PER_TICK;
				if (game != null)
					game.update(tick, (double)elapse / NS_PER_SECOND);
				else
					updateGameOver();
				tick++;
			}
			else
			{
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glClear(GL_COLOR_BUFFER_BIT);
				if (game != null)
					game.render((double)(now - lastRenderTime) / NS_PER_SECOND);
				else
					renderGameOver();
				Shaders.unbind();
				frame++;
				lastRenderTime = System.nanoTime();
			}
			win.update();
			
			now = System.currentTimeMillis();
			elapse = now - timer;
			if (elapse >= 1000)
			{
				timer += 1000;
				System.out.println("FPS: " + frame + ", UPS: " + tick);
				tick = 0;
				frame = 0;
			}
		}
		if (game != null)
			game.dispose();
		MemoryClass.clearAll();
		win.destroy();
	}
	
	private static LD36 instance;
	public static LD36 getInstance()
	{
		return (instance);
	}
	
	public static void main(String[] args)
	{
		System.setProperty("java.library.path", "lwjgl/jar");
		System.setProperty("org.lwjgl.librarypath", new File("lwjgl/native").getAbsolutePath());
		
		instance = new LD36();
		instance.loop();
	}
	
	public float getScaleX()
	{
		return (win.getWidth() / (float) WINDOW_WIDTH);
	}
	
	public float getScaleY()
	{
		float height = WINDOW_WIDTH * 9f / 16f;
		return (win.getHeight() / height);
	}
	
	int score;
	public void gameOver(int score)
	{
		this.score = score;
		game.dispose();
		game = null;
	}

	public void updateGameOver()
	{
		Keyboard k = win.getKeyboard();
		if (k.isPress(Keyboard.KEY_ENTER))
		{
			game = new Game();
		}
	}
	
	public void renderGameOver()
	{
		hud.bind();
		hud.setUniformMat4f("m_proj", Mat4.orthographic(0, 720 * 9 / 16, 720, 0, -1f, 1f));
		hud.setUniformMat4f("m_offset", Mat4.identity());
		hud.setUniform4f("color", Color4f.WHITE.toVector4());
		hud.setUniform2f("mulTexture", new Vec2(1, 1));
		hud.setUniform2f("offsetTexture", new Vec2());
		hud.setUniformMat4f("m_view", Mat4.identity());
		Vec2 size = Renderer.metricString("Score: " + score);
		size = new Vec2(360, 182).sub(size.div(2));
		size.x = (float) Math.floor(size.x);
		size.y = (float) Math.floor(size.y);
		Renderer.drawString(hud, "Score: " + score, size, Color4f.WHITE);
		size = Renderer.metricString("Press Enter for Retry");
		size = new Vec2(360, 232).sub(size.div(2));
		size.x = (float) Math.floor(size.x);
		size.y = (float) Math.floor(size.y);
		Renderer.drawString(hud, "Press Enter for Retry", size, Color4f.WHITE);
	}
}
