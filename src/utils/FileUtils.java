package utils;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {

	public static File[] getRecursiveFiles(File dir){
		return getRecursiveFiles(dir, new ArrayList<File>());
	}
	
	private static File[] getRecursiveFiles(File dir, ArrayList<File> fileList){
		File[] files = dir.listFiles();
		if(files != null){
			for (File file : files) {
				if (file.isDirectory()) {
					getRecursiveFiles(file, fileList);
				} else {
					fileList.add(file);
				}
			}
			return fileList.toArray(new File[fileList.size()]);
		}
		return null;
	}
}
