package fr.mimus.jbasicgl.utils;

import java.util.ArrayList;

public class MemoryClass
{
	private static ArrayList<IDisposable> classList = new ArrayList<IDisposable>();
	
	public static void addClass(IDisposable c)
	{
		classList.add(c);
	}
	
	public static void clear(IDisposable c)
	{
		for(IDisposable l:classList)
		{
			if (l == c)
			{
				l.dispose();
				break;
			}
		}
		classList.remove(c);
	}
	
	public static void clearAll()
	{
		for(IDisposable c:classList)
		{
			c.dispose();
		}
		classList.clear();
	}
}
