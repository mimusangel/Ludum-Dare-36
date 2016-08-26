package fr.mimus.jbasicgl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Utilitaire de fichier
 * @author Mimus
 * @version 1.0b
 */
public class FileUtils
{
	private FileUtils() {}
	
	/**
	 * Permet de charger un fichier dans un string.
	 * @param path url d'un fichier
	 * @return contenu du fichier sous format d'une variable String
	 */
	public static String loadToString(String path) {
		StringBuilder txt = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				txt.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return txt.toString();
	}

	/**
	 * Permet de créer un filtre pour les images.
	 * .png .jpg .jpeg .gif .bmp .jpe .jfif
	 * Exemple: "new File("./Directory").list(filter);"
	 * @author Mimus
	 * @version 1.0
	 */
	public class FileImageFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			String lowercaseName = name.toLowerCase();
			if (lowercaseName.endsWith(".png")
				 || lowercaseName.endsWith(".jpg")
				 || lowercaseName.endsWith(".jpeg")
				 || lowercaseName.endsWith(".gif")
				 || lowercaseName.endsWith(".bmp")
				 || lowercaseName.endsWith(".jpe")
				 || lowercaseName.endsWith(".jfif")) {
				return true;
			}
			return false;
		}

	}
}
