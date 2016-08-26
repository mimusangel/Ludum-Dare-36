package fr.mimus.jbasicgl.graphics;

import java.util.Random;

import fr.mimus.jbasicgl.maths.Vec4;

/**
 * Classe pour gérer les couleurs
 * @author Mimus
 * @version 1.0b
 */
public class Color4f
{
	public static Color4f WHITE = new Color4f(1f,1f,1f,1f);
	public static Color4f BLACK = new Color4f(0f,0f,0f,1f);
	public static Color4f GRAY = new Color4f(.5f,.5f,.5f,1f);
	public static Color4f DARK_GRAY = new Color4f(.25f,.25f,.25f,1f);
	public static Color4f LIGHT_GRAY = new Color4f(.75f,.75f,.75f,1f);
	
	public static Color4f RED = new Color4f(1f,0f,0f,1f);
	public static Color4f LIGHT_RED = new Color4f(1f,.5f,.5f,1f);
	public static Color4f DARK_RED = new Color4f(.25f,0f,0f,1f);
	
	public static Color4f GREEN = new Color4f(0f,1f,0f,1f);
	public static Color4f LIGHT_GREEN = new Color4f(.5f,1f,.5f,1f);
	public static Color4f DARK_GREEN = new Color4f(0f,.25f,0f,1f);
	
	public static Color4f BLUE = new Color4f(0f,0f,1f,1f);
	public static Color4f LIGHT_BLUE = new Color4f(.5f,.5f,1,1f);
	public static Color4f DARK_BLUE = new Color4f(0f,0f,.25f,1f);
	
	public static Color4f CYAN = new Color4f(0f,1f,1f,1f);
	public static Color4f LIGHT_CYAN = new Color4f(.5f,1f,1f,1f);
	public static Color4f DARK_CYAN = new Color4f(0f,.5f,.5f,1f);
	public static Color4f PINK = new Color4f(1f,0f,1f,1f);
	public static Color4f LIGHT_PINK = new Color4f(1f,.5f,1f,1f);
	public static Color4f DARK_PINK = new Color4f(.5f,0f,.5f,1f);
	public static Color4f YELLOW = new Color4f(1f,1f,0f,1f);
	public static Color4f LIGHT_YELLOW = new Color4f(1f,1f,.5f,1f);
	public static Color4f DARK_YELLOW = new Color4f(.5f,.5f,0f,1f);
	public static Color4f ORANGE = new Color4f(1f,.5f,0f,1f);
	public static Color4f LIGHT_ORANGE = new Color4f(1f,.75f,.5f,1f);
	public static Color4f DARK_ORANGE = new Color4f(.5f,.25f,0f,1f);
	public static Color4f PURPLE = new Color4f(.5f,0f,1f,1f);
	public static Color4f LIGHT_PURPLE = new Color4f(.75f,.5f,1f,1f);
	public static Color4f DARK_PURPLE = new Color4f(.25f,0f,.5f,1f);
	
	/**
	 * Donne une couleur alléatoir.
	 * @return Une couleur aléatoir.
	 */
	public static Color4f randomColor()
	{
		Random rand = new Random();
		
		return (new Color4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
	}
	
	public float red;
	public float green;
	public float blue;
	public float alpha;
	/**
	 * Copie une couleur existante
	 * @param c Class de couleur
	 */
	public Color4f(Color4f c)
	{
		this(c.red, c.green, c.blue, c.alpha);
	}
	
	/**
	 * Convertit une couleur hexa en class de couleur.
	 * @param hexa couleur sous forme d'hexa
	 */
	public Color4f(int hexa)
	{
		this((hexa & 0xff000000) >> 24, (hexa & 0xff0000) >> 16, (hexa & 0xff00) >> 8, (hexa & 0xff));		
	}
	
	/**
	 * Créer une couleur a partir de paramêtre Rouge Vert Bleu.
	 * @param r Rouge
	 * @param g Vert
	 * @param b Bleu
	 */
	public Color4f(int r, int g, int b)
	{
		this((float)r / 255f, (float)g / 255f, (float)b / 255f, 1f);
	}
	/**
	 * Créer une couleur a partir de paramêtre Rouge Vert Bleu Alpha.
	 * @param r Rouge
	 * @param g Vert
	 * @param b Bleu
	 * @param a Alpha
	 */
	public Color4f(int r, int g, int b, int a)
	{
		this((float)r / 255f, (float)g / 255f, (float)b / 255f, a / 255f);
	}
	
	/**
	 * Créer une couleur a partir de paramêtre Rouge Vert Bleu normaliser.
	 * @param r Rouge
	 * @param g Vert
	 * @param b Bleu
	 */
	public Color4f(float r, float g, float b)
	{
		this(r, g, b, 1f);
	}
	
	/**
	 * Créer une couleur a partir de paramêtre Rouge Vert Bleu alpha normaliser.
	 * @param r Rouge
	 * @param g Vert
	 * @param b Bleu
	 * @param a Alpha
	 */
	public Color4f(float r, float g, float b, float a)
	{
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	/**
	 * @return Couleur rouge normaliser.
	 */
	public float getRed()
	{
		return (red);
	}

	/**
	 * Defini la couleur rouge.
	 * @param red Rouge normaliser
	 * @return Elle même.
	 */
	public Color4f setRed(float red)
	{
		this.red = red;
		return (this);
	}
	
	/**
	 * @return Couleur verte normaliser.
	 */
	public float getGreen()
	{
		return (green);
	}

	/**
	 * Defini la couleur Verte.
	 * @param green Verte normaliser
	 * @return Elle même.
	 */
	public Color4f setGreen(float green)
	{
		this.green = green;
		return (this);
	}
	
	/**
	 * @return Couleur bleu normaliser.
	 */
	public float getBlue()
	{
		return (blue);
	}

	/**
	 * Defini la couleur Bleu.
	 * @param blue Bleu normaliser
	 * @return Elle même.
	 */
	public Color4f setBlue(float blue)
	{
		this.blue = blue;
		return (this);
	}
	
	/**
	 * @return Alpha normaliser.
	 */
	public float getAlpha()
	{
		return (alpha);
	}
	
	/**
	 * Defini l'Alpha.
	 * @param alpha Alpha normaliser
	 * @return Elle même.
	 */
	public Color4f setAlpha(float alpha)
	{
		this.alpha = alpha;
		return (this);
	}
	
	/**
	 * Multiplie la couleur par une valeur normaliser.
	 * @param v Valeur normaliser
	 * @return Elle même.
	 */
	public Color4f mul(float v)
	{
		red *= v;
		green *= v;
		blue *= v;
		return (this);
	}
	/**
	 * Multiplie la class par un autre class de couleur.
	 * @param v Class de couleur
	 * @return Elle même.
	 */
	public Color4f mul(Color4f v) {
		red *= v.red;
		green *= v.green;
		blue *= v.blue;
		return (this);
	}
	
	/**
	 * Convertit la class en tableau de float.
	 * @return tableau de couleur [Rouge, Vert, Bleu, Alpha]
	 */
	public float[] toArray() {
		return (new float[] {red, green, blue, alpha});
	}
	
	public Vec4 toVector4()
	{
		return (new Vec4(red, green, blue, alpha));
	}
}
