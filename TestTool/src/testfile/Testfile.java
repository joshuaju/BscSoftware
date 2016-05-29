package testfile;

import java.io.File;
import java.util.ArrayList;

import exceptions.testfile.TestfileException;

public class Testfile {
	private String author;
	private String testname;
	private String description;
	private String path;

	private ArrayList<String> libPaths;
	private ArrayList<Testline> setupLines;
	private ArrayList<Testline> testLines;
	private ArrayList<Testline> teardownLines;

	public static final String TAG_FIRST_CHAR = "[";
	public static final String TAG_AUTHOR = "[AUTHOR]";
	public static final String TAG_TESTNAME = "[TESTNAME]";
	public static final String TAG_DESCRIPTION = "[DESC]";
	public static final String TAG_LIBRARY = "[LIB]";
	public static final String TAG_SETUP = "[SETUP]";
	public static final String TAG_TEST = "[TEST]";
	public static final String TAG_TEARDOWN = "[TEARDOWN]";
	public static final String TAG_COMMENT = "#";

	Testfile() {
		author = "";
		testname = "";
		description = "";
		path = "";
		libPaths = new ArrayList<>();
		setupLines = new ArrayList<>();
		testLines = new ArrayList<>();
		teardownLines = new ArrayList<>();
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

	public Testline[] getSetupLines() {
		return setupLines.toArray(new Testline[0]);
	}

	void addSetupLine(String line, int lineNumber) throws TestfileException {
		Testline testline = new Testline(lineNumber, line);		
		setupLines.add(testline);
	}

	public boolean hasSetupLines() {
		return setupLines.size() > 0;
	}

	public Testline[] getTestLines() {
		return testLines.toArray(new Testline[0]);
	}

	void addTestLine(String line, int lineNumber) throws TestfileException {
		Testline testline = new Testline(lineNumber, line);
		testLines.add(testline);
	}

	public boolean hasTestLines() {
		return testLines.size() > 0;
	}

	public Testline[] getTeardownLines() {
		return teardownLines.toArray(new Testline[0]);
	}

	void addTeardownLine(String line, int lineNumber) throws TestfileException {
		Testline testline = new Testline(lineNumber, line);		
		teardownLines.add(testline);

	}

	public boolean hasTeardownLines() {
		return teardownLines.size() > 0;
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
			for (Testline line : setupLines) {
				builder.append("\t" + line.text + "\n");
			}
		}

		if (hasTestLines()) {
			builder.append(TAG_TEST + "\n");
			for (Testline line : testLines) {
				builder.append("\t" + line.text + "\n");
			}
		}

		if (hasTeardownLines()) {
			builder.append(TAG_TEARDOWN + "\n");
			for (Testline line : teardownLines) {
				builder.append("\t" + line.text + "\n");
			}
		}
		return builder.toString();
	}
}
