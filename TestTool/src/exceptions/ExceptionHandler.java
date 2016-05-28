package exceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionHandler {

	public static TestfileSyntaxException InvalidCharacter(int lineNumber, String line) {
		return new TestfileSyntaxException("Ungültiges Zeichen in Zeile " + lineNumber + ": " + line);
	}

	public static TestfileSyntaxException InvalidLine(int lineNumber, String line) {
		return new TestfileSyntaxException("Ungültige Zeile " + lineNumber + ": " + line);
	}

	public static TestfileContentException InvalidContent(String... errorMessages) {
		String msg = "Fehler in Testdatei: ";
		for (int i = 0; i < errorMessages.length; i++) {
			msg += i + ". " + errorMessages[i];
		}
		return new TestfileContentException(msg);
	}

	public static TestfileSyntaxException InvalidParameter(String parameter) {
		return new TestfileSyntaxException("Der Parameter ist ungültig: " + parameter);
	}

	public static TestfileException DuplicateKeyword(String keyword) {
		return new TestfileException(
				"Das Keyword kommt in mehreren Bibliotheken vor und kann nicht zugeordnet werden: " + keyword);
	}

	public static TestfileException NoSuchKeyword(String keyword, String libname) {
		if (libname.length() > 0){
			return new TestfileException("Das Keyword existiert nicht in der Bibliothek: " + libname + "." + keyword);
		} else {
			return new TestfileException("Das Keyword existiert nicht: " + keyword);
		}
		
	}
	
	public static TestfileException NoSuchKeyword(String keyword) {
		return NoSuchKeyword(keyword, "");
	}
	
	public static TestfileException NoSuchLibrary(String library) {
		return new TestfileException("Der Name für eine Bibliothek existiert nicht: " + library);
	}

	public static FileNotFoundException FileNotFound(File file) {
		return new FileNotFoundException("Die Datei wurde nicht gefunden: " + file.getAbsolutePath());
	}

	public static FileNotFoundException FileIsDirectory(File file) {
		return new FileNotFoundException("Der angegebene Pfad führt nicht zu einer Datei: " + file.getAbsolutePath());
	}
	
	public static FileNotFoundException DirectoryNotFound(File file) {
		return new FileNotFoundException("Der angegebene Pfad führt nicht zu einem Verzeichnis: " + file.getAbsolutePath());
	}

	public static FileNotFoundException DirectoryIsFile(File file) {
		return new FileNotFoundException(
				"Der angegebene Pfad führt nicht zu einem Verzeichnis: " + file.getAbsolutePath());
	}

	public static ClassNotFoundException ClassnameNotInClassLoader(String name) {
		return new ClassNotFoundException("Der Klassenname wurde nicht gefunden: " + name);
	}

	public static KeywordLibraryException ClassIsNotAKeywordLibrary(String name) {
		return new KeywordLibraryException("Die Klasse ist keine gültige Bibliothek: " + name);
	}

	public static KeywordLibraryException NoDefaultContructor(String name) {
		return new KeywordLibraryException("Die Bibliothek hat keinen Default-Konstruktor: " + name);
	}

	public static KeywordLibraryException CouldNotInstantiate(String name) {
		return new KeywordLibraryException("Es konnte keine Instanz der Bibliothek eryeugt werden: " + name);
	}

	public static KeywordLibraryException MethodIsNotAKeywordMethod(String name) {
		return new KeywordLibraryException("Die Methode ist keine gültig Schlüsselwort-Methode: " + name);
	}

	public static IllegalArgumentException TooManyArguments(String keyword) {
		return new IllegalArgumentException("Die Anzahl der Argumente für das Schlüsselwort ist ungültig: " + keyword);
	}

	public static IllegalArgumentException WrongArgument(String keyword, int argNumber, Object arg) {
		return new IllegalArgumentException("Das Argument ist nicht für das Schlüsselwort gültig: " + keyword
				+ ", Argument " + argNumber + ": " + arg);
	}

	public static KeywordLibraryException LibraryPathIsInvalid(String path) {
		return new KeywordLibraryException("Der Bibliothekspfad ist ungültig: " + path);
	}

	public static KeywordLibraryException LibraryNameIsInvalid(String name) {
		return new KeywordLibraryException("Der Bibliotheksname ist ungültig: " + name);
	}
	
	public static TestfileException VariableIsNull(String varname){
		return new TestfileException("Die angeforderte Variable wurde nie initialisiert: " +  varname);
	}
	
	public static IOException ProcessJarFileException(File file){
		return new IOException("Beim verarbeiten des Jar-Verzeichnis ist ein fehler aufgetreten: " + file.getAbsolutePath());
	}
	
	public static ClassNotFoundException ClassNotInJarFile(File file, String classname){
		return new ClassNotFoundException("Die Klasse wurde nicht im Jar-Verzeichnis gefunen: " + file.getName() + ": " + classname);
	}
	
}
