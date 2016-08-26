package fr.mimus.jbasicgl.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import fr.mimus.jbasicgl.utils.DataBuffer;
import fr.mimus.jbasicgl.utils.IDisposable;
import fr.mimus.jbasicgl.utils.MemoryClass;
import static org.lwjgl.opengl.GL11.*;

/**
 * Class de gestion des textures, permet de les charger et les utiliser simplement.
 * @author Mimus
 * @version 1.0b
 */
public class Texture implements IDisposable
{
	private int index;
	private int width, height;
	private int[] pixels = null;
	
	/**
	 * Créer une texture a partir d'un chemin, si celci est introuvable créer une image d'erreur.
	 * @param path Chemin vers une image.
	 * @param nearest Defini le lissage de la texture
	 * @return Retourne la texture créer
	 */
	public static Texture FileTexture(String path, boolean nearest)
	{
		try
		{
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			return (new Texture(image, nearest));
		} 
		catch (IOException e) 
		{
			System.err.println("Error TEXTURE: Can't load texture: "+path);
		}
		return (new Texture(errorTexture(), nearest));
	}

	/**
	 * Créer une texture d'erreur.
	 * @return BufferedImage de l'image d'erreur.
	 */
	private static BufferedImage errorTexture()
	{
		BufferedImage buff = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.createGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 64, 64);
			g.setColor(Color.pink);
			g.fillRect(0, 0, 32, 32);
			g.fillRect(32, 32, 64, 64);
		g.dispose();
		return (buff);
	}
	
	/**
	 * Créer une texture a partir d'un BufferedImage
	 * @param image BufferedImage a convertir en texture opengl
	 * @param nearest Defini le lissage de la texture
	 */
	public Texture(BufferedImage image, boolean nearest)
	{
		width = image.getWidth();
		height = image.getHeight();
		pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		int[] data = new int[width*height];
		for(int i=0; i<data.length; i++) 
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);

		if (nearest)
		{
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
		else
		{
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}

		IntBuffer buffer = DataBuffer.createIntBuffer(data);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		this.index = id;
		glBindTexture(GL_TEXTURE_2D, 0);
		MemoryClass.addClass(this);
	}

	/**
	 * @return retourne l'index de la texture.
	 */
	public int getIndex()
	{
		return (index);
	}
	
	/**
	 * Active l'utilisation de la textures.
	 */
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, index);
	}

	/**
	 * Desactive l'utilisation des textures.
	 */
	public static void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Detruit la textures.
	 */
	public void dispose()
	{
		glDeleteTextures(index);
	}

	/**
	 * @return Largeur de la texture.
	 */
	public int getWidth()
	{
		return (width);
	}
	
	/**
	 * @return Hauteur de la texture.
	 */
	public int getHeight()
	{
		return (height);
	}
}
