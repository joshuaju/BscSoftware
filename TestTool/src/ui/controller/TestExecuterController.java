package ui.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import application.property.UserPreferences;
import exceptions.testfile.TestfileException;

import execution.TestSuiteExecuter;
import execution.TestSuiteExecuterThread;
import execution.TestSuiteProtocol;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestExecuterController {

	@FXML
	private TextField tfProtocolFile;

	@FXML
	private Button btnBrowseProtocolFile;

	@FXML
	private TextField tfTestFiles;

	@FXML
	private TextArea taOutput;

	@FXML
	private ToggleGroup pathTypeGroup;

	@FXML
	private RadioButton rbDirectory;

	@FXML
	private RadioButton rbFiles;

	@FXML
	private TextField tfAuthor;

	@FXML
	private CheckBox cbDefaultAuthor;

	@FXML
	private CheckBox cbDefaultProtocolFile;

	@FXML
	private Button btnStart;

	@FXML
	private Button btnAbort;

	private final ObservableList<File> selectedFiles;
	private final ObjectProperty<File> protocolFile;
	private final BooleanProperty executionRunning;
	private TestSuiteExecuterThread executionThread = null;

	public TestExecuterController() {
		selectedFiles = FXCollections.observableArrayList();
		protocolFile = new SimpleObjectProperty<>();
		executionRunning = new SimpleBooleanProperty(false);
	}

	public void initialize() {

		tfAuthor.disableProperty().bind(cbDefaultAuthor.selectedProperty());
		cbDefaultAuthor.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				setDefaultAuthorText();
			}
		});

		selectedFiles.addListener((ListChangeListener<File>) c -> {
			if (selectedFiles.size() > 0) {
				StringBuilder builder = new StringBuilder();
				selectedFiles.forEach(file -> builder.append(", " + file.getName()));
				tfTestFiles.setText(builder.substring(2));
			} else {
				tfTestFiles.clear();
			}
		});

		btnBrowseProtocolFile.disableProperty().bind(cbDefaultProtocolFile.selectedProperty());
		cbDefaultProtocolFile.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				setDefaultProtocolFile();
			}
		});

		pathTypeGroup.selectedToggleProperty().addListener((obs) -> selectedFiles.clear());
		Platform.runLater(() -> tfTestFiles.requestFocus());

		btnStart.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			boolean disable = tfAuthor.getText().length() == 0 || selectedFiles.size() == 0 || protocolFile.get() == null || executionRunning.get();
			System.out.println(disable);
			return disable;
		}, tfAuthor.textProperty(), selectedFiles, protocolFile, executionRunning));

		btnAbort.disableProperty().bind(executionRunning.not());
		
		setDefaultAuthorText();
		setDefaultProtocolFile();
	}

	@FXML
	void browseProtocolFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		File initDir = null;
		String initName = "";
		if (protocolFile.isNotNull().get()) {
			initDir = protocolFile.get().getParentFile();
			initName = protocolFile.get().getName();
		} else {
			initDir = new File(UserPreferences.get().getOrDefault(UserPreferences.APPLICATION_HOME_DIR));
		}

		fc.setInitialDirectory(initDir);
		fc.setInitialFileName(initName);
		fc.getExtensionFilters().add(new ExtensionFilter("Testdateien", "*.ptc"));
		File protocol = fc.showSaveDialog(btnBrowseProtocolFile.getScene().getWindow());
		if (protocol != null) {
			protocolFile.set(protocol);
			updateProtocolFileText();
		}
	}

	@FXML
	void browseTestFiles(ActionEvent event) {

		File initDir = new File(UserPreferences.get().getOrDefault(UserPreferences.APPLICATION_HOME_DIR));

		selectedFiles.clear();
		if (rbDirectory.isSelected()) {
			DirectoryChooser dc = new DirectoryChooser();
			dc.setInitialDirectory(initDir);
			File directory = dc.showDialog(rbDirectory.getScene().getWindow());
			if (directory != null) {
				selectedFiles.add(directory);
			}
		} else if (rbFiles.isSelected()) {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(initDir);
			fc.getExtensionFilters().add(new ExtensionFilter("Testdateien", "*.tst"));
			List<File> files = fc.showOpenMultipleDialog(rbFiles.getScene().getWindow());
			if (files != null) {
				selectedFiles.addAll(files);
			}
		}
	}

	@FXML
	void startExecution(ActionEvent event) {		
		taOutput.clear();
		
		TestSuiteExecuter tsExe = new TestSuiteExecuter(tfAuthor.getText());

		if (rbDirectory.isSelected()) {
			selectedFiles.forEach(file -> {
				try {
					tsExe.addDirectory(true, file.getAbsolutePath());
				} catch (TestfileException | FileNotFoundException e) {
					e.printStackTrace();
				}
			});
		} else {
			selectedFiles.forEach(file -> {
				try {
					tsExe.addFile(file.getAbsolutePath());
				} catch (TestfileException | FileNotFoundException e) {
					e.printStackTrace();
				}
			});
		}
				
		executionThread = new TestSuiteExecuterThread(tsExe);
		executionThread.setOnFinish(protocol -> finishedExecution(protocol));
		executionThread.setOnAbort(() -> abortedExecution());
		
		executionRunning.set(true);
		executionThread.start();
		
		System.out.println("started");
	}
	
	private void finishedExecution(TestSuiteProtocol protocol){
		taOutput.setText(protocol.toString());		
		executionRunning.set(false);
	}
	
	private void abortedExecution(){
		taOutput.setText("ABORTED");
		executionRunning.set(false);
	}

	@FXML
	void abortExecution(ActionEvent event) {
		if (executionThread == null || !executionThread.isAlive()){
			executionRunning.set(false);
			return;					
		}	
		
		executionThread.abort();
	}

	private void setDefaultAuthorText() {
		tfAuthor.setText(System.getProperty("user.name"));
	}

	private void setDefaultProtocolFile() {
		File parentDir = new File(UserPreferences.get().getOrDefault(UserPreferences.APPLICATION_HOME_DIR));
		protocolFile.set(new File(parentDir, "protocol.ptc"));
		updateProtocolFileText();
	}

	private void updateProtocolFileText() {
		if (protocolFile.isNull().get()) {
			tfProtocolFile.clear();
		} else {
			tfProtocolFile.setText(protocolFile.get().getAbsolutePath());
		}
	}
}
