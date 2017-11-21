package controllers;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Album;
import models.SerialUtils;
import models.User;

/**
 * This class is responsible for handling all user interactions while the Slide show view.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class SlideshowController {
	@FXML
	Label title;
	@FXML
	Button logout;
	@FXML 
	Button back;
	@FXML
	Button previous;
	@FXML
	Button next;
	@FXML
	ImageView photo;
	
	/**
	 * Keeps track of the current user.
	 */
	User user;
	
	/**
	 * Keeps track of the current album.
	 */
	Album album;
	
	/**
	 * Current position in the album.
	 */
	int curr;
	
	/**
	 * Called whenever the user enters the slideShow view.
	 * @param mainStage current application stage
	 */
	public void start(Stage mainStage) {
		title.setText(title.getText() + " " + album.name + ": " + user.username);

		photo.setImage(new Image(album.photos.get(0).location));
		curr = 0;
		
		previous.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent click) {
				if(curr == 0) {
					curr = album.photos.size() - 1;
				}
				else {
					curr--;
				}
				photo.setImage(new Image(album.photos.get(curr).location));
			}
		});
		
		next.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent click) {
				if(curr == album.photos.size() - 1) {
					curr = 0;
				}
				else {
					curr++;
				}
				photo.setImage(new Image(album.photos.get(curr).location));
			}
		});
		
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
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		back.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/album.fxml"));
					Parent root = (Parent) loader.load();
					
					AlbumController albumController = loader.getController();
					albumController.user = user;
					albumController.album = album;
					albumController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
	}
}
