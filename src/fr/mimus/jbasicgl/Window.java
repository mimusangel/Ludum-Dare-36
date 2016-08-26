package fr.mimus.jbasicgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import fr.mimus.jbasicgl.event.Resize;
import fr.mimus.jbasicgl.input.Keyboard;
import fr.mimus.jbasicgl.input.Mouse;

/**
 * Class de gestion des fenêtres
 * @author Mimus
 * @version 1.0b
 */
public class Window
{
	private static GLFWErrorCallback	errorCallback = null;
	private static int					numberWindow = 0;

	private final Resize				resizeCallback;
    private final Keyboard				keyCallback;
    private final GLFWCharCallback		charCallBack;
    private Mouse						mouse;
    private GLCapabilities				capabilities;
    private long						window;
	
	private String						title;
	private int							width;
	private int							height;
	private boolean						enableRezise;
	
	/**
	 * Constructeur de fenêtre.
	 * @param title Titre de la fenêtre.
	 * @param width Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	public Window(String title, int width, int height)
	{
		this(title, width, height, true);
	}
	
	/**
	 * Constructeur de fenêtre.
	 * @param title Titre de la fenêtre.
	 * @param width Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 * @param enableRezise La fenêtre est tel redimensionnable
	 */
	public Window(String title, int width, int height, boolean enableRezise)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		this.enableRezise = enableRezise;
		this.resizeCallback = new Resize(this);
        this.keyCallback = new Keyboard();
        charCallBack = new GLFWCharCallback() {
			public void invoke(long window, int codepoint) {
				keyCallback.setKeyChar((char)codepoint);
			}
        };
	}
	/**
	 * Permet de créer la fenêtre une fois initialiser.
	 * @return elle même.
	 */
	public Window create()
	{
		if (errorCallback == null)
			glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		if (glfwInit() != GLFW_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        if (this.enableRezise)
        	glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        else
        	glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        this.window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetFramebufferSizeCallback(this.window, resizeCallback);
        glfwSetKeyCallback(this.window, this.keyCallback);
        glfwSetCharCallback(this.window, charCallBack);
        this.mouse = new Mouse(this.window);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(this.window, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);
        glfwMakeContextCurrent(this.window);
        this.capabilities = GL.createCapabilities();
		GL.setCapabilities(capabilities);
        glfwShowWindow(this.window);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        numberWindow++;
		return (this);
	}
	/**
	 * @return Largeur de la fenêtre.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return Hauteur de la fenêtre.
	 */
	public int getHeight() {
		return height;
	}
	
	public void setSize(int w, int h)
	{
		width = w;
		height = h;
	}
	
	public void rezise(int w, int h)
	{
		setSize(w, h);
		glfwSetWindowSize(this.window, width, height);
		GL11.glViewport(0, 0, width, height);
	}
	
	public float getAspect()
	{
		return ((float)width / (float)height);
	}

	/**
	 * Permet de mettre ou non la synchronization vertical.
	 * @param sync activer / desactiver
	 * @return elle même;
	 */
	public Window sync(boolean sync)
	{
		glfwSwapInterval(sync ? 1 : 0);
		return (this);
	}
	
	/**
	 * @return La fenêtre est telle fermé? oui ou non
	 */
	public boolean isClose()
	{
		return (glfwWindowShouldClose(window) == GLFW_TRUE);
	}
	
	/**
	 * Permet de selectionner la fenetre ou l'on va dessiner.
	 */
	public void selectRendering()
	{
		glfwMakeContextCurrent(window);
		GL.setCapabilities(capabilities);
	}
	
	/**
	 * Permet de mettre à jour la fenêtre.
	 */
	public void update()
	{
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	/**
	 * Donne l'acces au clavier utiliser pour la fenêtre.
	 * @return Class du clavier.
	 */
	public Keyboard getKeyboard() {
		return keyCallback;
	}
	
	/**
	 * Donne l'acces a la souris utiliser pour la fenêtre.
	 * @return Class du souris.
	 */
	public Mouse getMouse()
	{
		return (mouse);
	}
	
	/**
	 * Permet de detruire la fenêtre
	 */
	public void destroy()
	{
		numberWindow--;
		glfwDestroyWindow(this.window);
		this.keyCallback.release();
        this.mouse.destroy();
        if (numberWindow <= 0)
        {
        	glfwTerminate();
        	errorCallback.release();
        	errorCallback = null;
        }
	}
}
