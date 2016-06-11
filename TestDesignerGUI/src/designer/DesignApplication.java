package designer;
import java.io.IOException;

import application.property.UserPreferences;
import designer.misc.DialogFactory;
import exceptions.keywordlibrary.KeywordLibraryException;
import external.LibraryLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesignApplication extends Application {

	public static void main(String[] args) throws ClassNotFoundException, IOException, KeywordLibraryException{
		LibraryLoader.createInstance();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		DialogFactory.initOwner(primaryStage);
		
		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("designer/fxml/TestDesignerMainView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
		
		primaryStage.setOnCloseRequest(event -> UserPreferences.get().store());		
	}

}
