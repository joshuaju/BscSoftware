import java.util.Optional;

import annotations.Keyword;
import annotations.KeywordLibrary;
import custom.InputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Stellt Dialog zur Eingabe von Werten während des Testlaufs bereit")
public class DialogKeywordLibrary {
	
	@Keyword(Description = "Öffnet eine Dialog mit einer Nachricht. Der Nutzer muss bestätigen, damit fortgefahren wird", Name = "Warte auf Bestätigung")
	public void showConfirmDialog(String message){
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setTitle("Bestätigen");
		al.setContentText(message);
		Optional<ButtonType> result = al.showAndWait();
		if (result.isPresent() && result.get() != ButtonType.OK){
			throw new AssertionError("Der Benutzer hat den Test abgebrochen");
		}
	}
	
	@Keyword(Description = "Öffnet einen Dialog der den Tester zum eingeben eines Textes auffordert", Name = "Frage nach Text für", Parameter = "Eine Nachricht, die im Dialog angezeigt wird", Return = "Den im Dialog eingetragen Wert")
	public String showStringDialog(String message) {
		TextInputDialog dlg = new InputDialog("Wert als Text eintragen", message);

		Optional<String> result = dlg.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return "";
	}

	@Keyword(Description = "Öffnet einen Dialog der den Tester zum eingeben einer ganzen Zahl auffordert", Name = "Frage nach ganzer Zahl für", Parameter = "Eine Nachricht, die den einzutragenden Wert beschreibt", Return = "Den im Dialog eingetragen Wert")
	public Integer showIntegerDialog(String message) {
		InputDialog dlg = new InputDialog("Wert als ganze Zahle eintragen", message);
		dlg.setInputValidator(text -> {
			try {
				Integer.parseInt(text);
				return true;
			} catch (Exception e) {
				return false;
			}
		});
		Optional<String> result = dlg.showAndWait();
		if (result.isPresent()) {
			if (result.get().length() > 0) {
				return Integer.parseInt(result.get());
			}
		}
		throw new AssertionError("Der Dialog wurde ohne Wert beendet");
	}

	@Keyword(Description = "Öffnet einen Dialog der den Tester zum eingeben einer Kommazahl auffordert", Name = "Frage nach Kommazahl für", Parameter = "Eine Nachricht, die im Dialog angezeigt wird", Return = "Den im Dialog eingetragen Wert")
	public Double showDoubleDialog(String message) {
		InputDialog dlg = new InputDialog("Wert als Kommazahl eintragen", message);
		dlg.setInputValidator(text -> {
			try {
				Double.parseDouble(text);
				return true;
			} catch (Exception e) {
				return false;
			}
		});
		Optional<String> result = dlg.showAndWait();
		if (result.isPresent()) {
			if (result.get().length() > 0) {
				return Double.parseDouble(result.get());
			}
		}
		throw new AssertionError("Der Dialog wurde ohne Wert beendet");
	}

}
