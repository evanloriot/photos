package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please enter a username.");
				    alert.showAndWait();
				}
				else if(username.getText().equals("admin")) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin.fxml"));
						Parent root = (Parent) loader.load();
						
						AdminController adminController = loader.getController();
						adminController.start(mainStage);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(Exception e) {
						System.out.println("error");
						e.printStackTrace();
					}
				}
				else if(userExists(username.getText())){
					//user functionality
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("User does not exist.");
				    alert.showAndWait();
				}
			}
		});
		
		username.setOnKeyPressed((event) -> {
			if(event.getCode() == KeyCode.ENTER) {
				if(username.getLength() == 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please enter a username.");
				    alert.showAndWait();
				}
				else if(username.getText().equals("admin")) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin.fxml"));
						Parent root = (Parent) loader.load();
						
						AdminController adminController = loader.getController();
						adminController.start(mainStage);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(Exception e) {
						System.out.println("error");
						e.printStackTrace();
					}
				}
				else if(userExists(username.getText())){
					//user functionality
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("User does not exist.");
				    alert.showAndWait();
				}
			}
		});
	}
	
	public boolean userExists(String user) {
		//check to see if user exists;
		return true;
	}
}
