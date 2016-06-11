package designer.misc;

import java.io.IOException;

import application.property.UserPreferences;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;

public class DialogFactory {

	private static Window owner;
	
	public static void initOwner(Window owner) {
		DialogFactory.owner = owner;
	}
	
	public static <T> Dialog<T> createDialog(String header, String content){
		Dialog<T> dlg = new Dialog<>();
		dlg.setHeaderText(header);
		dlg.setContentText(content);
		dlg.initOwner(owner);
		return dlg;
	}
	
	public static Alert createAlert(String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initOwner(owner);
		return alert;
	}
	
	public static Dialog<String> createSettingsDialog(){
		FXMLLoader loader = new FXMLLoader(
				DialogFactory.class.getClassLoader().getResource("designer/fxml/TestDesignerSettingsView.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DialogPane pane = new DialogPane();
		pane.setContent(root);
		pane.getButtonTypes().setAll(ButtonType.OK);
		
		Dialog<String> dlg = new Dialog<>();
		dlg.setTitle("Einstellungen");
		dlg.setDialogPane(pane);			
		dlg.setResizable(false);
		dlg.initOwner(owner);
		dlg.setOnCloseRequest(event -> UserPreferences.get().store());		
		return dlg;
	}
	
	
}
