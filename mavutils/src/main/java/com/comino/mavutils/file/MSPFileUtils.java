package com.comino.mavutils.file;

import java.io.File;
import java.net.URLDecoder;
import java.security.CodeSource;

public class MSPFileUtils {
	
	@SuppressWarnings("rawtypes")
	public static String getJarContainingFolder(Class aclass)  {
		CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

		File jarFile;

		try {

			if (codeSource.getLocation() != null) {
				jarFile = new File(codeSource.getLocation().toURI());
			}
			else {
				String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
				String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
				jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
				jarFile = new File(jarFilePath);
			}
			System.out.println("Path searched in "+jarFile.getParentFile().getAbsolutePath());
			return jarFile.getParentFile().getAbsolutePath();

		} catch(Exception e) {
			System.out.println("Path not found: "+e.getMessage());
			return null;
		}
	}

}
