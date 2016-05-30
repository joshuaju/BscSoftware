package execution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import exceptions.executing.SetupException;
import exceptions.executing.TeardownException;
import exceptions.executing.TestException;
import exceptions.keywordlibrary.KeywordException;
import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;
import external.Keyword;
import external.KeywordLibrary;
import external.LibraryLoader;
import testfile.Testfile;
import testfile.TestfileReader;
import testfile.Testline;

public class TestExecuter {

	private final String path;
	private final HashMap<String, KeywordLibrary> libnamesMap;
	private final HashMap<String, Object> variablesMap;

	private Testfile testfile;
	private TestProtocol protocol;

	private boolean executed;

	public TestExecuter(String path) {
		this.path = path;

		libnamesMap = new HashMap<>();
		variablesMap = new HashMap<>();

		executed = false;
	}

	/**
	 * 
	 * @throws TestfileException Probleme beim lesen der Testdatei -> Abbruch des einzelnen Tests
	 * @throws SetupException Probleme in der Aufbauphase des Test -> Abbruch des gesamten Testlauf
	 * @throws KeywordLibraryException Probleme beim Laden der Bibliotheken -> Abbruch des einzelnen Tests
	 * @throws TeardownException -> Abbruch des gesamten Testlauf
	 * @throws TestException -> Abbruch des einzelnen Tests
	 */
	public void execute() throws TestfileException, SetupException, KeywordLibraryException, TeardownException, TestException {
		if (executed) {
			throw new IllegalStateException("Der Test wurde bereits ausgeführt");
		}

		executed = true;
		testfile = null;
		protocol = null;
		try {
			testfile = loadTestfile(path);
		} catch (IOException e) {
			protocol = new TestProtocol(false, "-", path, e.getMessage());
			throw new TestfileException(e.getMessage(), e);
		}

		try {
			loadLibraries(testfile);
		} catch (ClassNotFoundException | IOException | KeywordLibraryException e) {
			protocol = createProtocol(testfile, false, e.getMessage());
			throw new KeywordLibraryException(e.getMessage(), e);
		}

		try {
			execute(testfile.getSetupLines());
		} catch (TestfileException | KeywordException | AssertionError e) {
			protocol = createProtocol(testfile, false, "Setup: " + e.getMessage());
			throw new SetupException(e.getMessage(), e); 
		}

		try {
			execute(testfile.getTestLines());
		} catch (TestfileException | KeywordException | AssertionError e) {
			protocol = createProtocol(testfile, false, "Test: " + e.getMessage());
			throw new TestException(e.getMessage(), e);
		}

		try {
			execute(testfile.getTeardownLines());
		} catch (TestfileException | KeywordException | AssertionError e) {
			protocol = createProtocol(testfile, false, "Teardown: " + e.getMessage());
			throw new TeardownException(e.getMessage(), e);
		}

		if (protocol == null) {
			// Es ist kein Fehler aufgetreten
			protocol = createProtocol(testfile, true, "");
		}
	}

	public TestProtocol getProtocol() {
		if (!executed) {
			throw new IllegalStateException("Der Test wurde noch nicht ausgeführt");
		}
		return protocol;
	}

	private TestProtocol createProtocol(Testfile testfile, boolean passed, String message) {
		return new TestProtocol(passed, testfile.getTestname(), testfile.getPath(), message);
	}

	private void execute(Testline[] lines) throws TestfileException, KeywordException, AssertionError {
		for (int i = 0; i < lines.length; i++) {
			Testline line = lines[i];
			try {
				executeLine(line.text);
			} catch (TestfileException e) {
				throw new TestfileException("Zeile " + line.number + ": " + e.getMessage(), e);
			} catch (KeywordException e) {
				throw new KeywordException("Zeile " + line.number + ": " + e.getMessage(), e);
			} catch (AssertionError e) {
				throw new AssertionError("Zeile " + line.number + ": " + e.getMessage(), e);
			}
		}
	}

	private Object executeLine(String line) throws TestfileException, KeywordException, AssertionError {
		if (line.contains("=")) {
			return assignVariableLine(line);
		} else {
			return executeKeywordLine(line);
		}
	}

	private Object assignVariableLine(String line) throws TestfileException, KeywordException, AssertionError {
		int equalsIndex = line.indexOf("=");
		String strVar = line.substring(0, equalsIndex).trim().replaceAll("[{}]", "");

		String strVal = line.substring(equalsIndex + 1).trim();
		Object assignVal = null;
		if (strVal.startsWith("{")) { // variable
			strVal = strVal.replaceAll("[{}]", "");
			assignVal = variablesMap.getOrDefault(strVal, null);
		} else if (strVal.startsWith("\"")) { // value
			strVal = strVal.replace("\"", "");
			assignVal = strVal;
		} else { // keyword
			assignVal = executeKeywordLine(strVal);
		}

		variablesMap.put(strVar, assignVal);
		return assignVal;
	}

	private Object retrieveVariable(String var) throws TestfileException {
		Object result = variablesMap.getOrDefault(var, null);
		if (result == null) {
			throw TestfileExceptionHandler.VariableIsNull(var);
		}
		return result;
	}

	/**
	 * Führt eine Zeile aus in der ein Keyword und (optional) Parameter stehen
	 * 
	 * @param line
	 *            Zeile
	 * @return Rückgabewert des Keywords
	 * @throws TestfileException
	 * @throws AssertionError
	 * @throws KeywordException
	 */
	private Object executeKeywordLine(String line) throws TestfileException, KeywordException, AssertionError {
		int firstArg = line.indexOf("\"");
		int firstVar = line.indexOf("{");
		int first;

		if (firstArg == -1 && firstVar == -1) {
			first = line.length();
		} else if (firstArg == -1 || firstVar == -1) {
			first = Math.max(firstArg, firstVar);
		} else {
			first = Math.min(firstArg, firstVar);
		}

		String strKeyword = line.substring(0, first).trim();
		String parLine = line.substring(first).trim();

		Keyword keyword = findKeyword(strKeyword);
		Object[] args = getParameters(parLine);

		return keyword.invoke(args);
	}

	/**
	 * Sucht ein Keyword in allen in der Testdatei angegeben und standardmäßig
	 * geladenen Bibliotheken. Wenn ein Bibliotheksname vorangestellt ist, dann
	 * wird nur in dieser gesucht.
	 * 
	 * @param strKeyword
	 *            Keyword, (optional) mit Bibliotheksname davor
	 * @return Das ermittelte Keyword
	 * @throws TestfileException
	 *             Das Keyword wurde nicht gefunden, oder es kommt in mehreren
	 *             Bibliotheken vor
	 */
	private Keyword findKeyword(String strKeyword) throws TestfileException {
		ArrayList<KeywordLibrary> libs = new ArrayList<>();
		libs.addAll(libnamesMap.values());

		if (LibraryLoader.getInstance().hasDefaultLibraries()) {
			libs.addAll(Arrays.asList(LibraryLoader.getInstance().getDefaultLibraries()));
		}

		int indexOfDot = strKeyword.indexOf(".");
		String libName = "";
		if (indexOfDot != -1) {
			libName = strKeyword.substring(0, indexOfDot);
			strKeyword = strKeyword.substring(indexOfDot + 1);
			KeywordLibrary lib = libnamesMap.getOrDefault(libName, null);
			if (lib == null) {
				throw TestfileExceptionHandler.NoLibraryForThisName(libName);
			}
			libs = new ArrayList<>();
			libs.add(lib);
		}

		Keyword keyword = null;
		for (KeywordLibrary lib : libs) {
			Keyword tmpKeyword = lib.getKeywordByName(strKeyword);
			if (tmpKeyword != null) {
				if (keyword == null) {
					keyword = tmpKeyword;
				} else {
					throw TestfileExceptionHandler.DuplicatedKeyword(tmpKeyword);
				}
			}
		}
		if (keyword == null) {
			throw TestfileExceptionHandler.NoSuchKeywordInLibrary(strKeyword, libName);
		}
		return keyword;
	}

	private Object[] getParameters(String parameterLine) throws TestfileException {
		if (parameterLine.length() == 0) {
			return new Object[0];
		}
		String[] parStrArray = parameterLine.split(",");
		Object[] res = new Object[parStrArray.length];
		for (int i = 0; i < parStrArray.length; i++) {
			String strPar = parStrArray[i].toString().trim();

			if (strPar.startsWith("{")) {
				strPar = strPar.replaceAll("[{}]", "");
				strPar = retrieveVariable(strPar).toString();
			} else if (strPar.startsWith("\"")) {
				strPar = strPar.replace("\"", "");
			} else {
				throw TestfileExceptionHandler.InvalidParameter(strPar);
			}
			res[i] = strPar;
		}
		return res;
	}

	private Testfile loadTestfile(String path) throws IOException {
		return TestfileReader.read(path);
	}

	private void loadLibraries(Testfile testfile) throws ClassNotFoundException, IOException, KeywordLibraryException {
		String[] libLines = testfile.getLibraryPaths();

		for (String line : libLines) {
			String[] split = line.split(" ");
			String path = split[0];
			String name = split[1];

			if (!Pattern.matches("\".*\"", path)) {
				throw TestfileExceptionHandler.InvalidParameter(path);
			}
			path = path.substring(1, path.length() - 1);

			if (!Pattern.matches("[\\d \\w]*", name)) {
				throw TestfileExceptionHandler.InvalidParameter(name);
			}

			KeywordLibrary library = LibraryLoader.getInstance().loadLibrary(path);

			libnamesMap.put(name, library);
		}

	}

}
