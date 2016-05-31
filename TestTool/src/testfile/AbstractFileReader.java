package testfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import exceptions.testfile.TestfileExceptionHandler;

abstract class AbstractFileReader {	
	
	/**
	 * Prüft ob es an dem Pfad eine Datei gibt. Sofern eine Datei vorhanden ist
	 * wird diese zurückgegebn.
	 * 
	 * @param path
	 *            Pfad
	 * @return Datei
	 * @throws FileNotFoundException
	 *             Datei wurde nicht gefunden oder der Pfad führt zu einem
	 *             Verzeichnis
	 */
	public File getFileFromPath(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			throw TestfileExceptionHandler.NoSuchFile(file);
		}

		return file;
	}
		
	/**
	 * Liest aus der angegeben Datei alle Zeilen
	 * 
	 * @param file
	 *            Datei die gelesen wird
	 * @return alle Zeilen
	 * @throws IOException
	 *             Wenn die Datei nicht gelesen werden kann
	 */
	public String[] getLinesFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw TestfileExceptionHandler.CouldNotReadFile(file, e);
		}
		return lines.toArray(new String[0]);
	}
	
}
