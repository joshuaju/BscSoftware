package ui.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import application.PropertyHelper;
import exceptions.keywordlibrary.KeywordLibraryException;
import external.KeywordLibrary;
import external.LibraryLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import testfile.Testfile;

public class TestDesignerMainController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private BorderPane bp_mainpane;

	@FXML
	private CheckMenuItem cmi_alwaysOnTop;

	private String CMD_TEXTEDITOR;

	@FXML
	void NewFile(ActionEvent event) {
		File selected = showFileChooserDialog();
		if (selected != null) {
			// "C:/Program Files (x86)/Notepad++/notepad++.exe"
			callTexteditor(selected);
		}
	}

	@FXML
	void NewFileFromTemplate(ActionEvent event) {
		File selected = showFileChooserDialog();
		if (selected != null) {
			String[] lines = Testfile.getTemplate();
			try {
				BufferedWriter bw = Files.newBufferedWriter(selected.toPath(), StandardOpenOption.CREATE_NEW);
				PrintWriter writer = new PrintWriter(bw);
				for (String line : lines) {
					writer.println(line);
				}
				writer.flush();
				writer.close();
				callTexteditor(selected);
			} catch (IOException e) {
				showAlertDialog("Template konnte nicht erstellt werden", e.getMessage());
			}

		}
	}

	@FXML
	void openSettings(ActionEvent event) {		
		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("ui/fxml/TestDesignerSettingsView.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DialogPane dlgPane = new DialogPane();
		dlgPane.setContent(root);

		Dialog<String> dlg = new Dialog<>();
		dlg.setTitle("Einstellungen");			
		dlg.setDialogPane(dlgPane);
		dlg.setResizable(true);
		dlgPane.getButtonTypes().add(ButtonType.OK);		
		dlg.show();
	}

	@FXML
	void initialize() {
		assert bp_mainpane != null : "fx:id=\"bp_mainpane\" was not injected: check your FXML file 'TestDesignerMainView.fxml'.";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		KeywordLibrary lib = null;
		try {
			LibraryLoader.getInstance("D:/Bsc/default/");
			lib = LibraryLoader.getInstance().loadLibrary("D:/Bsc/default/CompareKeywordLibrary.jar");
		} catch (ClassNotFoundException | IOException | KeywordLibraryException e) {
			e.printStackTrace();
		}
		FXMLLoader loader = new FXMLLoader(
				getClass().getClassLoader().getResource("ui/fxml/KeywordInformationListView.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		KeywordInformationListController controller = loader.getController();
		controller.addLibrary(lib);

		bp_mainpane.setCenter(root);

		Platform.runLater(() -> {
			Scene currentScene = bp_mainpane.getScene();
			Stage currentStage = (Stage) currentScene.getWindow();
			cmi_alwaysOnTop.selectedProperty()
					.addListener((obs, oldVal, newVal) -> currentStage.setAlwaysOnTop(newVal));
		});

		try {
			CMD_TEXTEDITOR = PropertyHelper.loadApplicationProperties().getProperty(PropertyHelper.TEXTEDITOR,
					"notepad");
		} catch (IOException e) {
			CMD_TEXTEDITOR = "notepad";
		}
	}

	private File showFileChooserDialog() {
		FileChooser fc = new FileChooser();

		ExtensionFilter tstFilter = new ExtensionFilter("Testdatei", "*.tst");
		fc.getExtensionFilters().add(tstFilter);

		File selected = fc.showSaveDialog(bp_mainpane.getScene().getWindow());

		if (selected != null && !selected.getName().endsWith(".tst")) {
			String header = "Ung�ltige Dateiendung";
			String content = "Die Dateiendung muss \".tst\" sein";
			showAlertDialog(header, content);
		}
		return selected;
	}

	private void showAlertDialog(String header, String content) {
		Alert alertDlg = new Alert(AlertType.ERROR);
		alertDlg.setHeaderText(header);
		alertDlg.setContentText(content);
		alertDlg.show();
	}

	private void callTexteditor(File selected) {
		ProcessBuilder process = new ProcessBuilder(CMD_TEXTEDITOR, selected.getAbsolutePath());
		try {
			process.start();
		} catch (IOException e) {
			showAlertDialog("Texteditor wurde nicht ge�ffnet", "");
		}
	}
}
