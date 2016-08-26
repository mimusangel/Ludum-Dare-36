package fr.mimus.jbasicgl.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;


/**
 * Class utiliser pour la classe mouse.
 * @author Mimus
 * @version 1.0b
 */
public class MouseCursorPos extends GLFWCursorPosCallback
{
	private double				mouseX = 0;
	private double				mouseY = 0;
	private double				mouseDX = 0;
	private double				mouseDY = 0;
	private boolean				grabbed = false;
    
	public double getX()
	{
		return (mouseX);
	}
	
	public double getY()
	{
		return (mouseY);
	}
	
	public double getDX()
	{
		double ret = mouseDX;
		mouseDX = 0;
		return (ret);
	}
	
	public double getDY()
	{
		double ret = mouseDY;
		mouseDY = 0;
		return (ret);
	}
	
	public void invoke(long window, double xpos, double ypos)
	{
		if (isGrabbed())
		{
			mouseDX = xpos - mouseX;
			mouseDY = ypos - mouseY;
		}
		mouseX = xpos;
		mouseY = ypos;
	}

	public boolean isGrabbed()
	{
		return grabbed;
	}

	public void setGrabbed(long window, boolean grabbed)
	{
		this.grabbed = grabbed;
		if (this.isGrabbed())
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		else
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
}
