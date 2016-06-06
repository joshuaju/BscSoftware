package ui.misc;

import external.KeywordLibrary;
import javafx.scene.control.ListCell;

public class KeywordLibraryListCell extends ListCell<KeywordLibrary> {

	public KeywordLibraryListCell() {
		super();
	}

	@Override
	protected void updateItem(KeywordLibrary item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText("");
		} else if (item != null) {
			setText(item.getName());
		}
	}
}
