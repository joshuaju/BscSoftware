import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;

public class Application {

	public static void main(String[] args) throws Exception {
		LibraryLoader.getInstance("D:/Bsc/default/");		
		
		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");		

		//		tsExe.addDirectory("testfiles/");
		
		tsExe.addPath("testfiles/test1.tst");		
//		tsExe.addPath("testfiles/test2.tst");		
//		tsExe.addPath("testfiles/test3.tst");
				
		TestSuiteProtocol tsProtocol = tsExe.execute();
		
		System.out.println(tsProtocol);
	}

}
