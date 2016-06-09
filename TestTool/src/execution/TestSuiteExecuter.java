package execution;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import exceptions.executing.SetupException;
import exceptions.executing.TeardownException;
import exceptions.executing.TestException;
import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class TestSuiteExecuter {

	private volatile boolean abort = false;
	
	private final String author;
	private final ArrayList<String> paths;
	private ProgressHandler progressHandler;

	private DoubleProperty suiteProgress = new SimpleDoubleProperty();
	private DoubleProperty testProgress = new SimpleDoubleProperty();

	public TestSuiteExecuter(String author) {
		this.author = author;
		this.paths = new ArrayList<>();
		progressHandler = new ProgressHandler();
	}

	public TestSuiteExecuter() {
		this(System.getProperty("user.name"));
	}

	public TestSuiteProtocol execute() throws InterruptedException {
		TestSuiteProtocol suiteprotocol = new TestSuiteProtocol(author);

		progressHandler.setMax(paths.size());
		suiteProgress.bind(progressHandler.progressProperty());

		for (int i = 0; i < paths.size() && !abort; i++) {
			String tmpPath = paths.get(i);
			try {
				executePath(suiteprotocol, tmpPath);
			} catch (SetupException | TeardownException e) {
				break;
			}
			progressHandler.increment();
		}

		if (abort){
			throw new InterruptedException();
		}
		return suiteprotocol;
	}

	private void executePath(TestSuiteProtocol suiteprotocol, String tmpPath) throws SetupException, TeardownException, InterruptedException {
		TestExecuter tmpExecuter = new TestExecuter(tmpPath);

		testProgress.bind(tmpExecuter.progressProperty());

		try {
			tmpExecuter.execute();

			TestProtocol tmpProtocol = tmpExecuter.getProtocol();
			suiteprotocol.addProtocol(tmpProtocol);
		} catch (TestfileException | TestException | KeywordLibraryException e) {
			TestProtocol tmpProtocol = tmpExecuter.getProtocol();
			suiteprotocol.addProtocol(tmpProtocol);
		} catch (SetupException | TeardownException e) {
			TestProtocol tmpProtocol = tmpExecuter.getProtocol();
			suiteprotocol.addProtocol(tmpProtocol);
			throw e;
		}
	}

	public void addFile(String... filepaths) throws TestfileException, FileNotFoundException {
		for (String filepath : filepaths) {
			File file = new File(filepath);
			if (!file.isFile()) {
				throw TestfileExceptionHandler.NoSuchFile(file);
			} else if (file.getName().endsWith(".tst")) {
				this.paths.add(filepath);
			} else {
				throw TestfileExceptionHandler.InvalidFileEnding(filepath);
			}
		}
	}

	public void addDirectory(boolean recursive, String... dirpaths) throws TestfileException, FileNotFoundException {
		for (String dirpath : dirpaths) {
			File dirFile = new File(dirpath);
			if (!dirFile.isDirectory()) {
				throw TestfileExceptionHandler.NoSuchDirectory(dirFile);
			}

			File[] children = dirFile
					.listFiles((FileFilter) pathname -> pathname.isDirectory() || pathname.getName().endsWith(".tst"));
			for (File sub : children) {
				if (recursive && sub.isDirectory()) {
					addDirectory(true, sub.getAbsolutePath());
				} else if (sub.isFile()) {
					addFile(sub.getAbsolutePath());
				}
			}
		}
	}

	public ReadOnlyDoubleProperty suiteProgressProperty() {
		return suiteProgress;
	}

	public ReadOnlyDoubleProperty testProgressProperty() {
		return testProgress;
	}
	
	public void abort(){
		abort = true;
	}
}
