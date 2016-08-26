package fr.mimus.jbasicgl.input;

import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Class de gestion du clavier
 * @author Mimus
 * @version 1.0b
 */
public class Keyboard extends GLFWKeyCallback
{
	public final static int KEY_UNKNOWN			= -1;
	public final static int KEY_SPACE			= 32;
	public final static int KEY_APOSTROPHE		= 39; /* ' */
	public final static int KEY_COMMA			= 44; /* , */
	public final static int KEY_MINUS			= 45; /* - */
	public final static int KEY_PERIOD			= 46; /* . */
	public final static int KEY_SLASH			= 47; /* / */
	public final static int KEY_0				= 48;
	public final static int KEY_1				= 49;
	public final static int KEY_2				= 50;
	public final static int KEY_3				= 51;
	public final static int KEY_4				= 52;
	public final static int KEY_5				= 53;
	public final static int KEY_6				= 54;
	public final static int KEY_7				= 55;
	public final static int KEY_8				= 56;
	public final static int KEY_9				= 57;
	public final static int KEY_SEMICOLON		= 59; /* ; */
	public final static int KEY_EQUAL			= 61; /* = */
	public final static int KEY_A				= 65;
	public final static int KEY_B				= 66;
	public final static int KEY_C				= 67;
	public final static int KEY_D				= 68;
	public final static int KEY_E				= 69;
	public final static int KEY_F				= 70;
	public final static int KEY_G				= 71;
	public final static int KEY_H				= 72;
	public final static int KEY_I				= 73;
	public final static int KEY_J				= 74;
	public final static int KEY_K				= 75;
	public final static int KEY_L				= 76;
	public final static int KEY_M				= 77;
	public final static int KEY_N				= 78;
	public final static int KEY_O				= 79;
	public final static int KEY_P				= 80;
	public final static int KEY_Q				= 81;
	public final static int KEY_R				= 82;
	public final static int KEY_S				= 83;
	public final static int KEY_T				= 84;
	public final static int KEY_U				= 85;
	public final static int KEY_V				= 86;
	public final static int KEY_W				= 87;
	public final static int KEY_X				= 88;
	public final static int KEY_Y				= 89;
	public final static int KEY_Z				= 90;
	public final static int KEY_LEFT_BRACKET	= 91; /* [ */
	public final static int KEY_BACKSLASH		= 92; /* \ */
	public final static int KEY_RIGHT_BRACKET	= 93; /* ] */
	public final static int KEY_GRAVE_ACCENT	= 96; /* ` */
	public final static int KEY_WORLD_1			= 161; /* non-US #1 */
	public final static int KEY_WORLD_2			= 162; /* non-US #2 */
	public final static int KEY_ESCAPE			= 256;
	public final static int KEY_ENTER			= 257;
	public final static int KEY_TAB				= 258;
	public final static int KEY_BACKSPACE		= 259;
	public final static int KEY_INSERT			= 260;
	public final static int KEY_DELETE			= 261;
	public final static int KEY_RIGHT			= 262;
	public final static int KEY_LEFT			= 263;
	public final static int KEY_DOWN			= 264;
	public final static int KEY_UP				= 265;
	public final static int KEY_PAGE_UP			= 266;
	public final static int KEY_PAGE_DOWN		= 267;
	public final static int KEY_HOME			= 268;
	public final static int KEY_END				= 269;
	public final static int KEY_CAPS_LOCK		= 280;
	public final static int KEY_SCROLL_LOCK		= 281;
	public final static int KEY_NUM_LOCK		= 282;
	public final static int KEY_PRINT_SCREEN	= 283;
	public final static int KEY_PAUSE			= 284;
	public final static int KEY_F1				= 290;
	public final static int KEY_F2				= 291;
	public final static int KEY_F3				= 292;
	public final static int KEY_F4				= 293;
	public final static int KEY_F5				= 294;
	public final static int KEY_F6				= 295;
	public final static int KEY_F7				= 296;
	public final static int KEY_F8				= 297;
	public final static int KEY_F9				= 298;
	public final static int KEY_F10				= 299;
	public final static int KEY_F11				= 300;
	public final static int KEY_F12				= 301;
	public final static int KEY_F13				= 302;
	public final static int KEY_F14				= 303;
	public final static int KEY_F15				= 304;
	public final static int KEY_F16				= 305;
	public final static int KEY_F17				= 306;
	public final static int KEY_F18				= 307;
	public final static int KEY_F19				= 308;
	public final static int KEY_F20				= 309;
	public final static int KEY_F21				= 310;
	public final static int KEY_F22				= 311;
	public final static int KEY_F23				= 312;
	public final static int KEY_F24				= 313;
	public final static int KEY_F25				= 314;
	public final static int KEY_NUMPAD_0		= 320;
	public final static int KEY_NUMPAD_1		= 321;
	public final static int KEY_NUMPAD_2		= 322;
	public final static int KEY_NUMPAD_3		= 323;
	public final static int KEY_NUMPAD_4		= 324;
	public final static int KEY_NUMPAD_5		= 325;
	public final static int KEY_NUMPAD_6		= 326;
	public final static int KEY_NUMPAD_7		= 327;
	public final static int KEY_NUMPAD_8		= 328;
	public final static int KEY_NUMPAD_9		= 329;
	public final static int KEY_NUMPAD_DECIMAL	= 330;
	public final static int KEY_NUMPAD_DIVIDE	= 331;
	public final static int KEY_NUMPAD_MULTIPLY	= 332;
	public final static int KEY_NUMPAD_SUBTRACT	= 333;
	public final static int KEY_NUMPAD_ADD		= 334;
	public final static int KEY_NUMPAD_ENTER	= 335;
	public final static int KEY_NUMPAD_EQUAL	= 336;
	public final static int KEY_LEFT_SHIFT		= 340;
	public final static int KEY_LEFT_CONTROL	= 341;
	public final static int KEY_LEFT_ALT		= 342;
	public final static int KEY_LEFT_SUPER		= 343;
	public final static int KEY_RIGHT_SHIFT		= 344;
	public final static int KEY_RIGHT_CONTROL	= 345;
	public final static int KEY_RIGHT_ALT		= 346;
	public final static int KEY_RIGHT_SUPER		= 347;
	public final static int KEY_MENU			= 348;
	public final static int KEY_LAST			= KEY_MENU;

	private boolean keyDown[]	= new boolean[65536]; 
	private boolean keyPress[]	= new boolean[65536]; 
	
	private int		mods		= 0;
	private char	lastKeyChar	= 0;
	
	/**
	 * Savoir si une touche est appuyé.
	 * @param key Numéro de la touche du clavier
	 * @return True si elle est enfoncé, sinon false
	 */
	public boolean isDown(int key)
	{
		if (key >= 0 && key < keyDown.length)
			return (keyDown[key] || keyPress[key]);
		return (false);
	}

	/**
	 * Savoir si une touche est pressé.
	 * @param key Numéro de la touche du clavier
	 * @return True si elle est pressé, sinon false
	 */
	public boolean isPress(int key)
	{
		if (key >= 0 && key < keyDown.length)
			if (keyPress[key] && !keyDown[key])
			{
				keyPress[key] = false;
				return (true);
			}
		return (false);
	}
	
	/**
	 * @return Le dernier caractère appuyer, attention est remit à 0 quand appeler.
	 */
	public char getKeyChar()
	{
		char c = lastKeyChar;
		lastKeyChar = 0;
		return (c);
	}
	
	/**
	 * Redefini le dernier caractère appuyer
	 * @param c Caractère
	 */
	public void setKeyChar(char c)
	{
		lastKeyChar = c;
	}
	
	/**
	 * retourne l'index du mod utiliser (Shift...)
	 * @return mod
	 */
	public int getMods()
	{
		return (mods);
	}
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key < 0 || key >= 65536)
			return ;
		keyPress[key] = (action == 1);
		keyDown[key] = (action == 2);
		this.mods = mods;
	}
}
