import java.io.File;

import fr.mimus.jbasicgl.Window;
import fr.mimus.jbasicgl.graphics.Shaders;

public class LD36
{
	public static final int		WINDOW_WIDTH = 720;
	public static final double	NS_PER_SECOND = 1000000000.0f;
	public static final double	NS_PER_TICK = NS_PER_SECOND / 60.0f;
	
	Window win;
	public LD36()
	{
		win = new Window("LD36", 720, 720 * 9 / 16, true);
		win.create();
		win.sync(false);
	}
	
	private void loop()
	{
		long time = System.nanoTime();
		long timer = System.currentTimeMillis();
		int tick = 0;
		int frame = 0;
		while(win != null && !win.isClose())
		{
			long now = System.nanoTime();
			long elaspse = now - time;
			if (elaspse >= NS_PER_TICK)
			{
				time += NS_PER_TICK;
				update(tick);
				tick++;
			}
			else
			{
				render();
				Shaders.unbind();
				frame++;
			}
			win.update();
			
			now = System.currentTimeMillis();
			elaspse = now - timer;
			if (elaspse >= 1000)
			{
				timer += 1000;
				System.out.println("FPS: " + frame + ", UPS: " + tick);
				tick = 0;
				frame = 0;
			}
		}
		win.destroy();
	}
	
	private void render()
	{
		
	}
	
	private void update(int tick)
	{
		
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
