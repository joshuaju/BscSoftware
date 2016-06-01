package application;
import java.util.Properties;

import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApplication extends Application {
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Properties appProps = PropertyHelper.loadApplicationProperties(false);
		
		String[] defaultDirs = appProps.getProperty("default.dirs", "").split(",");;		
		LibraryLoader.getInstance(defaultDirs);
		
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
		
		PropertyHelper.saveApplicationProperties(appProps);
		Platform.exit();
	}
	
	

}
