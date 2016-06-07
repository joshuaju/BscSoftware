package ui.misc;

import java.util.function.Consumer;

import external.SimpleKeywordLibrary;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KeywordLibraryListCell extends ListCell<SimpleKeywordLibrary> {
	private Label lblCross;
	
	public KeywordLibraryListCell() {
		super();
		
		Image img = new Image(getClass().getClassLoader().getResourceAsStream("img/cross.png"));
		ImageView ivCross = new ImageView(img);
		ivCross.setFitHeight(12);
		ivCross.setFitWidth(12);
		lblCross = new Label();
		lblCross.setGraphic(ivCross);	
		lblCross.visibleProperty().bind(hoverProperty());
	}

	@Override
	protected void updateItem(SimpleKeywordLibrary item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setVisible(false);
			setText("");
			setGraphic(null);
		} else if (item != null) {
			setText(item.getName());
			setVisible(true);
			setGraphic(lblCross);
		}
	}

}
