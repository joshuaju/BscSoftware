package external;

import java.lang.reflect.Method;

import exceptions.keywordlibrary.KeywordExceptionHandler;
import exceptions.keywordlibrary.KeywordLibraryException;

/**
 * This class stores information on a single keyword
 * 
 * @see ExecutableKeyword
 * @author JJungen
 *
 */
public class SimpleKeyword {

	protected final Method method;
	private final annotations.Keyword annotation;

	SimpleKeyword(Method method) throws KeywordLibraryException {
		if (!SimpleKeyword.isKeywordMethod(method)) {
			throw KeywordExceptionHandler.MethodIsNotAKeyword(method);
		}
		this.method = method;
		this.annotation = method.getAnnotation(annotations.Keyword.class);
	}

	/**
	 * Den Namen, also die String-Darstellung des Schlüsselworts
	 * 
	 * @return Name
	 */
	public String getName() {
		return annotation.Name();
	}

	/**
	 * Die Beschreibung zum Schlüsselwort
	 * 
	 * @return
	 */
	public String getDescription() {
		return annotation.Description();
	}

	/**
	 * Die Beschreibung zu den Übergabeparametern
	 * 
	 * @return
	 */
	public String getParameter() {
		return annotation.Parameter();
	}

	/**
	 * Die Beschreibung zum Rückgabewert
	 * 
	 * @return
	 */
	public String getReturn() {
		return annotation.Return();

	}

	/**
	 * Die Typen der Übergabeparamter (von links nach rechts)
	 * 
	 * @return Array der Typen
	 */
	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

	/**
	 * Den Typ des Rückgabewerts
	 * 
	 * @return
	 */
	public Class<?> getReturntype() {
		return method.getReturnType();
	}

	@Override
	public String toString() {
		return "[NAME] " + getName() + ", [DESC] " + getDescription() + ", [PARA] " + getParameter() + ", [RET] "
				+ getReturn();
	}

	public static boolean isKeywordMethod(Method method) {
		annotations.Keyword anno = method.getAnnotation(annotations.Keyword.class);
		if (anno != null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNameEqual(ExecutableKeyword keyword, String name) {
		return keyword.getName().equals(name);
	}

}
