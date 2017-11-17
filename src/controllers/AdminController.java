package controllers;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;
import utilities.Utilities;

public class AdminController {
	@FXML
	Button logout;
	@FXML
	Button add;
	@FXML
	Button delete;
	@FXML
	ListView<User> users;
	
	ObservableList<User> obsList;
	
	public void start(Stage mainStage) {
		obsList = FXCollections.observableArrayList(getUsers());
		
		users.setItems(obsList);
		
		logout.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
					Parent root = (Parent) loader.load();
					
					LoginController loginController = loader.getController();
					loginController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				Optional<String> result = Utilities.showDialog("Add", "Add User", "Please add a username to add to users list.", "Username", "Username:");
						
				result.ifPresent(userName -> {
					if(doesUserExist(userName)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Username already exists.");
					    alert.showAndWait();
					}
					else {
						addUser(userName);
					}
				});
			}
		});
		
		delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				User user = users.getSelectionModel().getSelectedItem();
				if(user != null) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Are you sure you want to delete user \"" + user.toString() + "\"?");
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK) {
						deleteUser(user.toString());
					}
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please select a user to delete.");
				    alert.showAndWait();
				}
			}
		});
	}
	
	public boolean doesUserExist(String username) {
		for(int i = 0 ; i < obsList.size(); i++) {
			if(username.equals(obsList.get(i).toString())) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<User> getUsers(){
		//implement get users from db
		
		//temp
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("evan"));
		users.add(new User("temp1"));
		users.add(new User("temp2"));
		users.add(new User("temp3"));
		users.add(new User("temp4"));
		return users;
	}
	
	public void deleteUser(String username) {
		//implement delete user from db
		
		for(int i = 0; i < obsList.size(); i++) {
			if(username.equals(obsList.get(i).toString())) {
				obsList.remove(i);
				return;
			}
		}
	}
	
	public void addUser(String username) {
		//implement add user to db
		
		obsList.add(new User(username));
	}
}
