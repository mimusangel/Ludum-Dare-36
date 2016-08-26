package fr.mimus.jbasicgl.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.mimus.jbasicgl.maths.Triangle;
import fr.mimus.jbasicgl.maths.Vec2;
import fr.mimus.jbasicgl.maths.Vec3;
import fr.mimus.jbasicgl.utils.DataBuffer;
import fr.mimus.jbasicgl.utils.IDisposable;
import fr.mimus.jbasicgl.utils.MemoryClass;

/**
 * Créer une classe de VAO pour facilité le dessin,
 * attention tout le VAO on les VAO on les même buffer pour optimiser la memoire donc n'oublier pas de le compiler(buffering()) avant de passer au suivant.
 * @author Mimus
 * @version 1.2b
 */
public class Mesh implements IDisposable
{
	public final static int INDEX_VERTEX	= 0;
	public final static int INDEX_COLOR		= 1;
	public final static int INDEX_TEXCOORD	= 2;
	public final static int INDEX_NORMAL	= 3;
	
	private int vao;
	private int vbo;
	private int cbo;
	private int tbo;
	private int nbo;
	private int ibo;

	private static FloatBuffer 	verticesBuffer;
	private static FloatBuffer 	colorBuffer;
	private static FloatBuffer 	textureBuffer;
	private static FloatBuffer 	normalBuffer;
	private static IntBuffer 	indiceBuffer;
	
	private int size;
	private int countVertice;
	private int countIndice;
	private boolean indicesEnable;
	
	/**
	 * Initialise le VAO sans utiliser les index, il son donc desactiver.
	 * @param buffSize Nombre de vertice maximum que vous allez utiliser
	 */
	public Mesh(int buffSize)
	{
		this(buffSize, 0);
	}
	
	/**
	 * Initialise le VAO en utilisant des index, les index sont donc initialiser mais pas obligatoirement utilisable
	 * @param buffSize Nombre de vertice maximum que vous allez utiliser
	 * @param indiceSize Nombre d'index maximum que vous allez utiliser
	 */
	public Mesh(int buffSize, int indiceSize)
	{
		this.size = 0;
		this.countVertice = 0;
		this.countIndice = 0;
		indicesEnable = false;
		if (verticesBuffer == null || buffSize * 3 > verticesBuffer.capacity())
		{
			verticesBuffer = DataBuffer.allocateFloatBuffer(buffSize * 3);
			colorBuffer = DataBuffer.allocateFloatBuffer(buffSize * 4);
			textureBuffer = DataBuffer.allocateFloatBuffer(buffSize * 2);
			normalBuffer = DataBuffer.allocateFloatBuffer(buffSize * 3);
		}
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		cbo = glGenBuffers();
		tbo = glGenBuffers();
		nbo = glGenBuffers();
		if (indiceSize > 0)
		{
			if (indiceBuffer == null || indiceSize > indiceBuffer.capacity())
				indiceBuffer = DataBuffer.allocateIntBuffer(indiceSize);
			ibo = glGenBuffers();
		}
		MemoryClass.addClass(this);
	}
	
	/**
	 * Ajout un vertices au buffer
	 * @param vec Vecteur du vertices
	 * @return Lui même
	 */
	public Mesh addVertices(Vec2 vec)
	{
		return (addVertices(vec.x, vec.y, 0));
	}
	
	/**
	 * Ajout un vertices au buffer
	 * @param x Coordonnée X du vertices
	 * @param y Coordonnée Y du vertices
	 * @return Lui même
	 */
	public Mesh addVertices(float x, float y)
	{
		return (addVertices(x, y, 0));
	}
	
	/**
	 * Ajout un vertices au buffer
	 * @param vec Vecteur du vertices
	 * @return Lui même
	 */
	public Mesh addVertices(Vec3 vec)
	{
		return (addVertices(vec.x, vec.y, vec.z));
	}
	
	/**
	 * Ajout un vertices au buffer
	 * @param x Coordonnée X du vertices
	 * @param y Coordonnée Y du vertices
	 * @param z Coordonnée Z du vertices
	 * @return Lui même
	 */
	public Mesh addVertices(float x, float y, float z)
	{
		verticesBuffer.put(x).put(y).put(z);
		this.countVertice++;
		return (this);
	}
	
	/**
	 * Ajoute une couleur au buffer
	 * @param color Class de couleur
	 * @return Lui même
	 */
	public Mesh addColor(Color4f color)
	{
		return (addColor(color.red, color.green, color.blue, color.alpha));
	}
	
	/**
	 * Ajoute une couleur au buffer
	 * @param r Rouge
	 * @param g Vert
	 * @param b Bleu
	 * @param a Alpha
	 * @return Lui même
	 */
	public Mesh addColor(float r, float g, float b, float a)
	{
		colorBuffer.put(r).put(g).put(b).put(a);
		return (this);
	}
	
	/**
	 * Ajout les coordonnées de la texture.
	 * @param x Coordonnée X de ma texture
	 * @param y Coordonnée Y de la texture
	 * @return Lui même
	 */
	public Mesh addTexCoord2f(float x, float y)
	{
		textureBuffer.put(x).put(y);
		return (this);
	}
	
	public Mesh addTexCoord2f(Vec2 v)
	{
		return (addTexCoord2f(v.x , v.y));
	}
	
	/**
	 * Ajoute une normal au buffer
	 * @param vec Vecteur de la normal
	 * @return Lui même
	 */
	public Mesh addNormal(Vec3 vec)
	{
		return (addNormal(vec.x, vec.y, vec.z));
	}
	
	/**
	 * Ajoute une normal au buffer
	 * @param x Coordonnée X
	 * @param y Coordonnée Y
	 * @param z Coordonnée Z
	 * @return Lui même
	 */
	public Mesh addNormal(float x, float y, float z)
	{
		normalBuffer.put(x).put(y).put(z);
		return (this);
	}
	
	/**
	 * ajoute des index de vertex au buffer
	 * @param indices liste des index des vertex
	 * @return Lui même
	 */
	public Mesh addIndice(int...indices)
	{
		if (ibo > 0)
		{
			indiceBuffer.put(indices);
			this.countIndice += indices.length;
		}
		else
		{
			System.err.println("Indice buffer no enable, use Constructor: \"VAO(buffSize, indiceSize)\"");
		}
		return (this);
	}
	

	public Mesh addTriangle(Triangle t)
	{
		addVertices(t.getA());
		addColor(t.getColor());
		addNormal(t.getNormal());
		addTexCoord2f(t.getUvA());
		addVertices(t.getB());
		addColor(t.getColor());
		addNormal(t.getNormal());
		addTexCoord2f(t.getUvB());
		addVertices(t.getC());
		addColor(t.getColor());
		addNormal(t.getNormal());
		addTexCoord2f(t.getUvC());
		return (this);
	}
	
	/**
	 * Met vos buffers dans les buffers graphique, puis les nettoies.
	 */
	public void buffering()
	{
		if (vao <= 0)
		{
			System.err.println("This Model is deleted.");
			return ;
		}
		System.out.println("Buffering Model: ");
		System.out.println("Model Index:\t" + vao);
		System.out.println("- Vertices:\t" + verticesBuffer.position() + " (" + this.countVertice +")");
		
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		verticesBuffer.flip();
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(INDEX_VERTEX, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(INDEX_VERTEX);
		verticesBuffer.clear();
		
		if (colorBuffer.position() > 0)
		{
			System.out.println("- Color:\t" + colorBuffer.position());
			glBindBuffer(GL_ARRAY_BUFFER, cbo);
			colorBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(INDEX_COLOR, 4, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(INDEX_COLOR);
			colorBuffer.clear();
		}
	
		if (textureBuffer.position() > 0)
		{
			System.out.println("- TexCoord:\t" + textureBuffer.position());
			glBindBuffer(GL_ARRAY_BUFFER, tbo);
			textureBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(INDEX_TEXCOORD, 2, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(INDEX_TEXCOORD);
			textureBuffer.clear();
		}
		
		if (normalBuffer.position() > 0)
		{
			System.out.println("- Normal:\t" + normalBuffer.position());
			glBindBuffer(GL_ARRAY_BUFFER, nbo);
			normalBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(INDEX_NORMAL, 3, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(INDEX_NORMAL);
			normalBuffer.clear();
		}
		
		if (ibo > 0 && indiceBuffer.position() > 0)
		{
			System.out.println("- Indices:\t" + indiceBuffer.position());
			indiceBuffer.flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			indiceBuffer.clear();
			indicesEnable = true;
			this.size = this.countIndice;
		}
		else
		{
			this.size = this.countVertice;
			indicesEnable = false;
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		this.countVertice = 0;
		this.countIndice = 0;
		System.out.println("VAO added in GPU buffer. Memory cleared.");
	}

	/**
	 * affiche le vao, si vous avez mis en buffer graphique (buffering())
	 */
	public void render(int mode) {
		if (vao <= 0)
			return ;
		glBindVertexArray(vao);
		if (indicesEnable)
		{
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glDrawElements(mode, this.size, GL_UNSIGNED_INT, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		else
			glDrawArrays(mode, 0, this.size);
		glBindVertexArray(0);
	}
	
	/**
	 * Nettoie les buffers
	 */
	public void clearAll()
	{
		verticesBuffer.clear();
		colorBuffer.clear();
		textureBuffer.clear();
		normalBuffer.clear();
		if (indiceBuffer != null)
		{
			indiceBuffer.clear();
		}
	}
	
	/**
	 * Nettoie les buffers et supprime les buffers graphique.
	 */
	public void dispose()
	{
		verticesBuffer.clear();
		colorBuffer.clear();
		textureBuffer.clear();
		normalBuffer.clear();
		if (indiceBuffer != null)
		{
			indiceBuffer.clear();
			glDeleteBuffers(ibo);
		}
		glDeleteBuffers(nbo);
		glDeleteBuffers(tbo);
		glDeleteBuffers(cbo);
		glDeleteBuffers(vbo);
		glDeleteVertexArrays(vao);
		vao = 0;
		vbo = 0;
		cbo = 0;
		tbo = 0;
		nbo = 0;
	}
	
	/**
	 * Vous écris dans la console des informations qui peu vous être utile.
	 */
	public static void printInfo()
	{
		System.out.println("Vertex Attrib Array:\t" + INDEX_VERTEX);
		System.out.println("COLOR Attrib Array:\t" + INDEX_COLOR);
		System.out.println("TexCoord Attrib Array:\t" + INDEX_TEXCOORD);
		System.out.println("Normal Attrib Array:\t" + INDEX_NORMAL);
	}
}
