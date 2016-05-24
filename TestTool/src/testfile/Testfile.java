package testfile;

import java.util.ArrayList;
import java.util.regex.Pattern;

import exceptions.TestfileSyntaxException;

public class Testfile {
	private String author;
	private String testname;
	private String description;

	private ArrayList<String> libPaths;
	private ArrayList<String> setupLines;
	private ArrayList<String> testLines;
	private ArrayList<String> teardownLines;

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
		libPaths = new ArrayList<>();
		setupLines = new ArrayList<>();
		testLines = new ArrayList<>();
		teardownLines = new ArrayList<>();
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

	public String[] getSetupLines() {
		return setupLines.toArray(new String[0]);
	}

	void addSetupLine(String line) throws TestfileSyntaxException {		
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)){
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		setupLines.add(line);
	}

	public boolean hasSetupLines() {
		return setupLines.size() > 0;
	}

	public String[] getTestLines() {
		return testLines.toArray(new String[0]);
	}

	void addTestLine(String line) throws TestfileSyntaxException {
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)){
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		testLines.add(line);
	}

	public boolean hasTestLines() {
		return testLines.size() > 0;
	}

	public String[] getTeardownLines() {
		return teardownLines.toArray(new String[0]);
	}

	void addTeardownLine(String line) throws TestfileSyntaxException {
		if (!Pattern.matches(REGEX_KEYWORD_LINE_VALID, line)){
			throw new TestfileSyntaxException("Ungültiges Zeichen");
		}
		teardownLines.add(line);
	}

	public boolean hasTeardownLines() {
		return teardownLines.size() > 0;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		builder.append(TAG_AUTHOR + "\t" + getAuthor() + "\n");
		builder.append(TAG_TESTNAME + "\t" + getTestname() + "\n");
		builder.append(TAG_DESCRIPTION + "\t" + getDescription() + "\n");
		
		if (hasLibraryPaths()){
			for (String path: libPaths){
				builder.append(TAG_LIBRARY + "\t" + path + "\n");
			}
		}
		
		if (hasSetupLines()){
			builder.append(TAG_SETUP + "\n");
			for (String line: setupLines){
				builder.append("\t" + line + "\n");
			}
		}
		
		if (hasTestLines()){
			builder.append(TAG_TEST + "\n");
			for (String line: testLines){
				builder.append("\t" + line + "\n");
			}
		}
		
		if (hasTeardownLines()){
			builder.append(TAG_TEARDOWN + "\n");
			for (String line: teardownLines){
				builder.append("\t" + line + "\n");
			}
		}
		return builder.toString();
	}
}
