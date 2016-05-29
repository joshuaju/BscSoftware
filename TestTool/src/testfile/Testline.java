package testfile;

import java.util.regex.Pattern;

import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;

public class Testline {

	public final int number;
	public final String text;

	public Testline(int number, String text) throws TestfileException {
		this.number = number;
		this.text = text.trim();
		checkLine(text);
	}

	private void checkLine(String text) throws TestfileException {
		String strKeyword = null;
		String strParameter = null;

		if (text.contains("=")) {
			String[] split = text.split("=");
			strKeyword = split[0].trim();
			strParameter = split[1].trim();
			checkAssignLine(strKeyword, strParameter);
		} else {
			int valIndex = text.indexOf("\"");
			int varIndex = text.indexOf("{");
			int min;
			if (valIndex == -1 && varIndex == -1) {
				min = text.length();
			} else if (valIndex == -1) {
				min = varIndex;
			} else if (varIndex == -1) {
				min = valIndex;
			} else {
				min = Math.min(valIndex, varIndex);
			}
			strKeyword = text.substring(0, min);
			strParameter = text.substring(min);
			checkRegularLine(strKeyword, strParameter);
		}
	}

	// private String REGEX_KEYWORD_LINE_VALID = "[\\d \\w \\s \" = \\{ \\} ,
	// \\. ß äöü ÄÖÜ]*";

	private void checkAssignLine(String variable, String value) throws TestfileException {
		// Variablen haben links und rechts eine geschwungene Klammer,
		// dazwischen sind Buchstaben und Zahlen erlaubt

		if (!checkVariable(variable)) {
			throw TestfileExceptionHandler.InvalidAssignVariable(number, variable);
		}
		// Eine Variablen wird entweder einer Variable, einem Wert oder Keyword
		// zugewiesen
		if (!checkValue(value) && !checkVariable(value)) {
			try {
				// auf der rechten Seite des '=' Zeichen steht ein Keyword,
				// welches überprüft wird
				checkLine(value);
			} catch (TestfileException e) {
				throw TestfileExceptionHandler.InvalidAssignValue(number, value);
			}
		}
	}

	private void checkRegularLine(String keyword, String args) throws TestfileException {
		if (!checkKeyword(keyword)) {
			throw TestfileExceptionHandler.InvalidKeyword(number, keyword);
		}

		String[] argsArray;
		if (args.length() == 0) {
			return;
		} else if (!args.contains(",")) {
			argsArray = new String[] { args };
		} else {
			argsArray = args.split(",");
		}
		for (int i = 0; i < argsArray.length; i++) {
			String tmpArg = argsArray[i].trim();
			if (!checkValue(tmpArg) && !checkVariable(tmpArg)) {
				throw TestfileExceptionHandler.InvalidKeywordArgument(number, i, tmpArg);
			}
		}
	}

	private boolean checkVariable(String var) {
		String REGEX_VARIABLE = "[{][\\w \\d \\s ß äöü ÄÖÜ]*[}]";
		if (!Pattern.matches(REGEX_VARIABLE, var)) {
			return false;
		}
		return true;
	}

	private boolean checkValue(String value) {
		String REGEX_VALUE = "\"[\\w \\d \\s \\. ß äöü ÄÖÜ]*\"";
		if (!Pattern.matches(REGEX_VALUE, value)) {
			return false;
		}
		return true;
	}

	private boolean checkKeyword(String keyword) {
		String REGEX_KEYWORD = "[\\w \\d \\s \\. ß äöü ÄÖÜ]*";
		if (!Pattern.matches(REGEX_KEYWORD, keyword)) {
			return false;
		}
		return true;
	}

}
