package exceptions.keywordlibrary;

import java.lang.reflect.Method;

import external.ExecutableKeyword;

public class KeywordExceptionHandler {

	public static KeywordException MethodIsNotAKeyword(Method method) {
		return new KeywordException(
				"Methode ist kein Keyword: " + method.getClass().getName() + "." + method.getName());
	}

	public static KeywordException TooManyArguments(ExecutableKeyword keyword) {
		return new KeywordException("Ungültige Anzahl von Übergabeparametern: " + keyword.getName());
	}

	public static KeywordException WrongArgument(ExecutableKeyword keyword, Object arg, int argNumber) {
		return new KeywordException("Ungültiger Übergabeparameter: Parameter " + argNumber + ", " + arg);
	}
	
	public static KeywordException WrongArgument(ExecutableKeyword keyword, IllegalArgumentException cause) {
		return new KeywordException("Ungültiger Übergabeparameter", cause);
	}

}
