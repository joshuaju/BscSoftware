package ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import external.Keyword;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

public class KeywordInformationController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TitledPane info_mainpane;

	@FXML
	private TextArea ta_description;

	@FXML
	private TextArea ta_parameter;

	@FXML
	private TextArea ta_return;

	private ObjectProperty<Keyword> currentKeyword;

	@FXML
	void initialize() {
		assert info_mainpane != null : "fx:id=\"info_mainpane\" was not injected: check your FXML file 'KeywordInformationView.fxml'.";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		currentKeyword = new SimpleObjectProperty<>();
		currentKeyword.addListener((obs) -> updateInformation());
	}

	public void setKeyword(Keyword keyword) {
		currentKeyword.set(keyword);
	}

	public ObjectProperty<Keyword> currentKeywordProperty() {
		return currentKeyword;
	}

	private void updateInformation() {
		Keyword keyword = currentKeyword.get();
		setTitle(keyword);
		setDescription(keyword);
		setParams(keyword);
		setReturn(keyword);
		
		if (keyword != null) {
			info_mainpane.setVisible(true);		
		} else {
			info_mainpane.setVisible(true);			
		}		
	}

	private void setTitle(Keyword keyword) {
		String name = "";
		if (keyword != null) {
			name = keyword.getName();
		}
		info_mainpane.setText(name);
	}

	private void setDescription(Keyword keyword) {
		String text = "";
		if (keyword != null) {
			text = keyword.getDescription();
		}
		ta_description.setText(text);
	}

	private void setParams(Keyword keyword) {
		String text = "";
		if (keyword != null) {
			text = keyword.getParameter();
			if (text.length() == 0) {
				String params = "";
				for (Class<?> tmpParam : keyword.getParameterTypes()) {
					params += tmpParam.getSimpleName() + ", ";
				}
				if (params.length() > 0) {
					params = params.substring(0, params.length() - 2);
				} else {
					params = Void.class.getSimpleName();
				}
				text = params;
			}
		}
		ta_parameter.setText(text);
	}

	private void setReturn(Keyword keyword) {
		String text = "";
		if (keyword != null) {
			text = keyword.getReturn();
			if (text.length() == 0) {
				text = keyword.getReturntype().getSimpleName();
			}
		}
		ta_return.setText(text);
	}

}
