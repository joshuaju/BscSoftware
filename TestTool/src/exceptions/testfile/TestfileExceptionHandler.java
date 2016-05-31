package exceptions.testfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import external.Keyword;

public class TestfileExceptionHandler {

	// public static TestfileException InvalidCharacter(int lineNumber, String
	// line) {
	// return new TestfileException("Ungültiges Zeichen: Zeile " + lineNumber +
	// ": " + line);
	// }
	private static final String PREFIX = "Einlesen: ";

	public static TestfileException InvalidAssignVariable(int lineNumber, String assignVariable) {
		return new TestfileException(
				PREFIX + "Ungültige Variable für eine Zuweisung: Zeile " + lineNumber + ": " + assignVariable);
	}

	public static TestfileException InvalidParameterForRepeat(String parameter) {
		return new TestfileException(PREFIX + "Ungültiger Parameter für die Wiederholungszahl des Tests: " + parameter);
	}

	public static TestfileException InvalidMathExpression(String expression, Throwable cause) {
		return new TestfileException(PREFIX + "Ungültiger Mathematischer ausdruck: " + expression, cause);
	}

	public static TestfileException InvalidAssignValue(int lineNumber, String assignArgs) {
		return new TestfileException(
				PREFIX + "Ungültiger Wert für eine Zuweisung: Zeile " + lineNumber + ": " + assignArgs);
	}
	
	public static TestfileException InvalidVariableFileLine(int lineNumber, String fileName, String line) {
		return new TestfileException(PREFIX + "Ungültige Zuweisung in Variabel-Datei: " + fileName + ": Zeile " + lineNumber + ": " + line);
	}

	public static TestfileException InvalidKeyword(int lineNumber, String strKeyword) {
		return new TestfileException(
				PREFIX + "Ungültige Zeichen im Schlüsselwort: Zeile " + lineNumber + ": " + strKeyword);
	}

	public static TestfileException InvalidKeywordArgument(int lineNumber, int argNumber, String keywordArgs) {
		return new TestfileException(PREFIX + "Ungültige Zeichen im Argument: Zeile " + lineNumber + ": Argument "
				+ argNumber + ": " + keywordArgs);
	}

	public static TestfileException InvalidLine(int lineNumber, String line) {
		return new TestfileException(PREFIX + "Ungültige Zeile: Zeile " + line + ": " + line);
	}

	public static TestfileException Incomplete(String... message) {
		String msg = "";
		for (String tmpMsg : message) {
			msg += tmpMsg + "; ";
		}
		return new TestfileException(PREFIX + "Unvollständige Testdatei: " + msg);
	}

	public static TestfileException NoLibraryForThisName(String name) {
		return new TestfileException(PREFIX + "Keine Bibliothek mit diesem Name: " + name);
	}

	public static TestfileException DuplicatedKeyword(Keyword keyword) {
		return new TestfileException(PREFIX + "Das Keyword kommt in mehreren Bibliotheken vor: " + keyword.getName());
	}

	public static TestfileException NoSuchKeywordInLibrary(String libname, String keywordName) {
		return new TestfileException(PREFIX + "Das Keyword kommt nicht vor: " + libname + "." + keywordName);
	}

	public static TestfileException InvalidParameter(String parameter) {
		return new TestfileException(PREFIX + "Parameter ist ungültig: " + parameter);
	}

	public static TestfileException InvalidLibraryPath(String path) {
		return new TestfileException(PREFIX + "Ungültiger Pfad für Bibliothek: " + path);
	}

	public static TestfileException InvalidLibraryName(String name) {
		return new TestfileException(PREFIX + "Ungültiger Name für Bibliothek: " + name);
	}

	public static FileNotFoundException NoSuchFile(File file) {
		if (file.isDirectory())
			return new FileNotFoundException(PREFIX + "Datei ist ein Verzeichnis: " + file.getAbsolutePath());
		else
			return new FileNotFoundException(PREFIX + "Datei existiert nicht: " + file.getAbsolutePath());
	}

	public static FileNotFoundException NoSuchDirectory(File file) {
		if (!file.isDirectory())
			return new FileNotFoundException(PREFIX + "Vezeichnis ist eine Datei: " + file.getAbsolutePath());
		else
			return new FileNotFoundException(PREFIX + "Verzeichnis existiert nicht: " + file.getAbsolutePath());
	}

	public static IOException CouldNotReadFile(File file, IOException cause) {
		return new IOException(PREFIX + "Datei konnte nicht gelesen werden: " + file.getAbsolutePath(), cause);
	}

	public static TestfileException VariableIsNull(String variablename) {
		return new TestfileException(PREFIX + "Die angeforderte Variable existiert nicht: " + variablename);
	}

}
