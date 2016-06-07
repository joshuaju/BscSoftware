package ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.MainApplication;
import exceptions.keywordlibrary.KeywordLibraryException;
import external.LibraryLoader;
import external.SimpleKeywordLibrary;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ui.misc.KeywordLibraryListCell;

public class LibraryInformationListController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private LibraryInformationController libraryInfoController;

	@FXML
	private ListView<SimpleKeywordLibrary> lv_libraries;

	@FXML
	private Label lblAddLibrary;

	@FXML
	private SearchFieldController searchController;

	private ListProperty<SimpleKeywordLibrary> libraryListProperty;

	private FilteredList<SimpleKeywordLibrary> filteredList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image imgAddLib = new Image(getClass().getClassLoader().getResourceAsStream("img/plus.png"));
		ImageView imgViewAddLib = new ImageView(imgAddLib);
		lblAddLibrary.setGraphic(imgViewAddLib);
		lblAddLibrary.setOnMouseClicked((event) -> browseAndAddLibrary());

		libraryListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

		ReadOnlyObjectProperty<SimpleKeywordLibrary> selected = lv_libraries.getSelectionModel().selectedItemProperty();
		libraryInfoController.libraryProperty().bind(selected);

		filteredList = searchController.getFilteredList(libraryListProperty);
		searchController.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
				filteredList.setPredicate(k -> k.getName().toLowerCase().contains(newVal.toLowerCase()));
			}
		});

	
		lv_libraries.setItems(filteredList);		
		lv_libraries.setCellFactory(param -> new KeywordLibraryListCell());		
	}

	public ListProperty<SimpleKeywordLibrary> libraryListProperty() {
		return libraryListProperty;
	}

	public void addLibrary(String path) {		
		String header = "Bibliothek wurde nicht geladen";
		if (!path.endsWith(".jar")) {
			MainApplication.showAlert(header, "Es wurde keine Jar-Datei ausgewählt");
		}
		LibraryLoader loader = LibraryLoader.getInstance();
		try {
			SimpleKeywordLibrary library = loader.loadSimpleKeywordLibrary(path);
			libraryListProperty.add(library);
		} catch (IOException | KeywordLibraryException e) {
			MainApplication.showAlert("Bibliothek wurde nicht geladen", e.getMessage());
		}
	}

	public void browseAndAddLibrary() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Bibliothek laden");
		fc.getExtensionFilters().add(new ExtensionFilter("Bibliothek", "*.jar"));
		File selected = fc.showOpenDialog(lv_libraries.getScene().getWindow());
		if (selected != null && selected.getName().endsWith(".jar")) {
			addLibrary(selected.getAbsolutePath());
		}
	}

}
