package ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.MainApplication;
import application.UserPreferences;
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
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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
		
		SortedList<SimpleKeywordLibrary> sortedList = new SortedList<>(filteredList);
		sortedList.setComparator((o1, o2) -> o1.getName().compareTo(o2.getName()));
		
		lv_libraries.setItems(sortedList);
		lv_libraries.setCellFactory(param -> new KeywordLibraryListCell());

		lv_libraries.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ContextMenu cm = createContextMenu();
		lv_libraries.setContextMenu(cm);
	}

	private ContextMenu createContextMenu() {
		ContextMenu cm = new ContextMenu();
		MenuItem add = new MenuItem("Add");
		MenuItem rem = new MenuItem("Remove");
		cm.getItems().addAll(add, rem);
		add.setOnAction(event -> {
			browseAndAddLibrary();
		});

		rem.setOnAction(event -> {
			List<SimpleKeywordLibrary> selected = new ArrayList<>(lv_libraries.getSelectionModel().getSelectedItems());
			
			while (!selected.isEmpty()) {
				SimpleKeywordLibrary sel = selected.remove(0);
				libraryListProperty.remove(sel);
			}
		});

		rem.disableProperty().bind(lv_libraries.getSelectionModel().selectedItemProperty().isNull());

		return cm;
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
		
		String libraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
		File initDir = new File(libraryDir);
		if (initDir.isDirectory()){
			fc.setInitialDirectory(initDir);
		}
				
		
		List<File> selected = fc.showOpenMultipleDialog(lv_libraries.getScene().getWindow());
		if (selected != null) {
			for (File sel : selected) {
				if (sel != null && sel.getName().endsWith(".jar")) {
					addLibrary(sel.getAbsolutePath());
				}
			}
		}

	}

}
