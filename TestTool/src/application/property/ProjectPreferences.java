package application.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ProjectPreferences extends PrefHelper {

	public static final String PROJECT_NAME = "project.name";		
	
	private static PrefHelper pref = new ProjectPreferences();
	public static PrefHelper get(){
		return pref;
	}
	@Override
	protected void load() {
		InputStream bis = getClass().getClassLoader().getResourceAsStream("project.properties");
		
		Properties properties = new Properties();
		if (bis != null) {
			try {
				properties.load(bis);
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Set<Object> keys = properties.keySet();
		for (Object key: keys){
			String value = properties.getProperty(key.toString(), null);
			put(key.toString(), value);
		}
	}

	@Override
	public void store() {
		
	}

}
