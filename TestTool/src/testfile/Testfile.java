package testfile;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import exceptions.TestfileSyntaxException;

public class Testfile {
	private String author;
	private String testname;
	private String description;
	private String path;

	private ArrayList<String> libPaths;
	private ArrayList<String> setupLines;
	private ArrayList<String> testLines;
	private ArrayList<String> teardownLines;

	private ArrayList<Integer> setupLineNumber;
	private ArrayList<Integer> testLineNumber;
	private ArrayList<Integer> teardownLineNumber;

	public static final String TAG_FIRST_CHAR = "[";
	public static final String TAG_AUTHOR = "[AUTHOR]";
	public static final String TAG_TESTNAME = "[TESTNAME]";
	public static final String TAG_DESCRIPTION = "[DESC]";
	public static final String TAG_LIBRARY = "[LIB]";
	public static final String TAG_SETUP = "[SETUP]";
	public static final String TAG_TEST = "[TEST]";
	public static final String TAG_TEARDOWN = "[TEARDOWN]";
	public static final String TAG_COMMENT = "#";

	private static final String REGEX_KEYWORD_LINE_VALID = "[\\d \\w \\s \" = \\{ \\} , \\. ß äöü ÄÖÜ]*";

	Testfile() {
		author = "";
		testname = "";
		description = "";
		path = "";
		libPaths = new ArrayList<>();
		setupLines = new ArrayList<>();
		testLines = new ArrayList<>();
		teardownLines = new ArrayList<>();
		setupLineNumber = new ArrayList<>();
		testLineNumber = new ArrayList<>();
		teardownLineNumber = new ArrayList<>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(File f) {
		this.path = f.getAbsolutePath();
	}

	public String getAuthor() {
		return author;
	}

	void setAuthor(String author) {
		this.author = author;
	}

	public boolean hasAuthor() {
		return author.length() > 0;
	}

	public String getTestname() {
		return testname;
	}

	void setTestname(String testname) {
		this.testname = testname;
	}

	public boolean hasTestname() {
		return testname.length() > 0;
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public boolean hasDescription() {
		return description.length() > 0;
	}

	public String[] getLibraryPaths() {
		return libPaths.toArray(new String[0]);
	}

	void addLibraryPath(String path) {
		libPaths.add(path);
	}

	public boolean hasLibraryPaths() {
		return libPaths.size() > 0;
	}

	// TODO Line-Objekt zurückgeben: {String line, int linenumber }, damit das
	// Protokoll besser wird
	public String[] getSetupLines() {
		return setupLines.toArray(new String[0]);
	}

	void addSetupLine(String line, int lineNumber) throws TestfileSyntaxException {
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)) {
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		setupLines.add(line);
		addSetupLineNumber(lineNumber);
	}

	public boolean hasSetupLines() {
		return setupLines.size() > 0;
	}

	public Integer getSetupLineNumber(int index) {
		return setupLineNumber.get(index);
	}

	private void addSetupLineNumber(int number) {
		this.setupLineNumber.add(number);
	}

	public String[] getTestLines() {
		return testLines.toArray(new String[0]);
	}

	void addTestLine(String line, int lineNumber) throws TestfileSyntaxException {
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)) {
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		testLines.add(line);
		addTestLineNumber(lineNumber);
	}

	public boolean hasTestLines() {
		return testLines.size() > 0;
	}

	public Integer getTestLineNumber(int index) {
		return testLineNumber.get(index);
	}

	private void addTestLineNumber(int number) {
		this.testLineNumber.add(number);
	}

	public String[] getTeardownLines() {
		return teardownLines.toArray(new String[0]);
	}

	void addTeardownLine(String line, int lineNumber) throws TestfileSyntaxException {
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)) {
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		teardownLines.add(line);
		addTeardownLineNumber(lineNumber);
	}

	public boolean hasTeardownLines() {
		return teardownLines.size() > 0;
	}

	public Integer getTeardownLineNumber(int index) {
		return teardownLineNumber.get(index);
	}

	private void addTeardownLineNumber(int number) {
		this.teardownLineNumber.add(number);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(TAG_AUTHOR + "\t" + getAuthor() + "\n");
		builder.append(TAG_TESTNAME + "\t" + getTestname() + "\n");
		builder.append(TAG_DESCRIPTION + "\t" + getDescription() + "\n");

		if (hasLibraryPaths()) {
			for (String path : libPaths) {
				builder.append(TAG_LIBRARY + "\t" + path + "\n");
			}
		}

		if (hasSetupLines()) {
			builder.append(TAG_SETUP + "\n");
			for (String line : setupLines) {
				builder.append("\t" + line + "\n");
			}
		}

		if (hasTestLines()) {
			builder.append(TAG_TEST + "\n");
			for (String line : testLines) {
				builder.append("\t" + line + "\n");
			}
		}

		if (hasTeardownLines()) {
			builder.append(TAG_TEARDOWN + "\n");
			for (String line : teardownLines) {
				builder.append("\t" + line + "\n");
			}
		}
		return builder.toString();
	}
}
