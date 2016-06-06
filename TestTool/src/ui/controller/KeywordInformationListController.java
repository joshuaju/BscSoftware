package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import external.Keyword;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import ui.misc.KeywordListCell;

public class KeywordInformationListController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ListView<Keyword> lv_keywords;

	@FXML
	private BorderPane bp_information;

	@FXML
	private SplitPane sp_main;

	@FXML
	private KeywordInformationController keywordInfoController;

	@FXML
	private SearchFieldController searchController;

	private ListProperty<Keyword> keywordListProperty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lv_keywords.setCellFactory(param -> new KeywordListCell());

		keywordListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

		lv_keywords.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> keywordInfoController.setKeyword(newVal));

		FilteredList<Keyword> filteredList = searchController.getFilteredList(keywordListProperty);
		filteredList.setPredicate(kw -> true);
		searchController.textProperty().addListener((obs, oldVal, newVal) -> {
			filteredList.setPredicate(kw -> kw.getName().toLowerCase().contains(newVal.toLowerCase()));
		});
		
		lv_keywords.setItems(filteredList);
	}

	public ListProperty<Keyword> keywordListProperty() {
		return keywordListProperty;
	}

}
