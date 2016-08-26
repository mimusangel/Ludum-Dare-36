package fr.mimus.jbasicgl.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Gestion de buffer
 * @author Mimus
 * @version 1.0b
 */
public class BufferUtils
{
	private BufferUtils() {}
	
	/**
	 * Permet d'allouer un buffer de Byte
	 * @param size Taille du buffer.
	 * @return Le buffer de Byte.
	 */
	public static ByteBuffer allocateByteBuffer(int size)
	{
		return (ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()));
	} 
	
	/**
	 * Ajout un tableau de Byte dans un nouveau Buffer
	 * @param array Tableau de Byte.
	 * @return Le buffer de Byte.
	 */
	public static ByteBuffer createByteBuffer(byte[] array)
	{
		ByteBuffer buff = allocateByteBuffer(array.length);
		
		buff.put(array).flip();
		return (buff);
	} 
	
	/**
	 * Permet d'allouer un buffer de Float
	 * @param size Taille du buffer.
	 * @return Le buffer de Float.
	 */
	public static FloatBuffer allocateFloatBuffer(int size)
	{
		return (ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asFloatBuffer());
	}
	
	/**
	 * Ajout un tableau de Float dans un nouveau Buffer
	 * @param array Tableau de Float.
	 * @return Le buffer de Float.
	 */
	public static FloatBuffer createFloatBuffer(float[] array)
	{
		FloatBuffer buff = allocateFloatBuffer(array.length);
		
		buff.put(array).flip();
		return (buff);
	}
	
	/**
	 * Permet d'allouer un buffer d'Integer
	 * @param size Taille du buffer.
	 * @return Le buffer d'Integer.
	 */
	public static IntBuffer allocateIntBuffer(int size)
	{
		return (ByteBuffer.allocateDirect(size << 2).order(ByteOrder.nativeOrder()).asIntBuffer());
	}
	
	/**
	 * Ajout un tableau d'Integer dans un nouveau Buffer
	 * @param array Tableau d'Integer.
	 * @return Le buffer d'Integer.
	 */
	public static IntBuffer createIntBuffer(int[] array)
	{
		IntBuffer buff = allocateIntBuffer(array.length);
		
		buff.put(array).flip();
		return (buff);
	}
}
