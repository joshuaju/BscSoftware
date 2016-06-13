package executer;

import java.io.IOException;

import executer.misc.DialogFactory;
import exceptions.keywordlibrary.KeywordLibraryException;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ExecuterApplication extends Application {

	public static void main(String[] args) throws ClassNotFoundException, IOException, KeywordLibraryException{
		LibraryLoader.createInstance();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		DialogFactory.initOwner(primaryStage);
		
		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("executer/fxml/TestExecuterView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		
		Image windowIcon = new Image(getClass().getClassLoader().getResourceAsStream("img/atesteo_favicon.png"));
		primaryStage.getIcons().add(windowIcon);
		primaryStage.setTitle("ATESTEO Testdurchführungs-Tool");
		
		primaryStage.show();
	}

}