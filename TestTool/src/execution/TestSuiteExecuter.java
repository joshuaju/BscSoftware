package execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.executing.SetupException;
import exceptions.executing.TeardownException;
import exceptions.executing.TestException;
import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileException;
import exceptions.testfile.TestfileExceptionHandler;
import exceptions.testfile.TestfileSyntaxException;

public class TestSuiteExecuter {

	private final String author;
	private final ArrayList<String> paths;

	public TestSuiteExecuter(String author) {
		this.author = author;
		this.paths = new ArrayList<>();
	}
	
	public TestSuiteExecuter(){
		this(System.getProperty("user.name"));
	}
	

	public void addPath(String... paths) {
		this.paths.addAll(Arrays.asList(paths));
	}

	/**
	 * Fügt alle Testdateien mit der Dateiendung '*.tst' aus dem Verzeichnis
	 * hinzu
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public void addDirectory(String path) throws FileNotFoundException {
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			throw TestfileExceptionHandler.NoSuchDirectory(dir);
		}

		File[] files = dir.listFiles((currDir, currFile) -> currFile.endsWith(".tst"));
		ArrayList<String> filePaths = new ArrayList<>();
		for (File f : files) {
			filePaths.add(f.getAbsolutePath());
		}
		this.paths.addAll(filePaths);
	}

	public TestSuiteProtocol execute() throws TestfileSyntaxException {
		TestSuiteProtocol suiteprotocol = new TestSuiteProtocol(author);		
		for (String tmpPath : paths) {
			TestExecuter tmpExecuter = new TestExecuter(tmpPath);
						
			try {
				tmpExecuter.execute();
				TestProtocol tmpProtocol = tmpExecuter.getProtocol();
				suiteprotocol.addProtocol(tmpProtocol);
			} catch (TestfileException | TestException | KeywordLibraryException e) {
				// Einzelnen Test abrrechen
				TestProtocol tmpProtocol = tmpExecuter.getProtocol();
				suiteprotocol.addProtocol(tmpProtocol);				
				continue;
			} catch (SetupException | TeardownException e){
				// Gesamten Testlauf abbrechen
				TestProtocol tmpProtocol = tmpExecuter.getProtocol();
				suiteprotocol.addProtocol(tmpProtocol);				
				break;
			}															
		}

		return suiteprotocol;
	}

}
