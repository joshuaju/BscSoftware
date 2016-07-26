package input;

import java.util.HashMap;

import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;

/**
 * This class stores a map of variable names and related variables
 * 
 * @author JJungen
 *
 */
public class VariableFile {

	private final HashMap<String, String> variableMap;

	public VariableFile() {
		variableMap = new HashMap<>();
	}

	public void addVariable(String name, String value) {
		variableMap.put(name, value);
	}

	public boolean hasValueFor(String name) {
		return variableMap.containsKey(name);
	}

	public String getValueFor(String name) throws TestfileException {
		if (!hasValueFor(name)) {
			throw TestfileExceptionHandler.VariableIsNull(name);
		}
		return variableMap.get(name);
	}

	public String getValueForOrNull(String name) {
		return variableMap.getOrDefault(name, null);
	}

}
