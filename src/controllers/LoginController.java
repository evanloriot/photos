package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

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
import models.Album;
import models.SerialUtils;
import models.User;

/**
 * This class is responsible for handling all user interactions while the Login view is active.
 * The LoginCotroller class passes data back and forth between the user and the system.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class LoginController {
	/**
	 * Button to log in as username typed into username
	 */
	@FXML
	Button login;
	/**
	 * Field containing user inputted username
	 */
	@FXML
	TextField username;
	
	/**
	 * List of all known users.
	 */
	ArrayList<User> userList;
	
	/**
	 * Called when the a user enters the Login view. Handles interactions with the user.
	 * @param mainStage current application stage
	 */
	public void start(Stage mainStage) {
		try {
			userList = SerialUtils.getUserList();
			boolean hasStock = false;
			for(int i = 0; i < userList.size(); i++) {
				if(userList.get(i).username.equals("stock")) {
					hasStock = true;
				}
			}
			if(!hasStock) {
				createStockUser();
			}
		} catch (IOException e) {
			SerialUtils.errorAlert(e);
		}
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
						adminController.start(mainStage, userList);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(IOException e) {
						SerialUtils.errorAlert(e);
					}
				}
				else if(userExists(username.getText())){
					User user = getUser(username.getText());
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/albums.fxml"));
						Parent root = (Parent) loader.load();
						
						AlbumsController albumsController = loader.getController();
						albumsController.user = user;
						albumsController.start(mainStage);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(IOException e) {
						SerialUtils.errorAlert(e);
					}
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
						adminController.start(mainStage, userList);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(IOException e) {
						SerialUtils.errorAlert(e);
					}
				}
				else if(userExists(username.getText())){
					User user = getUser(username.getText());
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/albums.fxml"));
						Parent root = (Parent) loader.load();
						
						AlbumsController albumsController = loader.getController();
						albumsController.user = user;
						albumsController.start(mainStage);
						
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					catch(IOException e) {
						SerialUtils.errorAlert(e);
					}
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
	
	/**
	 * Checks to see if a user with the passed user name already exists.
	 * @param userName The name of the user we're looking for.
	 * @return boolean -- True if the user is in the system, false otherwise.
	 */
	public boolean userExists(String userName) {
		for(User u : userList){
			if(u.toString().equals(userName)){
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * Looks through the user list to find a specific user.
	 * @param userName The name of the user object you would like.
	 * @return User -- It returns the requested user object if found, null otherwise.
	 */
	public User getUser(String userName) {
		for(User u : userList){
			if(u.toString().equals(userName)){
				return u;
			}
		}
		
		return null;
	}
	/*
	 * Creates a stock user with an album named "Stock" populated with photos from /models/stock/ if no user exists already
	 */
	public void createStockUser() {
		if(userExists("stock")) {
			return;
		}

		try {
			User user = new User("stock");
			Album album = new Album("Stock");
			String[] photos = {
					"android-logo.png", 
					"c-logo.png", 
					"eclipse-logo.png", 
					"erlang-logo.png", 
					"git-logo.png", 
					"haskell-logo.png", 
					"java-logo.png", 
					"python-logo.png", 
					"r-logo.png", 
					"sublime-logo.png"
			};
			for(int j = 0; j < photos.length; j++) {
				String location = Paths.get("./src/models/stock/" + photos[j]).toFile().getCanonicalPath();
				String[] split = location.split("\\\\");
				location = "file:" + File.separator + File.separator;
				for(int i = 0; i < split.length; i++) {
					location += File.separator;
					location += split[i];
				}
				album.addPhoto(location);
			}
			user.addAlbum(album);
			SerialUtils.writeUserToFile(user);
			userList.add(user);
		}
		catch(IOException e) {
			SerialUtils.errorAlert(e);
		}
	}
}
