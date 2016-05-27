package external;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import exceptions.ExceptionHandler;
import exceptions.KeywordLibraryException;

/**
 * Diese Klasse repräsentiert ein Keyword
 * 
 * @author JJungen
 *
 */
public class Keyword {

	private final annotations.Keyword annotation;
	private final Method method;
	private final Object libInstance;

	Keyword(Object libInstance, Method method) throws KeywordLibraryException {
		if (!Keyword.isKeywordMethod(method)) {
			throw ExceptionHandler.MethodIsNotAKeywordMethod(method.getClass().getName() + "." + method.getName());
		}
		this.annotation = method.getAnnotation(annotations.Keyword.class);
		this.method = method;
		this.libInstance = libInstance;
	}

	/**
	 * Den Namen, also die String-Darstellung des Schlüsselworts
	 * @return Name
	 */
	public String getName() {
		return annotation.Name();
	}

	/**
	 * Die Beschreibung zum Schlüsselwort 
	 * @return
	 */
	public String getDescription() {
		return annotation.Description();
	}

	/**
	 * Die Beschreibung zu den Übergabeparametern
	 * @return
	 */
	public String getParameter() {
		return annotation.Parameter();
	}

	/**
	 * Die Beschreibung zum Rückgabewert
	 * @return
	 */
	public String getReturn() {
		return annotation.Return();

	}

	/**
	 * Die Typen der Übergabeparamter (von links nach rechts)
	 * @return Array der Typen 
	 */
	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

	/**
	 * Den Typ des Rückgabewerts
	 * @return 
	 */
	public Class<?> getReturntype() {
		return method.getReturnType();
	}

	/**
	 * Dieses Keyword wird an dem Object instance, mit den Übergabeparametern args, aufegerufen 
	 * @param instance ausführendes Objekt
	 * @param args Übergabeparameter
	 * @return Rückgabewert des Keyword
	 * @throws IllegalAccessException Fehler bei der Reflexion 
	 * @throws IllegalArgumentException	Die Übergabeparameter sind nicht gültig
	 * @throws AssertionError Exception die bei vergleichenden Methoden auftritt.
	 */
	public Object invoke(Object... args)
			throws IllegalAccessException, IllegalArgumentException, AssertionError {

		Class<?>[] argClasses = getParameterTypes();

		if (argClasses.length != args.length) {
			throw ExceptionHandler.TooManyArguments(getName());
		}

		for (int i = 0; i < getParameterTypes().length; i++) {
			Class<?> tmpArgClass = getParameterTypes()[i];
			Object tmpArg = args[i];
			tmpArg = parseArgument(tmpArgClass, tmpArg);
			if (tmpArg == null) {
				throw ExceptionHandler.WrongArgument(getName(), i, tmpArg);
			}
			args[i] = tmpArg;
		}

		try {
			return method.invoke(libInstance, args);
		} catch (InvocationTargetException e){
			throw new AssertionError(e.getCause().getMessage());
		}		
	}

	/**
	 * Die Methode versucht das Argument in die ArgumentKlasse zu 'parsen'.
	 * 
	 * @param argClass
	 *            Klasse des Arguments
	 * @param arg
	 *            Argument
	 * @return Instanz des Argument der Klasse, oder NULL wenn das Parsen nicht
	 *         möglich ist
	 */
	private Object parseArgument(Class<?> argClass, Object arg) {
		try {
			if (argClass.equals(String.class) || argClass.equals(Object.class)) {
				return arg.toString();
			} else if (argClass.equals(Boolean.class) || argClass.equals(boolean.class)) {
				return Boolean.parseBoolean(arg.toString());
			} else if (argClass.equals(Character.class) || argClass.equals(char.class)) {
				String str = arg.toString();
				if (str.length() == 1) {
					return str.charAt(0);
				}
			} else if (argClass.equals(Byte.class) || argClass.equals(byte.class)) {
				return Byte.parseByte(arg.toString());
			} else if (argClass.equals(Short.class) || argClass.equals(short.class)) {
				return Short.parseShort(arg.toString());
			} else if (argClass.equals(Integer.class) || argClass.equals(int.class)) {
				return Integer.parseInt(arg.toString());
			} else if (argClass.equals(Long.class) || argClass.equals(long.class)) {
				return Long.parseLong(arg.toString());
			} else if (argClass.equals(Float.class) || argClass.equals(float.class)) {
				return Float.parseFloat(arg.toString());
			} else if (argClass.equals(Double.class) || argClass.equals(double.class)) {
				return Double.parseDouble(arg.toString());
			}
		} catch (Exception e) {
			// Keine Fehlerbehandlung, es soll NULL zurückgegeben werden
		}
		return null;
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

	public static boolean isNameEqual(Keyword keyword, String name) {
		return keyword.getName().equals(name);
	}

}
