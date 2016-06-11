package application;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

import application.property.UserPreferences;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	static Stage stage;

	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException, KeywordLibraryException,
			TestfileSyntaxException, BackingStoreException, InterruptedException {
		LibraryLoader.createInstance();
		stage = primaryStage;

		testExecutionGUI(primaryStage);

//		testExecution();
//		Platform.exit();
	}

	static void testDocumentationGUI(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				MainApplication.class.getClassLoader().getResource("ui/fxml/TestDesignerMainView.fxml"));
		BorderPane root = loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets()
				.add(MainApplication.class.getClassLoader().getResource("ui/css/listview.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.sizeToScene();
		primaryStage.setMinHeight(primaryStage.getHeight());

		primaryStage.setOnCloseRequest((event) -> UserPreferences.get().store());
	}
	
	static void testExecutionGUI(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				MainApplication.class.getClassLoader().getResource("ui/fxml/TestExecuterView.fxml"));
		
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets()
		.add(MainApplication.class.getClassLoader().getResource("ui/css/listview.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.sizeToScene();		
	}

	static void testExecution()
			throws IOException, ClassNotFoundException, KeywordLibraryException, TestfileSyntaxException, InterruptedException {
		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");
		// --------------------------------------------------------
		// tsExe.addDirectory("testfiles/tcu/");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_rotor.tst");
		// tsExe.addPath("testfiles/tcu/tcu_alarm_test.tst");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_tcu.tst");
		// --------------------------------------------------------
//		tsExe.addDirectory(true, "testfiles");
		 tsExe.addFile("testfiles/test.tst");
		// tsExe.addPath("testfiles/test1.tst");
		// tsExe.addPath("testfiles/test2.tst");
		// tsExe.addPath("testfiles/test3.tst");
		// --------------------------------------------------------

		tsExe.suiteProgressProperty().addListener(
				(obs, oldValue, newValue) -> System.out.println(((int) (newValue.doubleValue() * 100)) + "%"));
		tsExe.testProgressProperty().addListener(
				(obs, oldValue, newValue) -> System.out.println("\t" + ((int) (newValue.doubleValue() * 100)) + "%"));
		
		TestSuiteProtocol tsProtocol = tsExe.execute();
		System.out.println("\n" + tsProtocol);

		UserPreferences.get().store();
		Platform.exit();
	}

}
