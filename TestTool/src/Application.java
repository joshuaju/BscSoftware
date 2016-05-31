import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;

public class Application {

	public static void main(String[] args) throws Exception {
//		TestfileSyntaxer syntaxer = new TestfileSyntaxer();
//		syntaxer.check(".Setze Wert");
		
		
//		TestfileReader.read("testfiles/test.tst");
//
//		System.out.println("DONE");
		LibraryLoader.getInstance("D:/Bsc/default/");				
		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");		

//      --------------------------------------------------------
//		tsExe.addDirectory("testfiles/tcu/");				
//		tsExe.addPath("testfiles/tcu/tcu_testsignal_rotor.tst");
//		tsExe.addPath("testfiles/tcu/tcu_alarm_test.tst");						
//		tsExe.addPath("testfiles/tcu/tcu_testsignal_tcu.tst");
//      --------------------------------------------------------
		tsExe.addPath("testfiles/test.tst");
//      --------------------------------------------------------
				
		TestSuiteProtocol tsProtocol = tsExe.execute();		
		System.out.println(tsProtocol);
	}

}
