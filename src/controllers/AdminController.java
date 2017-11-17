package controllers;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.User;

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
	
	public void deleteUser(User user) {
		//implement delete user from db
	}
	
	public void addUser(String username) {
		//implement add user to db
	}
}
