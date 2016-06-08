package ui.controller;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.UserPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
		File selected = fc.showOpenDialog(tf_pathToDefaultLibraries.getScene().getWindow());
		if (selected != null) {
			if (!selected.exists()) {
				showAlertDialog("Datei existiert nicht", "");
				return;
			}
			String path = selected.getAbsolutePath();
			tf_pathToEditor.setText(path);
		}
	}

	@FXML
	void RestoreDefault(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Standardwerte setzen");
		alert.setContentText("Dieser Vorgang überschreibt alle anderen Einstellungen");
		Optional<ButtonType> pressed = alert.showAndWait();
		if (pressed.isPresent()) {
			if (pressed.get().equals(ButtonType.OK)) {
				String editor = UserPreferences.get().getDefaultValue(UserPreferences.EDITOR_EXE);
				tf_pathToEditor.setText(editor);

				String libraries = UserPreferences.get().getDefaultValue(UserPreferences.LIBRARY_DIR);
				tf_pathToDefaultLibraries.setText(libraries);
			}
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
		String editor = UserPreferences.get().getOrDefault(UserPreferences.EDITOR_EXE);
		tf_pathToEditor.setText(editor);

		String libraries = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
		tf_pathToDefaultLibraries.setText(libraries);

		tf_pathToEditor.textProperty().addListener((obs, oldText, newText) -> UserPreferences.get()
				.put(UserPreferences.EDITOR_EXE, tf_pathToEditor.getText()));
		tf_pathToDefaultLibraries.textProperty().addListener((obs, oldText, newText) -> UserPreferences.get()
				.put(UserPreferences.LIBRARY_DIR, tf_pathToDefaultLibraries.getText()));
	}

}
