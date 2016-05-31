package testfile.syntaxer;

import java.util.regex.Pattern;

import exceptions.testfile.TestfileSyntaxException;
import testfile.Testfile;

/**
 * Die Klasse prüft eine Testdatei auf syntaktische und lexikalische
 * Korrektheit. Eine semantische Prüfung findet nicht statt.
 * 
 * @author JJungen
 *
 */
public class TestfileSyntaxer {

	public static final String TAG_FIRST_CHAR = "[";
	public static final String TAG_AUTHOR = "[AUTHOR]";
	public static final String TAG_TESTNAME = "[TESTNAME]";
	public static final String TAG_DESCRIPTION = "[DESC]";
	public static final String TAG_LIBRARY_FILE = "[LIB]";
	public static final String TAG_VARIABLE_FILE = "[VAR]";
	public static final String TAG_SETUP = "[SETUP]";
	public static final String TAG_TEST = "[TEST]";
	public static final String TAG_TEARDOWN = "[TEARDOWN]";
	public static final String TAG_REPEAT = "[REPEAT]";
	public static final String TAG_COMMENT = "#";

	public static final String OPERATOR_ASSIGN = "=";
	public static final String SPLIT_ARGUMENT = ",";

	public void checkVariableLines(String...lines) throws TestfileSyntaxException{
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++){
			String line = lines[lineIndex].trim();
			if (line.length() == 0 || line.startsWith(TAG_COMMENT)){
				// ignore
			} else {
				try {
					checkAssign(line, false);
				} catch (TestfileSyntaxException e) {
						throw new TestfileSyntaxException(lineIndex+1, e.getMessage());
				}
			}			
		}
		
	}
	
	public void check(String... lines) throws TestfileSyntaxException {
		int lineIndex = 0;
		try {
		for (lineIndex = 0; lineIndex < lines.length; lineIndex++) {			
			String line = lines[lineIndex].trim();

			if (line.startsWith(Testfile.TAG_AUTHOR) || line.startsWith(Testfile.TAG_TESTNAME)) {
				// Nothing to check
			} else if (line.startsWith(Testfile.TAG_DESCRIPTION)) { // description
				for (; lineIndex < lines.length - 1; lineIndex++) {
					line = lines[lineIndex+1].trim();
					// Nothing to check
					if (line.startsWith(Testfile.TAG_FIRST_CHAR)) {
						break;
					}
				}
			} else if (line.startsWith(Testfile.TAG_LIBRARY_FILE)) { // library
				checkLibraryFileLine(line);
			} else if (line.startsWith(Testfile.TAG_VARIABLE_FILE)) {
				checkVariableFileLine(line);
			} else if (line.startsWith(Testfile.TAG_REPEAT)) { // repeat
				checkRepeatLine(line);
			} else if (line.startsWith(Testfile.TAG_SETUP) || line.startsWith(Testfile.TAG_TEST)
					|| line.startsWith(Testfile.TAG_TEARDOWN)) { // setup
				checkEmptyLine(line);
				for (; lineIndex < lines.length - 1; lineIndex++) {
					line = lines[lineIndex + 1].trim();
					if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
						continue;
					} else if (line.startsWith(TAG_FIRST_CHAR)) {
						break;
					} else {
						try {
						checkTestStepLine(line);
						} catch (TestfileSyntaxException e){
							lineIndex++;
							throw e;
						}
					}
				}
			} else if (line.length() == 0 || line.startsWith(Testfile.TAG_COMMENT)) {
				// comment or empty row -> ignore
			} else {
				// unknown line
				throw TestfileSyntaxException.InvalidLine();
			}
		}
		} catch (TestfileSyntaxException e){
			throw new TestfileSyntaxException((lineIndex+1), e.getMessage());
		}
	}

	private void checkEmptyLine(String line) throws TestfileSyntaxException {
		line = line.replaceAll("\\[.*\\]", "").trim(); // remove any tag
		if (line.length() > 0) {
			throw TestfileSyntaxException.InvalidLine();
		}
	}

	private void checkLibraryFileLine(String line) throws TestfileSyntaxException {
		line = line.replace(TAG_LIBRARY_FILE, "").trim();
		if (!Pattern.matches("\".*\" [\\w\\d]+", line)) {
			throw TestfileSyntaxException.InvalidLibraryFile();
		}
	}

	private void checkVariableFileLine(String line) throws TestfileSyntaxException {
		line = line.replace(TAG_VARIABLE_FILE, "").trim();
		if (!Pattern.matches("\".*\"", line)) {
			throw TestfileSyntaxException.InvalidVariableFile();
		}
	}

	private void checkRepeatLine(String line) throws TestfileSyntaxException {
		line = line.replace(TAG_REPEAT, "").trim();
		if (!Pattern.matches("[0-9]*", line)) {
			throw TestfileSyntaxException.InvalidArgForRepeat();
		}
	}

	private void checkTestStepLine(String line) throws TestfileSyntaxException {
		if (line.contains(OPERATOR_ASSIGN)) {
			checkAssign(line, true);
		} else {
			checkRegular(line);
		}
	}

	/**
	 * Prüft, ob eine Zuweisung gültig ist
	 * 
	 * @param line
	 * @throws TestfileSyntaxException
	 */
	private void checkAssign(String line, boolean keywordAllowed) throws TestfileSyntaxException {
		String[] split = line.split(OPERATOR_ASSIGN);
		if (split.length != 2) {
			throw TestfileSyntaxException.InvalidAssignLine();
		}

		String left = split[0].trim();
		String right = split[1].trim();
		// links muss variable sein
		if (!checkVariable(left)) {
			throw TestfileSyntaxException.InvalidVarToAssign();
		}
		// rechts darf alles sein
		if (!(checkArgument(right) || (keywordAllowed && checkKeyword(right)))) {
			throw TestfileSyntaxException.InvalidArgForAssign();
		}
	}

	/**
	 * Prüft, ob eine regüläre Zeile in Ordnung ist. Eine regüläre Zeile enthält
	 * zuerst ein Keyword und dann optional Argumente
	 * 
	 * @param line
	 * @throws TestfileSyntaxException
	 */
	private void checkRegular(String line) throws TestfileSyntaxException {
		int firstValIndex = line.indexOf("\"");
		int firstVarIndex = line.indexOf("{");

		int firstArg;
		if (firstValIndex == -1 && firstVarIndex == -1) {
			firstArg = line.length();
		} else if (firstValIndex == -1 || firstVarIndex == -1) {
			firstArg = Math.max(firstValIndex, firstVarIndex);
		} else {
			firstArg = Math.min(firstValIndex, firstVarIndex);
		}

		String keyword = line.substring(0, firstArg).trim();
		if (!checkKeyword(keyword)) {
			throw TestfileSyntaxException.InvalidKeyword();
		}

		String parLine = line.substring(firstArg).trim();
		if (parLine.length() > 0) {
			String[] args = parLine.split(",");
			int argNumber = checkArguments(args);
			if (argNumber < args.length) {
				throw TestfileSyntaxException.InvalidArgForKeyword(argNumber);
			}
		}
	}

	/**
	 * Prüft, ob ein String ein Keyword ist
	 * 
	 * @param keyword
	 * @return
	 */
	private boolean checkKeyword(String keyword) {
		String libName = null;
		if (keyword.contains(".")) {
			String[] split = keyword.split("\\.");
			if (split.length != 2) {
				return false;
			}
			libName = split[0];
			keyword = split[1];
		}

		if (libName != null && !checkLibraryName(libName)) {
			return false;
		}

		String REGEX_KEYWORD = "[\\w \\d \\s \\. ß äöü ÄÖÜ]{1,}";
		if (!Pattern.matches(REGEX_KEYWORD, keyword)) {
			return false;
		}
		return true;
	}

	/**
	 * Prüft, ob ein String ein Bibliotheksname ist
	 * 
	 * @param name
	 * @return
	 */
	private boolean checkLibraryName(String name) {
		if (Pattern.matches("[\\d\\w]{1,}", name)) {
			return true;
		}
		return false;
	}

	/**
	 * Prüft, ob ein String eine Variable ist. Ein Variable steht immer in
	 * geschwungenen Klammern
	 * 
	 * @param var
	 * @return
	 */
	private boolean checkVariable(String var) {
		if (Pattern.matches("[{].+[}]", var)) {
			var = var.substring(1, var.length()-1);
			if (!(var.contains("{") || var.contains("}"))){				
				return true;
			}			
		}
		return false;
	}

	/**
	 * Prüft, ob ein String ein Wert ist. Ein Wert steht immer in doppelten
	 * Anführungszeichen
	 * 
	 * @param val
	 * @return
	 */
	private boolean checkValue(String val) {
		if (Pattern.matches("\".*\"", val)) {
			val = val.substring(1, val.length()-1);
			if (!val.contains("\"")){
				return true;
			}			
		}
		return false;
	}

	/**
	 * Prüft, ob ein String ein Argument ein Wert oder eine Variable ist
	 * 
	 * @param arg
	 * @return
	 */
	private boolean checkArgument(String arg) {
		arg = arg.trim();
		if (checkVariable(arg) || checkValue(arg)) {
			return true;
		}
		return false;
	}

	/**
	 * Überprüft ein Feld von Argumenten
	 * 
	 * @see TestfileSyntaxer#checkArgument(String)
	 * @param args
	 * @return Bis zu welchem Argument kein Fehler aufgetreteb ist, oder die
	 *         Länge des Arrays wenn kein Fehler aufgetreten ist
	 */
	private int checkArguments(String[] args) {
		for (int argNumber = 0; argNumber < args.length; argNumber++) {
			String arg = args[argNumber].trim();
			if (!checkArgument(arg)) {
				return argNumber;
			}
		}
		return args.length;
	}

}
