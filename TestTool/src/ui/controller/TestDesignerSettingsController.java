package ui.controller;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import application.PropertyHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestDesignerSettingsController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField tf_pathToEditor;

	@FXML
	private TextField tf_pathToDefaultLibraries;

	@FXML
	void browseForDefLibs(ActionEvent event) {
		DirectoryChooser dc = new DirectoryChooser();
		
		File selectedDir = dc.showDialog(tf_pathToDefaultLibraries.getScene().getWindow());
		if (selectedDir != null) {
			String path = selectedDir.getAbsolutePath();
			tf_pathToDefaultLibraries.setText(path);			
		}
	}

	@FXML
	void browseForEditor(ActionEvent event) {
		FileChooser fc = new FileChooser();	
		ExtensionFilter tstFilter = new ExtensionFilter("Auführbare Datei", "*.*");
		fc.getExtensionFilters().add(tstFilter);
		File selected = fc.showSaveDialog(tf_pathToDefaultLibraries.getScene().getWindow());
		if (selected != null) {
			if (!selected.exists()) {
				showAlertDialog("Datei existiert nicht", "");
				return;
			}
			String path = selected.getAbsolutePath();
			tf_pathToEditor.setText(path);			
		}
	}

	private void showAlertDialog(String header, String content) {
		Alert alertDlg = new Alert(AlertType.ERROR);
		alertDlg.setHeaderText(header);
		alertDlg.setContentText(content);
		alertDlg.show();
	}

	@FXML
	void initialize() {
		assert tf_pathToEditor != null : "fx:id=\"tf_pathToEditor\" was not injected: check your FXML file 'TestDesignerSettingsView.fxml'.";
		assert tf_pathToDefaultLibraries != null : "fx:id=\"tf_pathToDefaultLibraries\" was not injected: check your FXML file 'TestDesignerSettingsView.fxml'.";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		String path = PropertyHelper.loadApplicationProperties().getProperty(PropertyHelper.TEXTEDITOR, "notepad");
		tf_pathToEditor.setText(path);
		path = PropertyHelper.loadApplicationProperties().getProperty(PropertyHelper.DEF_LIBRARIES_DIR, "");
		tf_pathToDefaultLibraries.setText(path);
		Properties props = PropertyHelper.loadApplicationProperties();
		
		tf_pathToEditor.textProperty().addListener((obs, oldText, newText) -> props.setProperty(PropertyHelper.TEXTEDITOR, newText));
		tf_pathToDefaultLibraries.textProperty().addListener((obs, oldText, newText) -> props.setProperty(PropertyHelper.DEF_LIBRARIES_DIR, newText));
	}

}
