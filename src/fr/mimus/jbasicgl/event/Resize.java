package fr.mimus.jbasicgl.event;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL11;

import fr.mimus.jbasicgl.Window;

public class Resize extends GLFWFramebufferSizeCallback 
{
	Window win;
	public Resize(Window w)
	{
		win = w;
	}
	
	public void invoke(long windowID, int width, int height)
	{
		win.setSize(width, height);
		GL11.glViewport(0, 0, width, height);
	}
}
