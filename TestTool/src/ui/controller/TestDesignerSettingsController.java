package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class TestDesignerSettingsController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Accordion accordion;

	@FXML
	private TextField tf_pathToEditor;

	@FXML
	private TextField tf_pathToDefaultLibraries;

	@FXML
	void browseForDefLibs(ActionEvent event) {
		// TODO
		System.out.println("Browse Libraries");
	}

	@FXML
	void browseForEditor(ActionEvent event) {
		// TODO
		System.out.println("Browser Editor");
	}

	@FXML
	void initialize() {
		assert tf_pathToEditor != null : "fx:id=\"tf_pathToEditor\" was not injected: check your FXML file 'TestDesignerSettingsView.fxml'.";
		assert tf_pathToDefaultLibraries != null : "fx:id=\"tf_pathToDefaultLibraries\" was not injected: check your FXML file 'TestDesignerSettingsView.fxml'.";

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		accordion.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
			// Das Accordion soll mindestens eine Pane offen haben
			if (oldPane == null) {
				return;
			}
			boolean allowExpanding = true;
			for (TitledPane tmpPane : accordion.getPanes()) {
				if (tmpPane.isExpanded()) {
					allowExpanding = false;
					break;
				}
			}
			if (allowExpanding) {
				Platform.runLater(() -> {
					accordion.setExpandedPane(oldPane);
				});
			}
		});
		accordion.setExpandedPane(accordion.getPanes().get(0));
	}
}
