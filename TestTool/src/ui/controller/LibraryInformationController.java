package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import external.Keyword;
import external.KeywordLibrary;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class LibraryInformationController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private KeywordInformationListController keywordInfoListController;

	@FXML
	private TitledPane tp_library;

	@FXML
	private TextField tf_author;

	@FXML
	private TextArea ta_description;

	private ObjectProperty<KeywordLibrary> libraryProperty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libraryProperty = new SimpleObjectProperty<>();

		libraryProperty.addListener((obs) -> updateInformation());

		tp_library.expandedProperty().addListener((obs, oldPane, newPane) -> {
			Platform.runLater(() -> {
				// resize if titledpane expands/collapses
				Stage stage = (Stage) tp_library.getScene().getWindow();
				stage.setMinHeight(0);
				stage.sizeToScene();
				stage.setMinHeight(stage.getHeight());
			});
		});
	}

	public ObjectProperty<KeywordLibrary> libraryProperty() {
		return libraryProperty;
	}

	public void updateInformation() {
		String name = "Keine Bibliothek ausgwählt";
		String author = "";
		String description = "";
		Keyword[] kw = new Keyword[0];
		if (libraryProperty.isNotNull().get()) {
			KeywordLibrary newLib = libraryProperty.get();
			name = newLib.getName();
			author = newLib.getAuthor();
			description = newLib.getDescription();
			kw = newLib.getKeywords();
		}

		tp_library.setText(name);
		tf_author.setText(author);
		ta_description.setText(description);
		keywordInfoListController.keywordListProperty().setAll(FXCollections.observableArrayList(kw));
	}

}
