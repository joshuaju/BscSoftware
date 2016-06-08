package ui.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

import application.MainApplication;
import application.property.UserPreferences;
import exceptions.keywordlibrary.KeywordLibraryException;
import external.LibraryLoader;
import external.SimpleKeywordLibrary;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
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
	private LibraryInformationListController libInfoListController;

	@FXML
	private CheckMenuItem cmi_alwaysOnTop;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String libraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
		File libDirFile = new File(libraryDir);
		Collection<File> files = null;
		try {
			files = getFiles(libDirFile);
		} catch (Exception e) {
			System.err.println(
					"WARNUNG: Der Pfad zum Bibliothekspfad war fehlerhaft. Der Pfad wurde auf Standard gesetzt");
			libraryDir = UserPreferences.get().getDefaultValue(UserPreferences.LIBRARY_DIR);
			libDirFile = new File(libraryDir);
			files = getFiles(libDirFile);
		}
		Collection<SimpleKeywordLibrary> libraries = new ArrayList<>();

		for (File tmp : files) {
			try {
				
				SimpleKeywordLibrary lib = LibraryLoader.getInstance().loadSimpleKeywordLibrary(tmp.getAbsolutePath());
				libraries.add(lib);
			} catch (IOException | KeywordLibraryException e) {
				e.printStackTrace();
			}
		}

		libInfoListController.libraryListProperty().addAll(libraries);

		Platform.runLater(() -> {
			Scene currentScene = bp_mainpane.getScene();
			Stage currentStage = (Stage) currentScene.getWindow();
			cmi_alwaysOnTop.selectedProperty().addListener((obs, oldVal, newVal) -> {
				currentStage.setAlwaysOnTop(newVal);
				UserPreferences.get().put(UserPreferences.ALWAYS_ON_TOP, newVal.toString());
			});

			String stored = UserPreferences.get().getOrDefault(UserPreferences.ALWAYS_ON_TOP);
			Boolean alwaysOnTop = Boolean.parseBoolean(stored);
			cmi_alwaysOnTop.setSelected(alwaysOnTop);
		});
	}

	@FXML
	void NewFile(ActionEvent event) {
		File selected = showFileOpener(null);
		if (selected != null) {
			callTexteditor(selected);
		}
	}

	@FXML
	void NewFileFromTemplate(ActionEvent event) {
		File selected = showFileSaver(null);
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
				MainApplication.showAlert("Template konnte nicht erstellt werden", e.getMessage());
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
		dlg.setResizable(false);
		MainApplication.showDialog(dlg);
	}

	@FXML
	void ImportLibrary(ActionEvent event) {
		libInfoListController.browseAndAddLibrary();
	}

	@FXML
	void OpenSession(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().setAll(new ExtensionFilter("Sitzung", "*.session"));

		String sessionDir = UserPreferences.get().getOrDefault(UserPreferences.SESSION_DIR);
		File sessionFile = new File(sessionDir);
		chooser.setInitialDirectory(sessionFile);

		File selected = chooser.showOpenDialog(this.bp_mainpane.getScene().getWindow());
		if (selected != null) {
			Properties session = new Properties();
			try {
				InputStream is = new FileInputStream(selected);
				session.load(is);
				is.close();
			} catch (FileNotFoundException e) {
				MainApplication.showAlert("Sitzung wurde nicht geöffnet", "Die Datei wurde nicht gefunden");
			} catch (IOException e) {
				MainApplication.showAlert("Sitzung wurde nicht geöffnet", "Die Datei konnte nicht geladen werden");
			}
			String names = session.getProperty("files", "");
			
			
			libInfoListController.libraryListProperty().clear();
			loadLibraries(names.split(","));
		}
	}

	@FXML
	void StoreSession(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().setAll(new ExtensionFilter("Sitzung", "*.session"));

		String sessionDir = UserPreferences.get().getOrDefault(UserPreferences.SESSION_DIR);
		File sessionFile = new File(sessionDir);
		chooser.setInitialDirectory(sessionFile);

		File selected = chooser.showSaveDialog(this.bp_mainpane.getScene().getWindow());
		if (selected != null) {
			Properties session = new Properties();

			ObservableList<SimpleKeywordLibrary> libs = libInfoListController.libraryListProperty().get();
			StringBuilder names = new StringBuilder();
			String defaultLibraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);
			libs.forEach(library -> {
				String name = library.getAbsolutePath();
				if (name.startsWith(defaultLibraryDir)) {
					name = name.substring(defaultLibraryDir.length());
				}
				names.append(", " + name);
			});

			session.setProperty("files", names.substring(2));
			try {
				OutputStream os = new FileOutputStream(selected);
				session.store(os,
						"In dieser Datei stehen die Bibliotheken die geladen werden sollen. Ausgangspunkt ist der Standard-Bibliothekspfad");
				os.close();
			} catch (FileNotFoundException e) {
				MainApplication.showAlert("Sitzung wurde nicht gespeichert", "Die Datei wurde nicht gefunden");
			} catch (IOException e) {
				MainApplication.showAlert("Sitzung wurde nicht gespeichert",
						"Die Datei konnte nicht geschrieben werden");
			}

		}
	}

	private void loadLibraries(String... names) {
		String libraryDir = UserPreferences.get().getOrDefault(UserPreferences.LIBRARY_DIR);

		Collection<File> files = new ArrayList<>();
		for (String name : names) {
			if (name.trim().length() > 0) {				
				File libraryFile = new File(libraryDir, name.trim());
				files.add(libraryFile);
			}
		}

		Collection<SimpleKeywordLibrary> loaded = new ArrayList<>();
		Collection<String> notloaded = new ArrayList<>();
		for (File tmp : files) {
			try {
				SimpleKeywordLibrary lib = LibraryLoader.getInstance().loadSimpleKeywordLibrary(tmp.getAbsolutePath());
				loaded.add(lib);
			} catch (IOException | KeywordLibraryException e) {
				notloaded.add(tmp.getName());
			}
		}

		libInfoListController.libraryListProperty().addAll(loaded);

		if (notloaded.size() > 0) {
			StringBuilder builder = new StringBuilder();
			notloaded.forEach((name) -> builder.append(", " + name));
			String msg = builder.toString().substring(2);
			MainApplication.showAlert("Folgende Bibliotheken konnten nicht geladen werden", msg);
		}
	}

	private Collection<File> getFiles(File file) {
		Collection<File> files = new ArrayList<>();
		if (file.isFile()) {
			files.add(file);
		} else if (file.isDirectory()) {
			File[] dirFiles = file.listFiles();
			for (File tmp : dirFiles) {
				files.addAll(getFiles(tmp));
			}
		}
		return files;
	}

	private File showFileSaver(String initDir) {
		FileChooser fc = new FileChooser();
		ExtensionFilter tstFilter = new ExtensionFilter("Testdatei", "*.tst");
		fc.getExtensionFilters().add(tstFilter);

		File selected = fc.showSaveDialog(bp_mainpane.getScene().getWindow());

		if (initDir != null && initDir.length() > 0) {
			File initDirFile = new File(initDir);
			if (initDirFile.exists() && initDirFile.isDirectory()) {
				fc.setInitialDirectory(initDirFile);
			}
		}

		if (selected != null && !selected.getName().endsWith(".tst")) {
			String header = "Ungültige Dateiendung";
			String content = "Die Dateiendung muss \".tst\" sein";
			MainApplication.showAlert(header, content);
		}
		return selected;
	}

	private File showFileOpener(String initDir) {
		FileChooser fc = new FileChooser();
		ExtensionFilter tstFilter = new ExtensionFilter("Testdatei", "*.tst");
		fc.getExtensionFilters().add(tstFilter);

		File selected = fc.showOpenDialog(bp_mainpane.getScene().getWindow());

		if (initDir != null && initDir.length() > 0) {
			File initDirFile = new File(initDir);
			if (initDirFile.exists() && initDirFile.isDirectory()) {
				fc.setInitialDirectory(initDirFile);
			}
		}

		if (selected != null && !selected.getName().endsWith(".tst")) {
			String header = "Ungültige Dateiendung";
			String content = "Die Dateiendung muss \".tst\" sein";
			MainApplication.showAlert(header, content);
		}
		return selected;
	}

	private void callTexteditor(File selected) {
		String editor = UserPreferences.get().getOrDefault(UserPreferences.EDITOR_EXE);
		ProcessBuilder process = new ProcessBuilder(editor, selected.getAbsolutePath());
		try {
			process.start();
		} catch (IOException e) {
			MainApplication.showAlert("Texteditor wurde nicht geöffnet",
					"In den Einstellungen kann der Pfad zum Editor angepasst werden");
		}
	}
}
