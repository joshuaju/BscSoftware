package testfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;

/**
 * Die Klasse ist verantwortlich für das einlesen von Testdateien.
 * 
 * @author JJungen
 *
 */
public class TestfileReader {

	private static TestfileReader tfReader = null;

	private static void createInstance() {
		if (tfReader == null) {
			tfReader = new TestfileReader();
		}
	}

	/**
	 * Liest eine Testdatei ein, unter beachtung der syntaktishen und
	 * inhaltlichen korrektheit.
	 * 
	 * @param path
	 *            Pfad der Testdatei
	 * @return Inhalt der Testdatei
	 * @throws IOException
	 *             Wenn die Datei nicht gefunden wird, oder beim lesen der Datei
	 *             fehler auftreten
	 */
	public static Testfile read(String path) throws IOException {
		createInstance();
		return tfReader._read(path);
	}

	private final boolean NEEDS_AUTHOR;
	private final boolean NEEDS_TESTNAME;
	private final boolean NEEDS_DESCRIPTION;
	private final boolean NEEDS_LIBRARIES;
	private final boolean NEEDS_SETUP;
	private final boolean NEEDS_TEST;
	private final boolean NEEDS_TEARDOWN;

	/**
	 * Erzeugt eine neue Instanz der Klasse.
	 * 
	 * @param needsAuthor
	 *            eingelesene Testfile benötigt einen Autoren
	 * @param needsTestname
	 *            eingelesene Testfile benötigt einen Testnamen
	 * @param needsDescription
	 *            eingelesene Testfile benötigt eine Beschreibung
	 * @param needsLibraries
	 *            eingelesene Testfile benötigt mindestens eine referenziert
	 *            Bibliothek
	 * @param needsSetup
	 *            eingelesene Testfile benötigt eine Testaufbauphase
	 * @param needsTest
	 *            eingelesene Testfile benötigt eine Testphase
	 * @param needsTeardown
	 *            eingelesene Testfile benötigt eine Testabbauphase
	 */
	private TestfileReader(boolean needsAuthor, boolean needsTestname, boolean needsDescription, boolean needsLibraries,
			boolean needsSetup, boolean needsTest, boolean needsTeardown) {
		NEEDS_AUTHOR = needsAuthor;
		NEEDS_TESTNAME = needsTestname;
		NEEDS_DESCRIPTION = needsDescription;
		NEEDS_LIBRARIES = needsLibraries;
		NEEDS_SETUP = needsSetup;
		NEEDS_TEST = needsTest;
		NEEDS_TEARDOWN = needsTeardown;
	}

	/**
	 * Erzeugt eine neue Instanz. Die benötigten Inhalten von Testfiles werden
	 * auf Standardwerte gesetzt.
	 */
	private TestfileReader() {
		NEEDS_AUTHOR = true;
		NEEDS_TESTNAME = true;
		NEEDS_DESCRIPTION = false;
		;
		NEEDS_LIBRARIES = false;
		NEEDS_SETUP = false;
		NEEDS_TEST = true;
		NEEDS_TEARDOWN = false;
	}

	/**
	 * @see TestfileReader#read(String)
	 */
	private Testfile _read(String path) throws IOException {
		File file = getFileFromPath(path);
		String[] lines = getLinesFromFile(file);
		Testfile testfile = readLinesToTestfile(lines);
		testfile.setPath(file);
		return testfile;
	}

	/**
	 * Erstellt aus den angegebenen Zeilen eine {@linkplain Testfile}
	 * 
	 * @param lines
	 *            Zeilen zum erstellen der Testfile
	 * @return
	 * @throws TestfileSyntaxException
	 * @throws TestfileContentException
	 * @throws IOException
	 */
	private Testfile readLinesToTestfile(String[] lines) throws TestfileException {
		Testfile testfile = new Testfile();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();

			if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) { // comment
				// ignore
			} else if (line.startsWith(Testfile.TAG_AUTHOR)) { // author
				String author = getLineContent(Testfile.TAG_AUTHOR, line);
				testfile.setAuthor(author);
			} else if (line.startsWith(Testfile.TAG_TESTNAME)) { // testname
				String testname = getLineContent(Testfile.TAG_TESTNAME, line);
				testfile.setTestname(testname);
			} else if (line.startsWith(Testfile.TAG_DESCRIPTION)) { // description
				String description = getLineContent(Testfile.TAG_DESCRIPTION, line);
				for (; i < lines.length - 1; i++) {
					line = lines[i + 1].trim();
					if (!line.startsWith(Testfile.TAG_FIRST_CHAR)) {
						if (line.length() > 0) {
							description += " " + line;
						}
					} else {
						break;
					}
				}
				testfile.setDescription(description);
			} else if (line.startsWith(Testfile.TAG_LIBRARY)) { // library
				String libPath = getLineContent(Testfile.TAG_LIBRARY, line);
				testfile.addLibraryPath(libPath);
			} else if (line.startsWith(Testfile.TAG_SETUP)) { // setup
				for (; i < lines.length - 1; i++) {
					line = lines[i + 1].trim();
					if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
						continue;
					} else if (line.startsWith(Testfile.TAG_FIRST_CHAR)) {
						break;
					} else {
						testfile.addSetupLine(line, i + 2);
					}
				}
			} else if (line.startsWith(Testfile.TAG_TEST)) { // test
				for (; i < lines.length - 1; i++) {
					line = lines[i + 1].trim();
					if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
						continue;
					} else if (line.startsWith(Testfile.TAG_FIRST_CHAR)) {
						break;
					} else {
						testfile.addTestLine(line, i + 2);
					}
				}
			} else if (line.startsWith(Testfile.TAG_TEARDOWN)) { // teardown
				for (; i < lines.length - 1; i++) {
					line = lines[i + 1].trim();
					if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
						continue;
					} else if (line.startsWith(Testfile.TAG_FIRST_CHAR)) {
						break;
					} else {
						testfile.addTeardownLine(line, i + 2);
					}
				}
			} else {
				throw TestfileExceptionHandler.InvalidLine(i + 2, line);
			}
		}

		checkTestfileContent(testfile);
		return testfile;
	}

	/**
	 * Überprüft ob der Inhalt der Testfile korrekt ist. Im Konstruktor werden
	 * die notwendigen Inhalte definiert. Wenn ein notwendiger Inhalte nicht
	 * gesetzt ist, dann wird ein Fehler geworfen.
	 * 
	 * @param testfile
	 *            Testfile die überprüft wird
	 * @throws TestfileContentException
	 */
	private void checkTestfileContent(Testfile testfile) throws TestfileException {
		ArrayList<String> errorMessages = new ArrayList<>();
		if (NEEDS_AUTHOR && !testfile.hasAuthor()) {
			errorMessages.add("Kein Author angegeben");
		}
		if (NEEDS_TESTNAME && !testfile.hasTestname()) {
			errorMessages.add("Kein Testname angegeben");
		}
		if (NEEDS_DESCRIPTION && !testfile.hasDescription()) {
			errorMessages.add("Keine Beschreibung angegeben");
		}
		if (NEEDS_LIBRARIES && !testfile.hasLibraryPaths()) {
			errorMessages.add("Keine Bibliotheken angegeben");
		}
		if (NEEDS_SETUP && !testfile.hasSetupLines()) {
			errorMessages.add("Keine Setup Schlüsselwörter");
		}
		if (NEEDS_TEST && !testfile.hasTestLines()) {
			errorMessages.add("Keine Schlüsselwörter im Testbereich");
		}
		if (NEEDS_TEARDOWN && !testfile.hasTeardownLines()) {
			errorMessages.add("Keine Teardown Schlüsselwörter");
		}

		if (errorMessages.size() > 0) {
			throw TestfileExceptionHandler.Incomplete(errorMessages.toArray(new String[0]));
		}
	}

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
	private File getFileFromPath(String path) throws FileNotFoundException {
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
	private String[] getLinesFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw TestfileExceptionHandler.CouldNotReadFile(file, e);
		}
		return lines.toArray(new String[0]);
	}

	/**
	 * Entfernt (äußere) Leerzeichen und einen Teil des Strings
	 * 
	 * @param remove
	 *            Teil der Entfernt wird
	 * @param line
	 *            String der bearbeitet wird
	 * @return verarbeiteten String
	 */
	private String getLineContent(String remove, String line) {
		line = line.replace(remove, "").trim();
		return line;
	}

}
