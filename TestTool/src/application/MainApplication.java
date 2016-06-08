package application;

import java.io.IOException;
import java.util.Optional;
import java.util.prefs.BackingStoreException;

import exceptions.keywordlibrary.KeywordLibraryException;
import exceptions.testfile.TestfileSyntaxException;
import execution.TestSuiteExecuter;
import execution.TestSuiteProtocol;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApplication extends Application {

	// public static void main(String[] args){
	// launch(args);
	// }

	public static Stage stage;
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException, KeywordLibraryException, TestfileSyntaxException, BackingStoreException {										
		LibraryLoader.createInstance();
		
		stage = primaryStage;			
//		testGUI(primaryStage);
		testExecution();			
		Platform.exit();
	}
	
	public static void testGUI(Stage primaryStage) throws IOException{
		FXMLLoader loader = new FXMLLoader(
				MainApplication.class.getClassLoader().getResource("ui/fxml/TestDesignerMainView.fxml"));
		BorderPane root = loader.load();

		Scene scene = new Scene(root);
		scene.getStylesheets().add(MainApplication.class.getClassLoader().getResource("ui/css/listview.css").toExternalForm());
		primaryStage.setScene(scene);	
		primaryStage.show();
		
		primaryStage.sizeToScene();
		primaryStage.setMinHeight(primaryStage.getHeight());
		
		primaryStage.setOnCloseRequest((event) -> UserPreferences.get().store());
	}
	
	public static void testExecution()
			throws IOException, ClassNotFoundException, KeywordLibraryException, TestfileSyntaxException {				
		TestSuiteExecuter tsExe = new TestSuiteExecuter("Joshua Jungen");
		// --------------------------------------------------------
		// tsExe.addDirectory("testfiles/tcu/");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_rotor.tst");
//		 tsExe.addPath("testfiles/tcu/tcu_alarm_test.tst");
		// tsExe.addPath("testfiles/tcu/tcu_testsignal_tcu.tst");
		// --------------------------------------------------------
		tsExe.addPath("testfiles/test.tst");
		tsExe.addPath("testfiles/test1.tst");
		tsExe.addPath("testfiles/test2.tst");
		tsExe.addPath("testfiles/test3.tst");
		// --------------------------------------------------------

		
		tsExe.suiteProgressProperty().addListener((obs, oldValue, newValue) -> System.out.println(newValue + "%"));
		tsExe.testProgressProperty().addListener((obs, oldValue, newValue) -> System.out.println("\t" + newValue + "%"));
		TestSuiteProtocol tsProtocol = tsExe.execute();
		System.out.println("\n" + tsProtocol);

		UserPreferences.get().store();
		Platform.exit();
	}
	
	public static void showAlert(String header, String content){
		boolean onTop = MainApplication.stage.isAlwaysOnTop();
		MainApplication.stage.setAlwaysOnTop(false);
		Alert alertDlg = new Alert(AlertType.ERROR);
		alertDlg.setHeaderText(header);
		alertDlg.setContentText(content);
		alertDlg.showAndWait();
		MainApplication.stage.setAlwaysOnTop(onTop);
		
	}
	
	public static <T> Optional<T> showDialog(Dialog<T> dlg){		
		boolean onTop = MainApplication.stage.isAlwaysOnTop();
		MainApplication.stage.setAlwaysOnTop(false);
		dlg.initOwner(stage);
		dlg.initModality(Modality.APPLICATION_MODAL);
		Optional<T> optional = dlg.showAndWait();
		MainApplication.stage.setAlwaysOnTop(onTop);
		return optional;
	}

}
