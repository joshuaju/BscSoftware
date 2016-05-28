import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;

public class Application {

	public static void main(String[] args) throws Exception {

//		KeywordLibrary lib = LibraryLoader.loadLibrary("D:/TCUIVKeywordLibrary.jar");
//		Keyword kw = lib.getKeywordByName("Lese Kanal A");
//		kw.invoke();
		
		LibraryLoader.getInstance("D:/Bsc/default/");		
		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");		
		tsExe.addPath("testfiles/test.tst");
		TestSuiteProtocol tsProtocol = tsExe.execute();
		System.out.println(tsProtocol);
		
//		tsExe.addDirectory("U:/Developement/Java/BscSoftware/BscSoftware/TestTool/testfiles/");
		// String path = "E:/DeviceKeywordLibrary.jar";
		// KeywordLibrary lib = LibraryLoader.loadLibrary(path,
		// "DeviceKeywordLibrary");
		// String author = lib.getAuthor();
		// String desc = lib.getDescription();
		//
		// System.out.println("Author: " + author);
		// System.out.println("Description: " + desc);
		//
		// Keyword kw = lib.getKeywordByName("Setze Wert auf");
		// System.out.println("Keyword gefunden? " + kw != null);
		// System.out.println(kw);
		// kw.invoke("2");
		//
		// kw = lib.getKeywordByName("Lese Wert");
		// System.out.println(kw);
		//
		// System.out.println(kw.invoke());
	}

}
