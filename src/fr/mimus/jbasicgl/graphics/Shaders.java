package fr.mimus.jbasicgl.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import fr.mimus.jbasicgl.maths.Mat4;
import fr.mimus.jbasicgl.maths.Vec2;
import fr.mimus.jbasicgl.maths.Vec3;
import fr.mimus.jbasicgl.maths.Vec4;
import fr.mimus.jbasicgl.utils.FileUtils;
import fr.mimus.jbasicgl.utils.IDisposable;
import fr.mimus.jbasicgl.utils.MemoryClass;

/**
 * Class de gestion des shaders, permet de les charger et les utiliser.
 * @author Mimus
 * @version 1.0b
 */
public class Shaders implements IDisposable
{
	public final static String NEW_LINE = System.getProperty("line.separator"); 
	public final static Shaders BASE = new Shaders("#version 330 core" + NEW_LINE
			+ "layout (location = 0) in vec3 in_position;" + NEW_LINE
			+ "layout (location = 1) in vec4 in_color;" + NEW_LINE
			+ "layout (location = 2) in vec2 in_texCoord;" + NEW_LINE
			+ "layout (location = 3) in vec3 in_normal;" + NEW_LINE
			+ "uniform mat4 m_proj;" + NEW_LINE
			+ "uniform mat4 m_view;" + NEW_LINE
			+ "uniform mat4 m_obj;" + NEW_LINE
			+ "out vec4 v_color;" + NEW_LINE
			+ "void main()" + NEW_LINE
			+ "{" + NEW_LINE
			+ "gl_Position = m_proj * m_view * m_obj * vec4(in_position, 1.0);" + NEW_LINE
			+ "v_color = in_color;}",
			"#version 330 core" + NEW_LINE
			+ "layout (location = 0) out vec4 out_color;" + NEW_LINE
			+ "in vec4 v_color;" + NEW_LINE
			+ "void main()" + NEW_LINE
			+ "{" + NEW_LINE
			+ "out_color = v_color;" + NEW_LINE
			+ "}", false); 
	
	private int index;
	
	/**
	 * Defini l'url pour charger le shaders
	 * @param vertPath Chemin du fichier Vertex Shader
	 * @param fragPath Chemin du fichier Fragment Shader
	 */
	public Shaders(String vertPath, String fragPath)
	{
		this(vertPath, fragPath, true);
	}
	
	public Shaders(String vert, String frag, boolean path)
	{
		if (path)
			index = load(vert, frag);
		else
			index = create(vert, frag);
		MemoryClass.addClass(this);
	}
	
	/**
	 * @return L'index du shader.
	 */
	public int getIndex()
	{
		return (index);
	}
	
	/**
	 * Cherche et renvoi l'index de l'uniform dans le shader
	 * @param name Nom de l'uniform
	 * @return Index de l'uniform
	 */
	public int getUniform(String name)
	{
		int uniformIndex = glGetUniformLocation(index, name);
		if (uniformIndex == -1) 
		{
			System.err.println("Could not find uniform variable '" + name + "'!");
		}
		return uniformIndex;
	}

	/**
	 * Défini l'uniform 'NAME' par une valeur Integer.
	 * @param name Nom de l'uniform
	 * @param val Valeur a lui assigner
	 */
	public void setUniform1i(String name, int val)
	{
		glUniform1i(getUniform(name), val);
	}

	/**
	 * Défini l'uniform 'NAME' par une valeur Float.
	 * @param name Nom de l'uniform
	 * @param val Valeur a lui assigner
	 */
	public void setUniform1f(String name, float val)
	{
		glUniform1f(getUniform(name), val);
	}
	
	/**
	 * Défini l'uniform 'NAME' par deux valeur Float.
	 * @param name Nom de l'uniform
	 * @param x Valeur 1 a lui assigner
	 * @param y Valeur 2 a lui assigner
	 */
	public void setUniform2f(String name, float x, float y)
	{
		glUniform2f(getUniform(name), x, y);
	}
	
	/**
	 * Défini l'uniform 'NAME' par un Vecteur2f.
	 * @param name Nom de l'uniform
	 * @param vec Vecteur a lui assigner
	 */
	public void setUniform2f(String name, Vec2 vec)
	{
		setUniform2f(name, vec.x, vec.y);
	}
	
	/**
	 * Défini l'uniform 'NAME' par troi valeur Float.
	 * @param name Nom de l'uniform
	 * @param x Valeur 1 a lui assigner
	 * @param y Valeur 2 a lui assigner
	 * @param z Valeur 3 a lui assigner
	 */
	public void setUniform3f(String name, float x, float y, float z)
	{
		glUniform3f(getUniform(name), x, y, z);
	}
	
	/**
	 * Défini l'uniform 'NAME' par un Vecteur3f.
	 * @param name Nom de l'uniform
	 * @param vec Vecteur a lui assigner
	 */
	public void setUniform3f(String name, Vec3 vec)
	{
		setUniform3f(name, vec.x, vec.y, vec.z);
	}
	
	/**
	 * Défini l'uniform 'NAME' par troi valeur Float.
	 * @param name Nom de l'uniform
	 * @param x Valeur 1 a lui assigner
	 * @param y Valeur 2 a lui assigner
	 * @param z Valeur 3 a lui assigner
	 * @param w Valeur 4 a lui assigner
	 */
	public void setUniform4f(String name, float x, float y, float z, float w)
	{
		glUniform4f(getUniform(name), x, y, z, w);
	}
	
	/**
	 * Défini l'uniform 'NAME' par un Vecteur4f.
	 * @param name Nom de l'uniform
	 * @param vec Vecteur a lui assigner
	 */
	public void setUniform4f(String name, Vec4 vec)
	{
		setUniform4f(name, vec.x, vec.y, vec.z, vec.w);
	}
	
	/**
	 * Défini l'uniform 'NAME' par une Matrice 4*4.
	 * @param name Nom de l'uniform
	 * @param matrix Matrice a lui assigner
	 */
	public void setUniformMat4f(String name, Mat4 matrix)
	{
		glUniformMatrix4fv(getUniform(name), false, matrix.toBuffer());
	}
	
	/**
	 * Active l'utilisation du shader
	 */
	public void bind()
	{
		glUseProgram(index);
	}
	
	/**
	 * Desactive les utilisations shaders
	 */
	public static void unbind()
	{
		glUseProgram(0);
	}
	
	/**
	 * Charge les shaders.
	 * @param vertPath Chemin du fichier Vertex Shader
	 * @param fragPath Chemin du fichier Fragment Shader
	 * @return L'index du shader
	 */
	public static int load(String vertPath, String fragPath)
	{
		String vert = FileUtils.loadToString(vertPath); 
		String frag = FileUtils.loadToString(fragPath);
		
		return create(vert, frag);
	}
	
	/**
	 * Créé le shader (Est Appeler dans le load);
	 * @param vert Code du Vertex Shader
	 * @param frag Code du Fragment Shader
	 * @return L'index du shader
	 */
	public static int create(String vert, String frag)
	{
		int program = glCreateProgram(); 
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);
		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertID));
			return (-1);
		}
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragID));
			return (-1);
		}
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		return (program);
	}

	public void dispose()
	{
		glDeleteProgram(index);
		index = 0;
	}
	
	public boolean isValid()
	{
		return (index != -1);
	}
}
