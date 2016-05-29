package exceptions.keywordlibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class KeywordLibraryExceptionHandler {

	public static KeywordLibraryException ClassIsNotAKeywordLibrary(Class<?> theclass) {
		return new KeywordLibraryException("Klasse ist kein KeywordLibrary: " + theclass.getName());
	}
	
	public static KeywordLibraryException NoDefaultConstructor(Class<?> theclass) {
		return new KeywordLibraryException("Klasse ist hat keinen Standardkonstruktor: " + theclass.getName());
	}
	
	public static KeywordLibraryException CouldNotInstantiate(Class<?> theclass, Exception cause) {
		return new KeywordLibraryException("Klasse konnte nicht erzeugt werden: " + theclass.getName(), cause);
	}

	public static FileNotFoundException NoSuchDirectory(File file) {
		if (!file.exists())
			return new FileNotFoundException("Verzeichnis existiert nicht: " + file.getAbsolutePath());
		else if (!file.isDirectory())
			return new FileNotFoundException("Vezeichnis ist eine Datei: " + file.getAbsolutePath());
		else
			return new FileNotFoundException("Verzeichnis existiert nicht: " + file.getAbsolutePath());
	}

	public static FileNotFoundException NoSuchFile(File file) {
		if (!file.exists())
			return new FileNotFoundException("Datei existiert nicht: " + file.getAbsolutePath());
		else if (file.isDirectory())
			return new FileNotFoundException("Datei ist ein Verzeichnis: " + file.getAbsolutePath());
		else
			return new FileNotFoundException("Datei existiert nicht: " + file.getAbsolutePath());
	}

	public static FileNotFoundException NoSuchJarFile(File file) {
		return new FileNotFoundException("Datei ist kein Jar-Verzeichnis: " + file.getAbsolutePath());
	}

	public static KeywordLibraryException NoSuchClassInJarfile(String className, ClassNotFoundException cause) {
		return new KeywordLibraryException("Die Klassendatei wurd nicht gefunden: " + className, cause);
	}

	public static IOException JarFileProcessing(File file, IOException cause) {
		return new IOException("Jar Verzeichnis konnte nicht bearbeitet werden: " + file.getAbsolutePath(), cause);
	}

}
