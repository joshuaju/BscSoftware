package ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import external.Keyword;
import external.KeywordLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import ui.misc.KeywordListCell;

public class KeywordInformationListController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<KeywordLibrary> cb_library;

	@FXML
	private ListView<Keyword> lv_keywords;

	@FXML
	private BorderPane bp_information;
	private KeywordInformationController controller_information;

	@FXML
	void initialize() throws IOException {
		FXMLLoader kwInfoLoader = new FXMLLoader(
				getClass().getClassLoader().getResource("ui/fxml/KeywordInformationView.fxml"));
		TitledPane root = kwInfoLoader.load();
		controller_information = kwInfoLoader.getController();
		bp_information.setCenter(root);		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cb_library.setConverter(new StringConverter<KeywordLibrary>() {

			@Override
			public String toString(KeywordLibrary object) {
				return object.getName();
			}

			@Override
			public KeywordLibrary fromString(String string) {
				return null;
			}
		});
		cb_library.getSelectionModel().selectedItemProperty().addListener((obs) -> updateKeywordList());

		lv_keywords.setCellFactory(param -> new KeywordListCell());
		lv_keywords.getItems().clear();
		lv_keywords.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> controller_information.setKeyword(newVal));
	}

	public void addLibrary(KeywordLibrary library) {
		cb_library.getItems().add(library);
	}

	private void updateKeywordList() {
		KeywordLibrary selectedLib = cb_library.getSelectionModel().getSelectedItem();

		ObservableList<Keyword> kwList = FXCollections.observableArrayList();
		kwList.addAll(selectedLib.getKeywords());
		kwList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

		for (Keyword tmpKeyword : kwList) {
			lv_keywords.getItems().add(tmpKeyword);
		}
	}
}
