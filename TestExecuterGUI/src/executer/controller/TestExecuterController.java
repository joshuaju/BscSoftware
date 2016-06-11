package executer.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.property.UserPreferences;
import exceptions.testfile.TestfileException;
import executer.misc.DialogFactory;
import executer.misc.FileListCell;
import execution.TestSuiteExecuter;
import execution.TestSuiteExecuterThread;
import execution.TestSuiteProtocol;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestExecuterController {

	@FXML
	private TextField tfProtocolFile;

	@FXML
	private Button btnBrowseProtocolFile;

	@FXML
	private ListView<File> lvTestFiles;

	@FXML
	private Label lblNoTestfileHint;

	@FXML
	private TextArea taOutput;

	@FXML
	private TextField tfAuthor;

	@FXML
	private CheckBox cbDefaultAuthor;

	@FXML
	private Button btnStart;

	@FXML
	private Button btnAbort;

	@FXML
	private ProgressBar pbTest;

	@FXML
	private ProgressBar pbSuite;
	
	@FXML
    private MenuItem miAddFile;

    @FXML
    private MenuItem miAddDirectory;

    @FXML
    private MenuItem miRemove;

    @FXML
    private MenuItem miRemoveAll;

	private final ListProperty<File> selectedFiles;
	private final ObjectProperty<File> protocolFile;
	private final BooleanProperty executionRunning;
	private final BooleanProperty abortionRunning;
	private TestSuiteExecuterThread executionThread = null;

	public TestExecuterController() {
		selectedFiles = new SimpleListProperty<>(FXCollections.observableArrayList());
		protocolFile = new SimpleObjectProperty<>();
		executionRunning = new SimpleBooleanProperty(false);
		abortionRunning = new SimpleBooleanProperty(false);
	}

	public void initialize() {
		tfAuthor.disableProperty().bind(cbDefaultAuthor.selectedProperty());
		cbDefaultAuthor.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				setDefaultAuthorText();
			} else {
				tfAuthor.requestFocus();
				tfAuthor.selectAll();
			}
		});

		lvTestFiles.itemsProperty().bind(selectedFiles);
		lblNoTestfileHint.visibleProperty().bind(selectedFiles.sizeProperty().isEqualTo(0));
		lvTestFiles.setCellFactory(param -> new FileListCell());
		lvTestFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		miRemove.disableProperty().bind(lvTestFiles.getSelectionModel().selectedItemProperty().isNull());
		miRemoveAll.disableProperty().bind(selectedFiles.sizeProperty().isEqualTo(0));
		Platform.runLater(() -> lvTestFiles.requestFocus());

		btnStart.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			boolean disable = tfAuthor.getText().length() == 0 || selectedFiles.size() == 0
					|| protocolFile.get() == null || executionRunning.get();
			return disable;
		}, tfAuthor.textProperty(), selectedFiles, protocolFile, executionRunning));

		btnAbort.disableProperty().bind(executionRunning.not().or(abortionRunning));
		btnBrowseProtocolFile.disableProperty().bind(executionRunning);
		abortionRunning.addListener((obs, oldVal, newVal) -> {
			// final double progress;
			if (newVal) {
				// progress = -1;
				// pbTest.progressProperty().unbind();
				// pbSuite.progressProperty().unbind();
				btnStart.getScene().setCursor(Cursor.WAIT);
			} else {
				// progress = 0;
				btnStart.getScene().setCursor(Cursor.DEFAULT);
			}

			Platform.runLater(() -> {
				// pbTest.setProgress(progress);
				// pbSuite.setProgress(progress);
			});
		});

		setDefaultAuthorText();
	}

	@FXML
	void browseProtocolFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		File initDir = null;

		String initName = "protocol.ptc";
		if (protocolFile.isNotNull().get()) {
			initDir = protocolFile.get().getParentFile();
			initName = protocolFile.get().getName();
		}
		if (initDir == null || !initDir.isDirectory()) {
			initDir = new File(UserPreferences.get().getOrDefault(UserPreferences.APPLICATION_HOME_DIR));
		}

		fc.setInitialDirectory(initDir);
		fc.setInitialFileName(initName);
		fc.getExtensionFilters().add(new ExtensionFilter("Testdateien", "*.ptc"));
		File protocol = fc.showSaveDialog(btnBrowseProtocolFile.getScene().getWindow());
		if (protocol != null) {
			protocolFile.set(protocol);
			tfProtocolFile.setText(protocol.getAbsolutePath());
		}
	}	

	private void browseTestFiles(File initDir) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(initDir);
		fc.getExtensionFilters().add(new ExtensionFilter("Testdateien", "*.tst"));
		List<File> files = fc.showOpenMultipleDialog(lvTestFiles.getScene().getWindow());
		if (files != null) {
			selectedFiles.addAll(files);
		}
	}

	private void browseTestDirectory(File initDir) {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setInitialDirectory(initDir);
		File directory = dc.showDialog(lvTestFiles.getScene().getWindow());
		if (directory != null) {
			selectedFiles.add(directory);
		}
	}

	private void removeSelectedFile() {
		List<File> selected = new ArrayList<>(lvTestFiles.getSelectionModel().getSelectedItems());
		while (!selected.isEmpty()) {
			File sel = selected.remove(0);
			selectedFiles.remove(sel);
		}
	}

	@FXML
	void startExecution(ActionEvent event) {
		taOutput.clear();

		TestSuiteExecuter tsExe = new TestSuiteExecuter(tfAuthor.getText());

		pbSuite.progressProperty().bind(tsExe.suiteProgressProperty());
		pbTest.progressProperty().bind(tsExe.testProgressProperty());

		selectedFiles.forEach(file -> {
			try {
				if (file.isDirectory()) {
					tsExe.addDirectory(true, file.getAbsolutePath());
				} else if (file.isFile()) {
					tsExe.addFile(file.getAbsolutePath());
				} else {

				}
			} catch (FileNotFoundException | TestfileException e) {
				e.printStackTrace();
			}
		});

		executionThread = new TestSuiteExecuterThread(tsExe);
		executionThread.setOnFinish(protocol -> finishedExecution(protocol));
		executionThread.setOnAbort(() -> abortedExecution());

		executionRunning.set(true);
		executionThread.start();
	}

	private void finishedExecution(TestSuiteProtocol protocol) {
		pbSuite.progressProperty().unbind();
		pbTest.progressProperty().unbind();
		pbSuite.setProgress(0);
		pbTest.setProgress(0);

		if (protocol != null) {
			taOutput.setText(protocol.toString());
			try {
				protocol.writeToFile(protocolFile.get());
			} catch (IOException e) {
				Alert warning = DialogFactory.createAlert("Protokoll konnte nicht gespeichert werden",
						"Erstellen Sie eine Protokolldatei manuel", AlertType.WARNING);
				Platform.runLater(() -> warning.show());
			}
		} else {
			taOutput.clear();
		}
		executionRunning.set(false);
	}

	private void abortedExecution() {
		abortionRunning.set(false);
		finishedExecution(null);
		taOutput.setText("Testdurchführung wurde abgebrochen.");
	}

	@FXML
	void abortExecution(ActionEvent event) {
		if (executionThread == null || !executionThread.isAlive()) {
			executionRunning.set(false);
			return;
		}
		abortionRunning.set(true);
		executionThread.abort();
	}

	private void setDefaultAuthorText() {
		tfAuthor.setText(System.getProperty("user.name"));
	}
	
	@FXML
    void contextMenuAddDirectoryAction(ActionEvent event) {
		// TODO Pfad merken
		browseTestDirectory(null);
    }

    @FXML
    void contextMenuAddFileAction(ActionEvent event) {
    	// TODO letzten pfad merken
    	browseTestFiles(null);
    }

    @FXML
    void contextMenuRemoveAction(ActionEvent event) {
    	removeSelectedFile();
    }

    @FXML
    void contextMenuRemoveAllAction(ActionEvent event) {
    	selectedFiles.clear();
    }

}
