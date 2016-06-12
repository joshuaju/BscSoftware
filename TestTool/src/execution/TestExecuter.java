package execution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import exceptions.executing.SetupException;
import exceptions.executing.TeardownException;
import exceptions.executing.TestException;
import exceptions.keywordlibrary.KeywordException;
import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;
import exceptions.testfile.TestfileSyntaxException;
import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import external.ExecutableKeyword;
import external.ExecutableKeywordLibrary;
import external.LibraryLoader;
import input.Testfile;
import input.TestfileReader;
import input.Testline;
import input.VariableFile;
import input.VariableFileReader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TestExecuter {
	private volatile BooleanProperty abortProperty = new SimpleBooleanProperty(false);
	
	private final String DEFAULT_LIBRARIES_NAME = "std";
	private final String path;
	private VariableFile globalVariables;
	private VariableFile localVariables;

	private final HashMap<String, ExecutableKeywordLibrary> libnamesMap;

	private Testfile testfile;
	private TestProtocol protocol;

	private boolean executed;

	private ProgressHandler progressHandler;

	public TestExecuter(String path) {
		this.path = path;
		
		libnamesMap = new HashMap<>();

		globalVariables = new VariableFile();
		localVariables = new VariableFile();

		executed = false;
		progressHandler = new ProgressHandler();
	}

	/**
	 * 
	 * @throws TestfileException
	 *             Probleme beim lesen der Testdatei -> Abbruch des einzelnen
	 *             Tests
	 * @throws SetupException
	 *             Probleme in der Aufbauphase des Test -> Abbruch des gesamten
	 *             Testlauf
	 * @throws KeywordLibraryException
	 *             Probleme beim Laden der Bibliotheken -> Abbruch des einzelnen
	 *             Tests
	 * @throws TeardownException
	 *             -> Abbruch des gesamten Testlauf
	 * @throws InterruptedException 
	 * @throws TestException
	 *             -> Abbruch des einzelnen Tests
	 * @throws TestfileSyntaxException
	 */
	public void execute() throws TestfileException, KeywordLibraryException, SetupException, TeardownException, InterruptedException {	
		if (executed) {
			throw new IllegalStateException("Der Test wurde bereits ausgeführt");
		}
		executed = true;
		testfile = null;
		protocol = null;
		try {
			testfile = TestfileReader.read(path);
		} catch (TestfileSyntaxException | IOException e) {
			protocol = new TestProtocol(false, "-", path, e.getMessage());
			throw new TestfileException(e.getMessage(), e);
		}

		int teststeps = testfile.getSetupLines().length + testfile.getTestLines().length
				+ testfile.getTeardownLines().length;
		teststeps *= testfile.getRepetition();
		int max = testfile.getLibraryFilePaths().length + ((testfile.getVariableFilePaths().length > 0) ? 1 : 0)
				+ teststeps;
		progressHandler.setMax(max);
		
		try {
			loadLibraries(testfile);
		} catch (ClassNotFoundException | IOException | KeywordLibraryException e) {
			protocol = createProtocol(testfile, false, e.getMessage());
			throw new KeywordLibraryException(e.getMessage(), e);
		}		
		
		if (testfile.hasVariableFilePaths()) {
			try {
				globalVariables = VariableFileReader.readAll(path, testfile.getVariableFilePaths());
				progressHandler.increment();				
			} catch (TestfileSyntaxException | IOException e) {
				protocol = createProtocol(testfile, false, e.getMessage());
				throw new TestfileException(e.getMessage(), e);
			}
		}

		for (int round = 1; round <= testfile.getRepetition(); round++) {
			try {
				executeLines(testfile.getSetupLines());
			} catch (TestfileException | KeywordException | AssertionError e) {
				protocol = createProtocol(testfile, false, "Setup: " + e.getMessage());
				throw new SetupException(e.getMessage(), e);
			}

			try {
				executeLines(testfile.getTestLines());
			} catch (TestfileException | KeywordException | AssertionError e) {
				protocol = createProtocol(testfile, false, "Test: " + e.getMessage());
			}

			try {
				executeLines(testfile.getTeardownLines());
			} catch (TestfileException | KeywordException | AssertionError e) {
				protocol = createProtocol(testfile, false, "Teardown: " + e.getMessage());
				throw new TeardownException(e.getMessage(), e);
			}
		}
				
		if (abortProperty.get()){
			throw new InterruptedException();
		}
		if (protocol == null) {
			// Es ist kein Fehler aufgetreten
			protocol = createProtocol(testfile, true, "");			
		}
	}

	private TestProtocol createProtocol(Testfile testfile, boolean passed, String message) {
		return new TestProtocol(passed, testfile.getTestname(), testfile.getPath(), message);
	}

	public TestProtocol getProtocol() {
		if (!executed) {
			throw new IllegalStateException("Der Test wurde noch nicht ausgeführt");
		}
		return protocol;
	}

	/**
	 * Führt ein Array von Testschritte/Zeilen aus
	 * 
	 * @param lines
	 *            Testschritte/Zeilen
	 * @throws TestfileException
	 * @throws KeywordException
	 * @throws AssertionError
	 */
	private void executeLines(Testline[] lines) throws TestfileException, KeywordException, AssertionError {
		for (int i = 0; i < lines.length && !abortProperty.get(); i++) {
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
			progressHandler.increment();
		}
	}

	/**
	 * Führt einen Testschritt/Zeile aus
	 * 
	 * @param line
	 * @return
	 * @throws TestfileException
	 * @throws KeywordException
	 * @throws AssertionError
	 */
	private Object executeLine(String line) throws TestfileException, KeywordException, AssertionError {
		if (line.contains("=")) {
			return assignVariableLine(line);
		} else {
			return executeKeywordLine(line);
		}
	}

	/**
	 * Führt eine Zeile aus, in der eine Zuweisung (also: {var} = ...)
	 * stattfindet
	 * 
	 * @param line
	 * @return
	 * @throws TestfileException
	 * @throws KeywordException
	 * @throws AssertionError
	 */
	private Object assignVariableLine(String line) throws TestfileException, KeywordException, AssertionError {
		String[] split = line.split("=");

		String strVar = split[0].trim().replaceAll("[{}]", "");
		String strVal = split[1].trim();
		Object assignVal = interpretValue(strVal);

		localVariables.addVariable(strVar, assignVal.toString());
		return assignVal;
	}

	/**
	 * Interpretiert einen Wert als Variable, Wert(oder mathematischen Ausruck)
	 * oder als Keyword
	 * 
	 * @param value
	 * @return
	 * @throws TestfileException
	 * @throws KeywordException
	 * @throws AssertionError
	 */
	private Object interpretValue(String value) throws TestfileException, KeywordException, AssertionError {
		Object assignVal = null;
		if (value.startsWith("{")) { // variable
			value = value.replaceAll("[{}]", "");
			assignVal = retrieveVariable(value);
		} else if (value.startsWith("\"")) { // value
			value = value.replace("\"", "");
			if (value.startsWith("[") && value.endsWith("]")) {
				value = evaluateMathExpression(value).toString();
			}
			assignVal = value;
		} else { // keyword
			assignVal = executeKeywordLine(value);
		}
		return assignVal;
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

		ExecutableKeyword keyword = findKeyword(strKeyword);
		Object[] args = getParameters(parLine);

		return keyword.invoke(args);
	}

	/**
	 * Ermittelt den Wert zu einer gespeicherten Variabel. Es wird zuerst in den
	 * Testvariablen, und dann in den globalen Variablen nachgesehen.
	 * 
	 * @param var
	 *            Name der Variabel (ohne geschweifte Klammern)
	 * @return Wert der Variablen
	 * @throws TestfileException
	 *             Die Variable gibt es nicht, bzw. ist gleich NULL
	 */
	private Object retrieveVariable(String var) throws TestfileException {
		Object result = null;
		try {
			result = localVariables.getValueFor(var);
		} catch (TestfileException e) {
			result = globalVariables.getValueFor(var);
		}
		return result;
	}

	/**
	 * Wertet einen mathematischen Ausdruck aus. Das erste und letzte Zeichen
	 * muss eine ecke Klammer auf/zu sein.
	 * 
	 * @param expression
	 *            Mathematischer Ausdruck
	 * @return Der Berechnete Wert als double
	 * @throws TestfileException
	 *             Wenn der Ausdruck fehler enthält
	 */
	private Object evaluateMathExpression(String expression) throws TestfileException {
		if (!(expression.startsWith("[") && expression.endsWith("]"))) {
			throw TestfileExceptionHandler.InvalidMathExpression(expression, null);
		}

		expression = expression.substring(1, expression.length() - 1);
		int varBegin = expression.indexOf("{");
		while (varBegin != -1) { // Ersetzet alle {Variablen} durch Werte
			int varEnd = expression.indexOf("}", varBegin);
			String varName = expression.substring(varBegin + 1, varEnd);
			Object varValue = retrieveVariable(varName);
			expression = expression.replace("{" + varName + "}", varValue.toString());
			varBegin = expression.indexOf("{");
		}

		try {
			Expr evaluated = Parser.parse(expression);
			expression = "" + evaluated.value();
		} catch (SyntaxException e) {
			throw TestfileExceptionHandler.InvalidMathExpression(expression, e);
		}

		if (expression.endsWith(".0")) {
			// Wenn der Wert ganzahlig ist '.0' abschneiden, damit der Wert als
			// Integer interpretiert werden kann.
			expression = expression.substring(0, expression.length() - 2);
		}
		return expression;
	}

	/**
	 * Sucht ein Keyword in allen in der Testdatei angegeben und standardmäßig
	 * geladenen Bibliotheken. Wenn ein Bibliotheksname vorangestellt ist, dann
	 * wird nur in dieser gesucht.
	 * 
	 * @param keywordname
	 *            Keyword, (optional) mit Bibliotheksname davor
	 * @return Das ermittelte Keyword
	 * @throws TestfileException
	 *             Das Keyword wurde nicht gefunden, oder es kommt in mehreren
	 *             Bibliotheken vor
	 */
	private ExecutableKeyword findKeyword(String keywordname) throws TestfileException {
		String libraryname = "";
		int dotIndex = keywordname.indexOf(".");
		if (dotIndex != -1) {
			libraryname = keywordname.substring(0, dotIndex);
			keywordname = keywordname.substring(dotIndex + 1);
		}
		ArrayList<ExecutableKeywordLibrary> availableLibs = getAvailableLibraries(libraryname);
		ExecutableKeyword keyword = getExecutableKeyword(libraryname, keywordname, availableLibs);
		return keyword;
	}

	/**
	 * Gibt die verfügbaren Bibliotheken zurück. Kein Name bedeuted, dass alle
	 * Standard und im Testfall geladenen Bibliotheken zurückgegeben werden.
	 * Ansonsten wird nur die Bibliothek mit dem Bezeichner geladen. Wenn der
	 * Standard-Bezeichner verwendet wird, dann werden alle
	 * Standard-Bibliotheken geladen.
	 * 
	 * @param libraryname
	 *            Bezeichner der Bibliothek
	 * @return verfügbare Bibliotheken in denen nach einem Schlüsselwort gesucht
	 *         werden kann
	 * @throws TestfileException
	 */
	private ArrayList<ExecutableKeywordLibrary> getAvailableLibraries(String libraryname) throws TestfileException {
		ArrayList<ExecutableKeywordLibrary> availableLibs = new ArrayList<>();
		if (libraryname == null || libraryname.length() == 0) {
			availableLibs.addAll(LibraryLoader.getInstance().getDefaultLibraries());
			availableLibs.addAll(libnamesMap.values());
		} else {
			if (libraryname.equals(DEFAULT_LIBRARIES_NAME)) {
				availableLibs.addAll(LibraryLoader.getInstance().getDefaultLibraries());
			} else {
				ExecutableKeywordLibrary lib = libnamesMap.getOrDefault(libraryname, null);
				if (lib == null) {
					throw TestfileExceptionHandler.NoLibraryForThisName(libraryname);
				}
				availableLibs.add(lib);
			}
		}
		return availableLibs;
	}

	/**
	 * Sucht das Schlüsselwort in den verfügbaren Bibliotheken.
	 * 
	 * @see TestExecuter#getAvailableLibraries(String)
	 * @param libraryname
	 *            Bibliotheks-Bezeichner (kann auch leer sein)
	 * @param keywordname
	 *            Name des Schlüsselworts
	 * @param libraries
	 *            Bibliotheken in den gesucht wird
	 * @return
	 * @throws TestfileException
	 *             Das Keyword kommt in mehreren Bibliotheke vor, oder es wurde
	 *             nicht gefunden
	 */
	private ExecutableKeyword getExecutableKeyword(String libraryname, String keywordname,
			ArrayList<ExecutableKeywordLibrary> libraries) throws TestfileException {
		ExecutableKeyword finalKeyword = null;

		for (ExecutableKeywordLibrary tmpLibrary : libraries) {
			ExecutableKeyword tmpKeyword = tmpLibrary.getKeywordByName(keywordname);
			if (tmpKeyword != null) {
				if (finalKeyword != null) {
					throw TestfileExceptionHandler.DuplicatedKeyword(tmpKeyword);
				}
				finalKeyword = tmpKeyword;
			}
		}

		if (finalKeyword == null) {
			throw TestfileExceptionHandler.NoSuchKeywordInLibrary(libraryname, keywordname);
		}
		return finalKeyword;
	}

	/**
	 * Spaltet einen String mit durch Komma getrennten Parametern in die
	 * einzelnen Paramter auf
	 * 
	 * @param parameterLine
	 * @return
	 * @throws TestfileException
	 */
	private Object[] getParameters(String parameterLine) throws TestfileException {
		if (parameterLine.length() == 0) {
			return new Object[0];
		}
		String[] parStrArray = parameterLine.split(",");
		Object[] res = new Object[parStrArray.length];
		for (int i = 0; i < parStrArray.length; i++) {
			String strPar = parStrArray[i].toString().trim();

			Object value = null;
			try {
				value = interpretValue(strPar);
			} catch (KeywordException | AssertionError e) {
				throw TestfileExceptionHandler.InvalidParameter(strPar);
			}
			res[i] = value;
		}
		return res;
	}

	/**
	 * Läd alle Schlüsselwortbibliotheken der Testdatei
	 * 
	 * @param testfile
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws KeywordLibraryException
	 */
	private void loadLibraries(Testfile testfile) throws ClassNotFoundException, IOException, KeywordLibraryException {
		String[] libLines = testfile.getLibraryFilePaths();

		for (String line : libLines) {
			int last = line.lastIndexOf("\"");
			String path = line.substring(1, last);
			String name = line.substring(last + 1).trim();
			ExecutableKeywordLibrary library = LibraryLoader.getInstance().loadInstantiatedKeywordLibrary(path);
			libnamesMap.put(name, library);
			progressHandler.increment();
		}
	}

	public ReadOnlyDoubleProperty progressProperty() {
		return progressHandler.progressProperty();
	}
	
	public void abort(){
		abortProperty.set(true);
	}
	
	BooleanProperty abortProperty(){
		return abortProperty;
	}
	
}
