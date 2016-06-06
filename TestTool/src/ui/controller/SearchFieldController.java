package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchFieldController implements Initializable {

	@FXML
	private TextField tf_search;

	@FXML
	private Label lblImage;

	private ObjectProperty<ImageView> imageProperty;

	private ImageView imgSearch;
	private ImageView imgCross;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imageProperty = new SimpleObjectProperty<>();
		imgSearch = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/search.png")));
		imgSearch.setFitWidth(16);
		imgSearch.setFitHeight(16);
		imgCross = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/cross.png")));
		imgCross.setFitWidth(16);
		imgCross.setFitHeight(16);
		imageProperty.set(imgSearch);		
				
		lblImage.setOnMouseClicked(event -> tf_search.setText(""));
		lblImage.setOnMouseClicked((event) -> {
			if (imageProperty.get() == imgCross){
				tf_search.clear();
			}
		});
		
		tf_search.textProperty().addListener((obs, oldText, newText) -> {
			if (newText.length() == 0){
				imageProperty.set(imgSearch);
			} else {
				imageProperty.set(imgCross);				
			}
		});
		
		lblImage.graphicProperty().bind(imageProperty);	
	}

	public <T> FilteredList<T> getFilteredList(ObservableList<T> source) {
		return new FilteredList<>(source);
	}
	
	public StringProperty textProperty(){
		return tf_search.textProperty();
	}
	

}
