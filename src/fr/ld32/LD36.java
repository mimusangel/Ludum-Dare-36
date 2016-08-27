package fr.ld32;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.File;

import fr.mimus.jbasicgl.Window;
import fr.mimus.jbasicgl.graphics.Shaders;

public class LD36
{
	public static final int		WINDOW_WIDTH = 720;
	public static final double	NS_PER_SECOND = 1000000000.0f;
	public static final double	NS_PER_TICK = NS_PER_SECOND / 60.0f;
	
	public Window win;
	public Game game;
	
	public LD36()
	{
		win = new Window("LD36", 720, 720 * 9 / 16, true);
		win.create();
		win.sync(false);
		init();
	}
	
	private void init()
	{
		game = new Game();
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
				game.update(tick, (double)elapse / NS_PER_SECOND);
				tick++;
			}
			else
			{
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glClear(GL_COLOR_BUFFER_BIT);
				game.render((double)(now - lastRenderTime) / NS_PER_SECOND);
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

}