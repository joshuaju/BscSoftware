package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyHelper {

	public static String ALWAYS_ON_TOP = "always.on.top";
	public static String TEXTEDITOR = "editor";
	public static String DEF_LIBRARIES_DIR = "def.lib.dir";
	
	private static Properties props = null;

	/**
	 * Läd die Property-Datei des Nutzers. Der Speicherort ist über die
	 * Property-Datei des Projekts festgelegt.
	 * 
	 * @param forceReload
	 *            Die Klasse hält die geladene Property-Datei im speicher. Wenn die
	 *            Datei zwischenzeitlich auf Dateiebenen geändert wurde, dann
	 *            sollte die Datei neu eingelesen werden. TRUE zum erneuten einlesen, sonst FALSE
	 * @return
	 * @throws IOException
	 */
	public static Properties loadApplicationProperties() {
		boolean forceReload = false;
		if (props == null || forceReload) {
			String userpropPath = getUserPropertyPath();
			
			props = new Properties();

			try {
				InputStream in = new FileInputStream(userpropPath);
				props.load(in);
				in.close();
				System.out.println("Load Userproperties from " + userpropPath);
			} catch (FileNotFoundException e) {
				System.err.println("Userproperties not found");
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		return props;
	}	
	
	/**
	 * Speicher die Property-Datei des Nutzers. Der Speicherort ist über die
	 * Property-Datei des Projekts festgelegt.
	 * 
	 * @param properties
	 * @throws IOException
	 */
	public static void saveApplicationProperties(Properties properties) throws IOException {
		String userpropPath = getUserPropertyPath();
		File outFile = new File(userpropPath);
		outFile.getParentFile().mkdirs();
		outFile.createNewFile();

		OutputStream out = new FileOutputStream(outFile.getAbsoluteFile());
		properties.store(out, "User Properties");
		System.out.println("Saved Userproperties to " + userpropPath);
	}

	/**
	 * Läd die Property-Datei des Projekts. Die Datei liegt im Verzeichnis
	 * 'resources' und heißt 'project.properties'
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Properties getProjectProperties() {
		InputStream bis = PropertyHelper.class.getClassLoader().getResourceAsStream("project.properties");
		Properties projprops = new Properties();

		if (bis != null) {
			try {
			projprops.load(bis);
			bis.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		return projprops;
	}

	private static String getUserPropertyPath() {
		Properties projprops = getProjectProperties();
		String projname = projprops.getProperty("project.name");
		String userprop_sysenv = (String) projprops.getOrDefault("userprop.sysenv", "APPDATA");
		String userprop_filename = projprops.getProperty("userprop.filename");

		String userpropPath = System.getenv(userprop_sysenv) + "\\" + projname + "\\" + userprop_filename;
		return userpropPath.replace("\\", "/");
	}

}
