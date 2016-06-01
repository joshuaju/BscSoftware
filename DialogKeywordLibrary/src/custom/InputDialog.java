package custom;

import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InputDialog extends TextInputDialog {

	private TextField tf_content;
	private Label lbl_error;
	private ButtonType btnOk;
	private BooleanProperty validProperty;

	public InputDialog(String headerText, String contentText) {
		this(headerText, contentText, "Ungültiger Wert");
	}

	public InputDialog(String headerText, String contentText, String errorText) {
		super();
		setTitle("Wertabfrage");
		setHeaderText(headerText);
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(0, 0, 0, 0));
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.CENTER);

		Label lbl_content = new Label(contentText);
		tf_content = new TextField();
		hbox.getChildren().addAll(lbl_content, tf_content);

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER_RIGHT);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(15, 15, 10, 15));
		lbl_error = new Label(errorText);
		lbl_error.setStyle("-fx-text-fill: firebrick;");
		vbox.getChildren().addAll(hbox, lbl_error);

		getDialogPane().setContent(vbox);
		btnOk = ButtonType.OK;
		getDialogPane().getButtonTypes().setAll(btnOk);

		validProperty = new SimpleBooleanProperty(true);
		Node btnOkNode = getDialogPane().lookupButton(btnOk);
		btnOkNode.disableProperty().bind(validProperty.not());
		lbl_error.visibleProperty().bind(validProperty.not());

		// Inhalt des Textfield ohne Enter speichern
		tf_content.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == false) {
				String text = tf_content.getText();
				tf_content.setText(text);
			}
		});

		// Dialog wird nicht geschlossen, wenn der Wert ungültig ist
		setOnCloseRequest(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent arg0) {				
				if (!validProperty.get()) {
					arg0.consume();
				}
			}
		});

		// Inhalt nach dem Schließen zurückgeben
		setResultConverter(btn -> {
			return tf_content.getText().trim();
		});

		setInputValidator((text) -> true);
		Platform.runLater(() -> tf_content.requestFocus());
	}

	public void setInputValidator(Function<String, Boolean> validator) {
		tf_content.clear();
		tf_content.textProperty().addListener((ChangeListener<String>) (observable, oldVal, newVal) -> {
			boolean valid = validator.apply(newVal);
			validProperty.set(valid);
		});

		validProperty.set(validator.apply(tf_content.getText()));
	}

}
