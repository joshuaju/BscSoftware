package executer.misc;


import java.io.File;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileListCell extends ListCell<File>{
	
	private ImageView ivFile;
	private ImageView ivFolder;
	
	
	public FileListCell() {
		Image img = new Image(getClass().getClassLoader().getResourceAsStream("img/file.png"));
		ivFile = new ImageView(img);
		img = new Image(getClass().getClassLoader().getResourceAsStream("img/folder.png"));
		ivFolder = new ImageView(img);
		
		int fitW = 18;
		int fitH = fitW;
		ivFile.setFitWidth(fitW);
		ivFile.setFitHeight(fitH);
		ivFolder.setFitWidth(fitW);
		ivFolder.setFitHeight(fitH);
		
	}
	
	@Override
	protected void updateItem(File item, boolean empty) {
		super.updateItem(item, empty);
		if (empty){
			setText("");
			setGraphic(null);
		} else {
			setText(item.getAbsolutePath());
			if (item.isDirectory()){
				setGraphic(ivFolder);
			} else if (item.isFile()){
				setGraphic(ivFile);
			}
			
		}
	}

}
