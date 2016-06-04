package ui.misc;

import external.Keyword;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class KeywordListCell extends ListCell<Keyword> {

	public KeywordListCell() {
		super();

		setOnDragDetected(event -> {
			Keyword thiskeyword = getItem();
			if (thiskeyword == null) {
				return;
			}

			Dragboard db = startDragAndDrop(TransferMode.COPY);
			ClipboardContent content = new ClipboardContent();
			content.putString(getDragAndDropText(thiskeyword));
			db.setContent(content);
			event.consume();
		});

		setOnDragOver(event -> {
			if (event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.COPY);
			}
			event.consume();
		});

		setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			if (db.hasString()) {
				String text = db.getString();
				System.out.println(text);
			}
			event.setDropCompleted(true);
			event.consume();
		});
		setOnDragDone(DragEvent::consume);
	}

	@Override
	protected void updateItem(Keyword item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.getName());
		}
	}

	private String getDragAndDropText(Keyword thiskeyword) {
		String paramtext = "";

		for (Class<?> tmpParam : thiskeyword.getParameterTypes()) {
			String name = tmpParam.getSimpleName().toUpperCase();			
			paramtext += name + ", ";
		}
		if (paramtext.length() > 0) {
			paramtext = paramtext.replace(Object.class.getSimpleName().toUpperCase(), "ANY");
			paramtext = paramtext.substring(0, paramtext.length() - 2);
		} else {
			paramtext = Void.class.getSimpleName();
		}
		

		return thiskeyword.getName() + ((paramtext.length() > 0) ? "\t\t" + paramtext : "") + "\n";
	}

}
