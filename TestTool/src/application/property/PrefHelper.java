package application.property;

import java.util.HashMap;
import java.util.Set;

/**
 * Abstract class for preference handling. Information are stored in a map. To store and load preferences override
 * both methods {@link PrefHelper#store()} and {@link PrefHelper#load()}.
 * 
 * 
 * @see {@link UserPreferences}, {@link ProjectPreferences}
 * @author JJungen
 *
 */
public abstract class PrefHelper {

	private final HashMap<String, String> map;

	public PrefHelper() {
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

	public abstract void store();

	public String getDefaultValue(String key) {
		return null;
	}

	protected final Set<String> keySet() {
		return map.keySet();
	}

}
