package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import application.SerialUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
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
	
	public void start(Stage mainStage, ArrayList<User> userList) {
		obsList = FXCollections.observableArrayList(userList);
		
		User admin = SerialUtils.getUser(userList, "admin");
		obsList.remove(admin);
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
				Optional<String> result = Utilities.showDialog("Add", "Add User", "Please enter username to add to users list.", "Username", "Username:");
						
				result.ifPresent(userName -> {
					if(doesUserExist(userName)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Username already exists.");
					    alert.showAndWait();
					}
					else {
						addUser(userName, userList);
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
						deleteUser(user.toString(), userList);
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
	
	public boolean doesUserExist(String userName) {
		for(int i = 0 ; i < obsList.size(); i++) {
			if(userName.equals(obsList.get(i).toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteUser(String userName, ArrayList<User> userList) {
		for(Iterator<User> iterator = obsList.iterator() ; iterator.hasNext() ; ){
			User u = iterator.next();
			if(u.toString().equals(userName)){
				iterator.remove();
				userList.remove(u);
				SerialUtils.deleteUserFromFile(userName);
				SerialUtils.writeUserList(userList);
			}
		}
	}
	
	public void addUser(String userName, ArrayList<User> userList) {
		User u = new User(userName);
		try {
			SerialUtils.writeUserToFile(u);
			obsList.add(u);
			userList.add(u);
			SerialUtils.writeUserList(userList);
		} catch (Exception e){
			System.out.println("Error occured adding a user");
			e.printStackTrace();
		}

	}
}