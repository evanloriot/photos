package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	Button login;
	@FXML
	TextField username;
	
	public void start(Stage mainStage) {
		login.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				if(username.getLength() == 0) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.initOwner(mainStage);
				    alert.setTitle("Login");
				    alert.setHeaderText("Username Empty");
				    alert.setContentText("Please enter a username.");
				    alert.showAndWait();
				}
				else if(username.getText().equals("admin")) {
					 //admin functionality
				}
				else {
					//user functionality
				}
			}
		});
	}
}
