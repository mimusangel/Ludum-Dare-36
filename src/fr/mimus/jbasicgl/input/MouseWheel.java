package fr.mimus.jbasicgl.input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Class utiliser pour la classe mouse.
 * @author Mimus
 * @version 1.0b
 */
public class MouseWheel extends GLFWScrollCallback
{
	private double mouseWheelX = 0;
	private double mouseWheelY = 0;
	
	public double getWheelX()
	{
		double tmp = mouseWheelX;
		mouseWheelX = 0;
		return tmp;
	}
	
	public double getWheelY()
	{
		double tmp = mouseWheelY;
		mouseWheelY = 0;
		return tmp;
	}
	
	public void invoke(long window, double xoffset, double yoffset) {
		mouseWheelX = xoffset;
		mouseWheelY = yoffset;
	}

}
