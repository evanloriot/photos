package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AdminController {
	@FXML
	Button logout;
	@FXML
	Button add;
	@FXML
	Button delete;
	@FXML
	ListView<String> users;
	
	ObservableList<String> obsList;
	
	public void start(Stage mainStage) {
		
	}
}
