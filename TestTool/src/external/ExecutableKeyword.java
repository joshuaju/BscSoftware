package external;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import exceptions.keywordlibrary.KeywordException;
import exceptions.keywordlibrary.KeywordExceptionHandler;
import exceptions.keywordlibrary.KeywordLibraryException;

/**
 * Diese Klasse repräsentiert ein Keyword
 * 
 * @author JJungen
 *
 */
public class ExecutableKeyword extends SimpleKeyword {

	private final Object libInstance;

	ExecutableKeyword(Object libInstance, Method method) throws KeywordLibraryException {
		super(method);
		this.libInstance = libInstance;
	}

	public ExecutableKeyword(Object libInstance, SimpleKeyword simple) throws KeywordLibraryException {
		this(libInstance, simple.method);
	}

	/**
	 * Dieses Keyword wird an dem Object instance, mit den Übergabeparametern
	 * args, aufegerufen
	 * 
	 * @param instance
	 *            ausführendes Objekt
	 * @param args
	 *            Übergabeparameter
	 * @return Rückgabewert des Keyword 
	 * @throws KeywordException 
	 * @throws IllegalAccessException
	 *             Fehler bei der Reflexion
	 * @throws IllegalArgumentException
	 *             Die Übergabeparameter sind nicht gültig
	 */
	public Object invoke(Object... args) throws KeywordException {

		Class<?>[] argClasses = getParameterTypes();

		if (argClasses.length != args.length) {
			throw KeywordExceptionHandler.TooManyArguments(this);
		}

		for (int i = 0; i < getParameterTypes().length; i++) {
			Class<?> tmpArgClass = getParameterTypes()[i];
			Object tmpArg = args[i];
			tmpArg = parseArgument(tmpArgClass, tmpArg);
			if (tmpArg == null) {
				throw KeywordExceptionHandler.WrongArgument(this, tmpArg, i);
			}
			args[i] = tmpArg;
		}

		try {
			return method.invoke(libInstance, args);
		} catch (IllegalAccessException e) {
			// cannot happen
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			throw KeywordExceptionHandler.WrongArgument(this, null, -1);
		} catch (InvocationTargetException e) {
//			String x = e.getTargetException().getMessage();
//			if (x.contains("Not on FX application thread")) {
//				ObjectProperty<Throwable> throwable = new SimpleObjectProperty<>();
//				Platform.runLater(() -> {
//					try {
//						invoke(args);
//					} catch (KeywordException | AssertionError e1) {
//						throwable.set(e1);
//					}
//				});
//				if (throwable.isNotNull().get()){
//					throw new AssertionError(e.getCause().getMessage());
//				}
//			} else {
				throw new AssertionError(e.getCause().getMessage());
//			}
		}
		return null;
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

}
