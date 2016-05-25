package exceptions;

import java.io.File;
import java.io.FileNotFoundException;

public class ExceptionHandler {

	public static TestfileSyntaxException InvalidCharacter(int lineNumber, String line) {
		return new TestfileSyntaxException("Ung�ltiges Zeichen in Zeile " + lineNumber + ": " + line);
	}

	public static TestfileSyntaxException InvalidLine(int lineNumber, String line) {
		return new TestfileSyntaxException("Ung�ltige Zeile " + lineNumber + ": " + line);
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
		return new FileNotFoundException("Der angegebene Pfad f�hrt nicht zu einer Datei: " + file.getAbsolutePath());
	}
	
	public static FileNotFoundException DirectoryIsFile(File file) {
		return new FileNotFoundException("Der angegebene Pfad f�hrt nicht zu einem Verzeichnis: " + file.getAbsolutePath());
	}
	
	public static ClassNotFoundException ClassnameNotInClassLoader(String name){
		return new ClassNotFoundException("Der Klassenname wurde nicht gefunden: " + name);
	}
	
	public static KeywordLibraryException ClassIsNotAKeywordLibrary(String name){
		return new KeywordLibraryException("Die Klasse ist keine g�ltige Bibliothek: " + name);
	}
		
	public static KeywordLibraryException NoDefaultContrucotr(String name){
		return new KeywordLibraryException("Die Bibliothek hat keinen Default-Konstruktor: " + name);
	}
	
	public static KeywordLibraryException CouldNotInstantiate(String name){
		return new KeywordLibraryException("Es konnte keine Instanz der Bibliothek eryeugt werden: " + name);
	}
	
	public static KeywordLibraryException MethodIsNotAKeywordMethod(String name){
		return new KeywordLibraryException("Die Methode ist keine g�ltig Schl�sselwort-Methode: " + name);
	}

}
