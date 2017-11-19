package controllers;

import java.io.File;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.Photo;
import models.PhotoListViewCell;
import models.SerialUtils;
import models.User;

public class AlbumController {
	@FXML
	Label title;
	@FXML
	Button logout;
	@FXML
	Button addPhoto;
	@FXML
	Button deletePhoto;
	@FXML
	Button movePhoto;
	@FXML
	Button copyPhoto;
	@FXML
	Button playSlideshow;
	@FXML
	Button back;
	@FXML
	ListView<Photo> photosListView;
	
	ObservableList<Photo> photos;
	
	Album album;
	User user;
	
	public void start(Stage mainStage) {
		title.setText(title.getText() + " " + album.name + " - " + user.username);
		
		photos = FXCollections.observableArrayList(album.photos);
		photosListView.setItems(photos);
		photosListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			@Override
			public ListCell<Photo> call(ListView<Photo> listView){
				return new PhotoListViewCell();
			}
		});
		
		photosListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Photo photo = photosListView.getSelectionModel().getSelectedItem();
				if(click.getClickCount() == 2) {
					try {
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
					catch(Exception e) {
						System.out.println("error");
						e.printStackTrace();
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
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
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
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
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
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
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
						location = "file://";
						for(int i = 0; i < split.length; i++) {
							location += "/";
							location += split[i];
						}
					}
					addPhoto(location);
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		deletePhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = photosListView.getSelectionModel().getSelectedItem();
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
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		movePhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = photosListView.getSelectionModel().getSelectedItem();
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
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		copyPhoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Photo photo = photosListView.getSelectionModel().getSelectedItem();
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
							user.getAlbum(albumName).addPhoto(photo.location);
							photos.add(photo);
							photosListView.refresh();
						});
					}
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
	}

	public void addPhoto(String location) {
		if(location.isEmpty()) {
			return;
		}
		try{
			Photo photo = new Photo(location);
			photo.album = album;
			album.addPhoto(location);
			photos.add(photo);
			SerialUtils.writeUserToFile(user);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deletePhoto(String location) {
		album.deletePhoto(location);
		for(int i = 0; i < photos.size(); i++) {
			if(location.equals(photos.get(i).location)) {
			try{				
				photos.remove(i);
				SerialUtils.writeUserToFile(user);
			} catch(Exception e){
				e.printStackTrace();
			}
				return;
			}
		}
	}
}
