package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.PhotoListViewCell;
import models.SerialUtils;
import models.User;

/**
 * This class responsible for handling all user interactions while the album view is active.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class AlbumController {
	/**
	 * View title
	 */
	@FXML
	Label title;
	/**
	 * Button to log user out of system
	 */
	@FXML
	Button logout;
	/**
	 * Button to add a Photo to Album
	 */
	@FXML
	Button addPhoto;
	/**
	 * Button to delete selected Photo from Album
	 */
	@FXML
	Button deletePhoto;
	/**
	 * Button to move selected Photo from Album to another Album
	 */
	@FXML
	Button movePhoto;
	/**
	 * Button to copy selected Photo from Album to another Album
	 */
	@FXML
	Button copyPhoto;
	/**
	 * Button to send user to slideshow view
	 */
	@FXML
	Button playSlideshow;
	/**
	 * Button to send user to previous screen
	 */
	@FXML
	Button back;
	/**
	 * Listing of all Photos in Album
	 */
	@FXML
	ListView<ArrayList<Photo>> photosListView;
	
	/**
	 * When the user is in an album this list keeps track of all the photos that album
	 * contains and displays them to the user.
	 */
	ObservableList<ArrayList<Photo>> photos;
	
	/**
	 * Keeps track of the currently selected photo.
	 */
	Photo selected = null;
	
	/**
	 * Keeps track of the album that is currently being interacted with.
	 */
	Album album;
	
	/**
	 * Keeps track of the current user.
	 */
	User user;
	
	/**
	 * Called when the user enters the album view. Handles all user interactions when adding items to/editing items in
	 * an album.
	 * @param mainStage current application stage
	 */
	public void start(Stage mainStage) {
		title.setText(title.getText() + " " + album.name + " - " + user.username);
		
		photos = FXCollections.observableArrayList(getPhotos());
		photosListView.setItems(photos);
		photosListView.setCellFactory(x -> new PhotoListViewCell<>());
		
		photosListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Photo photo = new Photo("", 0);
				if(click.getClickCount() == 2) {
					try {
						if(photosListView.getSelectionModel().getSelectedItem() != null && ((int)click.getSceneX() / 133) < photosListView.getSelectionModel().getSelectedItem().size()) {
							if(photos.size() == 1) {
								if(click.getSceneY() < 325) {
									photo = photosListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
									
									FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/photo.fxml"));
									Parent root = (Parent) loader.load();
									
									PhotoController photoController = loader.getController();
									photoController.user = user;
									photoController.album = album;
									photoController.photoObj = photo;
									photoController.backLocation = "album";
									photoController.start(mainStage);
									
									Scene scene = new Scene(root);
									mainStage.setScene(scene);
								}
							}
							else {
								photo = photosListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
								
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/photo.fxml"));
								Parent root = (Parent) loader.load();
								
								PhotoController photoController = loader.getController();
								photoController.user = user;
								photoController.album = album;
								photoController.photoObj = photo;
								photoController.backLocation = "album";
								photoController.start(mainStage);
								
								Scene scene = new Scene(root);
								mainStage.setScene(scene);
							}
						}
					}
					catch(IOException e) {
						SerialUtils.errorAlert(e);
					}
				}
				else if(click.getClickCount() == 1){
					if(photosListView.getSelectionModel().getSelectedItem() != null && ((int)click.getSceneX() / 133) < photosListView.getSelectionModel().getSelectedItem().size()) {
						if(photos.size() == 1) {
							if(click.getSceneY() < 325) {
								selected = photosListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
							}
						}
						else{
							selected = photosListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
						}
					}
				}
			}
		});
		
		back.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
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
				catch(IOException e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		playSlideshow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				if(album.numPhotos > 0)
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/slideshow.fxml"));
					Parent root = (Parent) loader.load();

					SlideshowController slideshowController = loader.getController();
					slideshowController.user = user;
					slideshowController.album = album;
					slideshowController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
				}
				catch(IOException e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		addPhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					String location = "";
					
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Select a Photo");
					fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
					File file = fileChooser.showOpenDialog(mainStage);
					if(file != null) {
						location = file.getPath();
						String[] split = location.split("\\\\");
						location = "file:" + File.separator + File.separator;
						for(int i = 0; i < split.length; i++) {
							location += File.separator;
							location += split[i];
						}
					}
					addPhoto(location);
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		deletePhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = selected;
					if(photo == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Please select an photo.");
					    alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirmation Dialog");
						alert.setHeaderText("Are you sure you want to delete this photo?");
						Optional<ButtonType> result = alert.showAndWait();
						if(result.get() == ButtonType.OK) {
							deletePhoto(photo.location);
						}
					}
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		movePhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = selected;
					if(photo == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Please select an photo.");
					    alert.showAndWait();
					}
					else {
						Dialog<String> dialog = new Dialog<>();
						dialog.setTitle("Move Photo");
						dialog.setHeaderText("Please select album to move to.");
						
						ButtonType addButtonType = new ButtonType("Move", ButtonData.OK_DONE);
						dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
						
						GridPane grid = new GridPane();
						grid.setHgap(10);
						grid.setVgap(10);
						grid.setPadding(new Insets(20, 150, 10, 10));
						
						ObservableList<Album> albums = FXCollections.observableArrayList(user.albums);
						ListView<Album> albumListView = new ListView<Album>();
						albumListView.setItems(albums);
						grid.add(albumListView, 0, 0);
						
						Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
						addButton.setDisable(true);
						
						albumListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent click) {
								addButton.setDisable(false);
							}
						});
						
						dialog.getDialogPane().setContent(grid);
						
						dialog.setResultConverter(dialogButton -> {
							if(dialogButton == addButtonType) {
								return albumListView.getSelectionModel().getSelectedItem().name;
							}
							return null;
						});
						
						Optional<String> result = dialog.showAndWait();
						
						result.ifPresent(albumName -> {
							user.getAlbum(albumName).addPhoto(photo.location);
							deletePhoto(photo.location);
						});
					}
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		copyPhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = selected;
					if(photo == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Please select an photo.");
					    alert.showAndWait();
					}
					else {
						Dialog<String> dialog = new Dialog<>();
						dialog.setTitle("Copy Photo");
						dialog.setHeaderText("Please select album to copy to.");
						
						ButtonType addButtonType = new ButtonType("Copy", ButtonData.OK_DONE);
						dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
						
						GridPane grid = new GridPane();
						grid.setHgap(10);
						grid.setVgap(10);
						grid.setPadding(new Insets(20, 150, 10, 10));
						
						ObservableList<Album> albums = FXCollections.observableArrayList(user.albums);
						ListView<Album> albumListView = new ListView<Album>();
						albumListView.setItems(albums);
						grid.add(albumListView, 0, 0);
						
						Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
						addButton.setDisable(true);
						
						albumListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent click) {
								addButton.setDisable(false);
							}
						});
						
						dialog.getDialogPane().setContent(grid);
						
						dialog.setResultConverter(dialogButton -> {
							if(dialogButton == addButtonType) {
								return albumListView.getSelectionModel().getSelectedItem().name;
							}
							return null;
						});
						
						Optional<String> result = dialog.showAndWait();
						
						result.ifPresent(albumName -> {
							if(albumName.equals(album.name)) {
								addPhoto(photo.location);
							}
							else {
								user.getAlbum(albumName).addPhoto(photo.location);
								try {
									SerialUtils.writeUserToFile(user);
								}
								catch(IOException e) {
									SerialUtils.errorAlert(e);
								}
							}
							try {
								SerialUtils.writeUserToFile(user);
							}
							catch(IOException e) {
								SerialUtils.errorAlert(e);
							}
							photosListView.refresh();
						});
					}
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
	}

	/**
	 * Adds a photo to the album that the user is currently in.
	 * @param location Location of the photo that is being added.
	 */
	public void addPhoto(String location) {
		if(location.isEmpty()) {
			return;
		}
		try{
			Photo photo = user.getAlbum(album.name).addPhoto(location);
			photo.album = album;
			
			SerialUtils.writeUserToFile(user);
			if(photos.size() == 0) {
				photos.add(new ArrayList<Photo>());
			}
			photos.get(photos.size() - 1).add(photo);
			resizePhotos();
			photosListView.refresh();
		} catch(IOException e){
			SerialUtils.errorAlert(e);
		}
	}
	
	/**
	 * Deletes a photo from the album the user is currently in.
	 * @param location Location of the photo that is being deleted.
	 */
	public void deletePhoto(String location) {
		album.deletePhoto(location);
		for(int i = 0; i < photos.size(); i++) {
			for(int j = 0; j < photos.get(i).size(); j++) {
				if(location.equals(photos.get(i).get(j).location)) {
					try{				
						photos.get(i).remove(j);
						resizePhotos();
						photosListView.refresh();
						SerialUtils.writeUserToFile(user);
					} catch(IOException e){
						SerialUtils.errorAlert(e);
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Gets a list all of the photos in the current album.
	 * @return A list of all of the photo objects contained in this album.
	 */
	public ArrayList<ArrayList<Photo>> getPhotos(){
		ArrayList<ArrayList<Photo>> output = new ArrayList<ArrayList<Photo>>();
		ArrayList<Photo> row = new ArrayList<Photo>();
		int col;
		for(int i = 0; i < user.getAlbum(album.name).photos.size(); i++) {
			col = i % 6;
			if(col == 0) {
				row = new ArrayList<Photo>();
				output.add(row);
			}
			row.add(user.getAlbum(album.name).photos.get(i));
		}
		return output;
	}

	/**
	 * Resizes a list of photo objects to be displayed
	 */
	public void resizePhotos() {
		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).size() > 6) {
				if(i == photos.size() - 1) {
					photos.add(new ArrayList<Photo>());
				}
				while(photos.get(i).size() > 6) {
					photos.get(i+1).add(0, photos.get(i).get(photos.get(i).size() - 1));
					photos.get(i).remove(photos.get(i).size() - 1);
				}
			}	
			else if(i != photos.size() - 1 && photos.get(i).size() < 6) {
				photos.get(i).add(photos.get(i+1).get(0));
				photos.get(i+1).remove(0);
				if(photos.get(i+1).size() == 0) {
					photos.remove(i+1);
				}
				return;
			}
		}
	}
}
