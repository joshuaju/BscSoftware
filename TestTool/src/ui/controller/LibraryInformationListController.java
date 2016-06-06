package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import external.KeywordLibrary;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import ui.misc.KeywordLibraryListCell;

public class LibraryInformationListController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private LibraryInformationController libraryInfoController;

	@FXML
	private ListView<KeywordLibrary> lv_libraries;

	@FXML
	private SearchFieldController searchController;

	private ListProperty<KeywordLibrary> libraryListProperty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libraryListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
		lv_libraries.setCellFactory(param -> new KeywordLibraryListCell());

		ReadOnlyObjectProperty<KeywordLibrary> selected = lv_libraries.getSelectionModel().selectedItemProperty();
		libraryInfoController.libraryProperty().bind(selected);

		FilteredList<KeywordLibrary> filteredList = searchController.getFilteredList(libraryListProperty);
		searchController.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
				filteredList.setPredicate(k -> k.getName().toLowerCase().contains(newVal.toLowerCase()));
			}
		});

		lv_libraries.setItems(filteredList);
	}

	public ListProperty<KeywordLibrary> libraryListProperty() {
		return libraryListProperty;
	}

}
