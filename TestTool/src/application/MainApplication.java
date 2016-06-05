package application;

import java.io.IOException;
import java.util.Properties;

import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileSyntaxException;
import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

	// public static void main(String[] args){
	// launch(args);
	// }

	private static Properties appProperties;

	@Override
	public void start(Stage primaryStage) throws IOException {
		appProperties = PropertyHelper.loadApplicationProperties();

		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("ui/fxml/TestDesignerMainView.fxml"));
		Parent root = loader.load();

		primaryStage.setScene(new Scene(root));
		
		primaryStage.show();
		
		primaryStage.setOnCloseRequest((event) -> {
			try {
				PropertyHelper.saveApplicationProperties(appProperties);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		});
		
	}

	public static void testExecution()
			throws IOException, ClassNotFoundException, KeywordLibraryException, TestfileSyntaxException {

		String[] defaultDirs = appProperties.getProperty("default.dirs", "").split(",");
		;
		LibraryLoader.getInstance(defaultDirs);

		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");
		// --------------------------------------------------------
		// tsExe.addDirectory("testfiles/tcu/");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_rotor.tst");
		// tsExe.addPath("testfiles/tcu/tcu_alarm_test.tst");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_tcu.tst");
		// --------------------------------------------------------
		tsExe.addPath("testfiles/test.tst");
		// --------------------------------------------------------

		TestSuiteProtocol tsProtocol = tsExe.execute();
		System.out.println(tsProtocol);

		Platform.exit();
	}

}
