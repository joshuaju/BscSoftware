package execution;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import exceptions.ExceptionHandler;
import exceptions.KeywordLibraryException;
import exceptions.TestfileException;
import external.Keyword;
import external.KeywordLibrary;
import external.LibraryLoader;
import testfile.Testfile;
import testfile.TestfileReader;

public class TestExecuter {

	private final String path;
	private final HashMap<String, KeywordLibrary> libnamesMap;
	private final HashMap<String, Object> variablesMap;
	private Testfile testfile;

	public TestExecuter(String path) {
		this.path = path;

		libnamesMap = new HashMap<>();
		variablesMap = new HashMap<>();

	}

	public TestProtocol execute() {
		testfile = null;
		try {
			testfile = loadTestfile(path);
		} catch (IOException e) {
			return new TestProtocol(false, "-", path, e.getMessage());
		}

		// TODO zeile in fehlerprotokoll aufgenommen. überprüfen!!!
		try {
			loadLibraries(testfile);
		} catch (AssertionError | ClassNotFoundException | KeywordLibraryException | IOException e) {
			return createProtocol(testfile, false, e.getMessage());
		}
		try {
			execute(testfile.getSetupLines());
		} catch (AssertionError | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IOException e) {
			return createProtocol(testfile, false, "Setup: " + e.getMessage());
		}
		try {
			execute(testfile.getTestLines());
		} catch (AssertionError | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IOException e) {
			return createProtocol(testfile, false, "Test: " + e.getMessage());
		}
		try {
			execute(testfile.getTeardownLines());
		} catch (AssertionError | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IOException e) {
			return createProtocol(testfile, false, "Teardown: " + e.getMessage());
		}

		return createProtocol(testfile, true, "");
	}

	private TestProtocol createProtocol(Testfile testfile, boolean passed, String message) {
		return new TestProtocol(passed, testfile.getTestname(), testfile.getPath(), message);
	}

	private void execute(String[] lines)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			executeLine(line);
		}
	}

	private Object executeLine(String line)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (line.contains("=")) {
			return assignVariableLine(line);
		} else {
			return executeKeywordLine(line);
		}
	}

	private Object assignVariableLine(String line)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
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

	private Object retrieveVariable(String var) {
		Object result = variablesMap.getOrDefault(var, null);
		if (result == null) {

		}
		return result;
	}

	/**
	 * Führt eine Zeile aus in der ein Keyword und (optional) Parameter stehen
	 * 
	 * @param line
	 *            Zeile
	 * @return Rückgabewert des Keywords
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Object executeKeywordLine(String line)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
				throw ExceptionHandler.NoSuchLibrary(libName);
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
					throw ExceptionHandler.DuplicateKeyword(strKeyword);
				}
			}
		}
		if (keyword == null) {
			throw ExceptionHandler.NoSuchKeyword(strKeyword, libName);
		}
		return keyword;
	}

	private Object[] getParameters(String parameterLine) throws IOException {
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
				throw ExceptionHandler.InvalidParameter(strPar);
			}
			res[i] = strPar;
		}
		return res;
	}

	private Testfile loadTestfile(String path) throws IOException {
		return TestfileReader.read(path);
	}

	private void loadLibraries(Testfile testfile) throws KeywordLibraryException, ClassNotFoundException, IOException {
		String[] libLines = testfile.getLibraryPaths();

		for (String line : libLines) {
			String[] split = line.split(" ");
			String path = split[0];
			String name = split[1];

			if (!Pattern.matches("\".*\"", path)) {
				throw ExceptionHandler.LibraryPathIsInvalid(path);
			}
			path = path.substring(1, path.length() - 1);

			if (!Pattern.matches("[\\d \\w]*", name)) {
				throw ExceptionHandler.LibraryNameIsInvalid(name);
			}

			KeywordLibrary library = LibraryLoader.getInstance().loadLibrary(path);

			libnamesMap.put(name, library);
		}

	}

}
