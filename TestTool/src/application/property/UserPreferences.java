package application.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * This class handles the user's preferences
 * @author JJungen
 *
 */
public class UserPreferences extends PrefHelper {

	public static final String EDITOR_EXE = "editor.exe";
	public static final String LIBRARY_DIR = "library.dir";
	public static final String APPLICATION_HOME_DIR = "home.dir";	
	public static final String STD_LIBRARY_DIR = "std.library.dir";
	public static final String SESSION_DIR = "session.dir";
	public static final String ALWAYS_ON_TOP = "always.on.top";

	private static PrefHelper pref = new UserPreferences();

	private File propertyfile;
	
	public static PrefHelper get() {
		return pref;
	}

	@Override
	protected void load() {
		File appdata = new File(System.getenv("APPDATA"));
		String projectname = ProjectPreferences.get().get(ProjectPreferences.PROJECT_NAME);
		propertyfile = new File(appdata, projectname + File.separator + "user.prop");
		propertyfile.getParentFile().mkdirs();
		try {
			propertyfile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Properties properties = new Properties();

		try {
			InputStream bis = new FileInputStream(propertyfile);
			properties.load(bis);
			System.out.println("Properties loaded: " + propertyfile.getAbsolutePath());
			bis.close();
		} catch (IOException e) {
			System.err.println("Properties not loaded: " + propertyfile.getAbsolutePath());
		}

		Set<Object> keys = properties.keySet();
		for (Object key : keys) {
			String value = properties.getProperty(key.toString(), null);
			put(key.toString(), value);
		}

		File homeFolder = new File(getOrDefault(UserPreferences.APPLICATION_HOME_DIR));
		homeFolder.mkdirs();
		
		File libfolder = new File(getOrDefault(UserPreferences.LIBRARY_DIR));
		libfolder.mkdirs();

		File stdfolder = new File(getOrDefault(UserPreferences.STD_LIBRARY_DIR));
		stdfolder.mkdirs();
		
		File sessionFolder = new File(getOrDefault(UserPreferences.SESSION_DIR));
		sessionFolder.mkdirs();		
	}

	@Override
	public void store() {
		Properties prop = new Properties();

		Set<String> keys = super.keySet();
		for (String key : keys) {
			String value = getOrDefault(key);
			prop.put(key, value);
		}
		
		try {
			prop.store(new FileOutputStream(propertyfile), "In dieser Datei stehen die Einstellung der Applikation.");
			System.out.println("Properties stored: " + propertyfile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Properties not stored: " + propertyfile.getAbsolutePath());			
		}
	}

	@Override
	public String getDefaultValue(String key) {
		if (key.equals(EDITOR_EXE)) {
			return "notepad";
		} else if (key.equals(APPLICATION_HOME_DIR)){
			String projectname = ProjectPreferences.get().getOrDefault(ProjectPreferences.PROJECT_NAME);
			return System.getProperty("user.home") + File.separator + "Documents" + File.separator + projectname + File.separator;
		} else if (key.equals(LIBRARY_DIR)) {			
			return getOrDefault(APPLICATION_HOME_DIR) + "libs" + File.separator;
		} else if (key.equals(STD_LIBRARY_DIR)) {
			return getOrDefault(LIBRARY_DIR) + "std";
		} else if (key.equals(SESSION_DIR)) {			
			return getOrDefault(APPLICATION_HOME_DIR) + "sessions" + File.separator;
		} else if (key.equals(ALWAYS_ON_TOP)) {
			return Boolean.FALSE.toString();
		} else {
			return super.getDefaultValue(key);
		}
	}

}
