package fr.ld36.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.mimus.jbasicgl.graphics.Texture;


public class Res {
	public static HashMap<String, Texture> images = new HashMap<String, Texture>();
	
	
	public static void autoLoadRsc() {
		File imgDir = new File("rsc/images");
		sortFiles(FileUtils.getRecursiveFiles(imgDir), "tex", "png", "jpg");
		
		System.out.println(images.size() + " images loaded");
		for (Entry<String, Texture> entry : images.entrySet())
		{
			System.out.println(" - " + entry.getKey());
		}
	}
		
	private static void sortFiles(File[] files, String type, String... args){
		if(files!=null){
			for(File file : files){
				String[] split = file.getName().split("\\.(?=[^\\.]+$)");
				if(Arrays.asList(args).contains(split[1])){
					File url = file;
					switch(type){
						case "tex": 
							if(url != null)
								images.put(split[0], Texture.FileTexture(url.getAbsolutePath(), true));
							break;
					}
				}
			}
		}
	}
}
