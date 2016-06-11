package designer.misc;

import external.SimpleKeywordLibrary;
import javafx.scene.control.ListCell;

public class KeywordLibraryListCell extends ListCell<SimpleKeywordLibrary> {
	

	@Override
	protected void updateItem(SimpleKeywordLibrary item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setVisible(false);
			setText("");					
		} else if (item != null) {
			setText(item.getName());
			setVisible(true);			
		}
	}

}
