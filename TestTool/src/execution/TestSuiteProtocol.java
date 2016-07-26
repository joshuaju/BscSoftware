package execution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Store information about a testsuite execution
 * @author JJungen
 *
 */
public class TestSuiteProtocol {

	public static final String STATE_PASS = "PASS";
	public static final String STATE_FAIL = "FAIL";

	public static final String TAG_AUTHOR = "[AUTHOR]";
	public static final String TAG_DATE = "[DATE]";
	public static final String TAG_PASS = "[" + STATE_PASS + "]";
	public static final String TAG_FAIL = "[" + STATE_FAIL + "]";
	public static final String TAG_STATE = "[STATE]";

	private final String author;
	private final String date;
	private final ArrayList<TestProtocol> protocols;
	private String state = null;

	public TestSuiteProtocol(String author) {
		this.author = author;

		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		date = dateFormat.format(c.getTime());

		protocols = new ArrayList<>();
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public String getState() {
		if (state == null) {
			boolean passed = true;
			for (TestProtocol p : protocols) {
				if (!p.hasPassed()) {
					passed = false;
					break;
				}
			}
			state = (passed) ? STATE_PASS : STATE_FAIL;
		}
		return state;
	}

	public void addProtocol(TestProtocol protocol) {
		protocols.add(protocol);
	}

	public void writeToFile(File destination) throws IOException {

		File parent = destination.getParentFile();
		parent.mkdirs();

		destination.createNewFile();
				
		FileWriter w = new FileWriter(destination);
		BufferedWriter writer = new BufferedWriter(w);
		writer.write(this.toString());
		writer.flush();
		writer.close();
		destination.setWritable(false);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(TAG_AUTHOR + "\t" + author + "\n");
		builder.append(TAG_DATE + "\t\t" + date + "\n\n");
		builder.append(TAG_STATE + "\t\t" + getState() + "\n\n");

		StringBuilder failed = new StringBuilder();
		StringBuilder passed = new StringBuilder();
		for (TestProtocol tmpProtocol : protocols) {
			if (tmpProtocol.hasPassed()) {
				passed.append(tmpProtocol + "\n");
			} else {
				failed.append(tmpProtocol + "\n");
			}
		}

		builder.append(failed + "\n");

		builder.append(passed);
		return builder.toString();
	}

}
