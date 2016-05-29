package execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.testfile.TestfileExceptionHandler;

public class TestSuiteExecuter {

	private final String author;
	private final ArrayList<String> paths;	

	public TestSuiteExecuter(String author) {
		this.author = author;		
		this.paths = new ArrayList<>();		
	}
	
	public void addPath(String... paths){
		this.paths.addAll(Arrays.asList(paths));
	}
	
	/**
	 * Fügt alle Testdateien mit der Dateiendung '*.tst' aus dem Verzeichnis hinzu
	 * @param path
	 * @throws FileNotFoundException
	 */
	public void addDirectory(String path) throws FileNotFoundException{
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()){
			throw TestfileExceptionHandler.NoSuchDirectory(dir);			
		}		
		
		File[] files = dir.listFiles((currDir, currFile) -> currFile.endsWith(".tst"));
		ArrayList<String> filePaths = new ArrayList<>();
		for (File f: files){
			filePaths.add(f.getAbsolutePath());
		}
		this.paths.addAll(filePaths);		
	}

	public TestSuiteProtocol execute() {
		TestSuiteProtocol suiteprotocol = new TestSuiteProtocol(author);

		for (String tmpPath : paths) {
			TestExecuter tmpExecuter = new TestExecuter(tmpPath);
			TestProtocol tmpProtocol = tmpExecuter.execute();
			suiteprotocol.addProtocol(tmpProtocol);
		}

		return suiteprotocol;
	}

}
