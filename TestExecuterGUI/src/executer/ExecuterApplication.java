package executer;

import java.io.IOException;

import exceptions.keywordlibrary.KeywordLibraryException;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExecuterApplication extends Application {

	public static void main(String[] args) throws ClassNotFoundException, IOException, KeywordLibraryException{
		LibraryLoader.createInstance();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("executer/fxml/TestExecuterView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}