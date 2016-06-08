package application;

import java.util.HashMap;
import java.util.Set;

public abstract class PrefHelper {

	private final HashMap<String, String> map;
	
	public PrefHelper(){
		map = new HashMap<>();
		load();
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return getOrDefault(key, null);
	}

	public String getOrDefault(String key) {
		String def = getDefaultValue(key);
		return map.getOrDefault(key, def);
	}

	public String getOrDefault(String key, String def) {
		return map.getOrDefault(key, def);
	}
	
	protected abstract void load();

	protected abstract void store();
			
	public String getDefaultValue(String key){
		return null;
	}
	
	protected final Set<String> keySet(){
		return map.keySet();
	}
	
}