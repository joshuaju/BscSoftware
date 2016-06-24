package designer;

import java.io.IOException;

import application.property.UserPreferences;
import designer.misc.DialogFactory;
import exceptions.keywordlibrary.KeywordLibraryException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DesignApplication extends Application {

	public static void main(String[] args) throws ClassNotFoundException, IOException, KeywordLibraryException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DialogFactory.initOwner(primaryStage);

		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("designer/fxml/TestDesignerMainView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("style/root.css").toExternalForm());
		primaryStage.setScene(scene);

		primaryStage.setOnCloseRequest(event -> UserPreferences.get().store());

		Image windowIcon = new Image(getClass().getClassLoader().getResourceAsStream("img/atesteo_favicon.png"));
		primaryStage.getIcons().add(windowIcon);
		primaryStage.setTitle("ATESTEO Testerstellungs-Tool");

		primaryStage.show();

	}

}
