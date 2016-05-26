package exceptions;

import java.io.File;
import java.io.FileNotFoundException;

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

	public static FileNotFoundException FileNotFound(File file) {
		return new FileNotFoundException("Die Datei wurde nicht gefunden: " + file.getAbsolutePath());
	}

	public static FileNotFoundException FileIsDirectory(File file) {
		return new FileNotFoundException("Der angegebene Pfad führt nicht zu einer Datei: " + file.getAbsolutePath());
	}
	
	public static FileNotFoundException DirectoryIsFile(File file) {
		return new FileNotFoundException("Der angegebene Pfad führt nicht zu einem Verzeichnis: " + file.getAbsolutePath());
	}
	
	public static ClassNotFoundException ClassnameNotInClassLoader(String name){
		return new ClassNotFoundException("Der Klassenname wurde nicht gefunden: " + name);
	}
	
	public static KeywordLibraryException ClassIsNotAKeywordLibrary(String name){
		return new KeywordLibraryException("Die Klasse ist keine gültige Bibliothek: " + name);
	}
		
	public static KeywordLibraryException NoDefaultContrucotr(String name){
		return new KeywordLibraryException("Die Bibliothek hat keinen Default-Konstruktor: " + name);
	}
	
	public static KeywordLibraryException CouldNotInstantiate(String name){
		return new KeywordLibraryException("Es konnte keine Instanz der Bibliothek eryeugt werden: " + name);
	}
	
	public static KeywordLibraryException MethodIsNotAKeywordMethod(String name){
		return new KeywordLibraryException("Die Methode ist keine gültig Schlüsselwort-Methode: " + name);
	}
	
	public static IllegalArgumentException TooManyArguments(String keyword){
		return new IllegalArgumentException("Die Anzahl der Argumente für das Schlüsselwort ist ungültig: " + keyword);
	}
	
	public static IllegalArgumentException WrongArgument(String keyword, int argNumber){
		return new IllegalArgumentException("Das Argument ist nicht für das Schlüsselwort gültig: "+ keyword + ", Argument " + argNumber);
	}

}
