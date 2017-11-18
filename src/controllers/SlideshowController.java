package controllers;

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
import models.User;

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
	
	User user;
	Album album;
	int curr;
	
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
					System.out.println("error");
					e.printStackTrace();
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
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
	}
}
