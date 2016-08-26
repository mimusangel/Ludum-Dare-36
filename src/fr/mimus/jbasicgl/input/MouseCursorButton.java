package fr.mimus.jbasicgl.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Class utiliser pour la classe mouse.
 * @author Mimus
 * @version 1.0b
 */
public class MouseCursorButton extends GLFWMouseButtonCallback
{
	private boolean buttonDown[] = new boolean[Mouse.MOUSE_BUTTON_LAST];
	
	public boolean isDown(int button)
	{
		if (button >= 0 && button < Mouse.MOUSE_BUTTON_LAST)
			return (buttonDown[button]);
		return (false);
	}
	public void invoke(long window, int button, int action, int mods) {
		buttonDown[button] = (action != 0) ? true : false;
	}
}
