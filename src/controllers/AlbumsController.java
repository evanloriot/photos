package controllers;

import java.util.Optional;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.AlbumListViewCell;
import models.User;
import utilities.Utilities;

public class AlbumsController {
	@FXML
	Button logout;
	@FXML
	Button searchByTag;
	@FXML
	Button searchByDate;
	@FXML
	Button createAlbum;
	@FXML
	Button renameAlbum;
	@FXML
	Button deleteAlbum;
	@FXML
	ListView<Album> albumsListView;
	
	ObservableList<Album> albums;
	
	User user;
	
	public void start(Stage mainStage) {
		albums = FXCollections.observableArrayList(user.albums);
		albumsListView.setItems(albums);
		albumsListView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>(){
			@Override
			public ListCell<Album> call(ListView<Album> listView){
				return new AlbumListViewCell();
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
		
		searchByTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					//do search redirect
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		searchByDate.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					//do search redirect
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		createAlbum.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Optional<String> result = Utilities.showDialog("Create", "Create Album", "Please enter a name for the new album.", "Name", "Name:");
				
				result.ifPresent(album -> {
					if(doesAlbumExist(album)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("Album already exists.");
					    alert.showAndWait();
					}
					else {
						addAlbum(album);
					}
				});
			}
		});
		
		renameAlbum.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Album album = albumsListView.getSelectionModel().getSelectedItem();
				if(album == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please select an album.");
				    alert.showAndWait();
				}
				else {
					Optional<String> result = Utilities.showDialog("Rename", "Rename Album", "Please enter a new name for the album.", "Name", "Name:");
					
					result.ifPresent(albumName -> {
						if(doesAlbumExist(albumName)) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.initOwner(mainStage);
						    alert.setTitle("Error");
						    alert.setHeaderText("Album already exists.");
						    alert.showAndWait();
						}
						else {
							renameAlbum(album.name, albumName);
						}
					});
				}
			}
		});
		
		deleteAlbum.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Album album = albumsListView.getSelectionModel().getSelectedItem();
				if(album == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please select an album.");
				    alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Are you sure you want to delete album \"" + album.name + "\"?");
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK) {
						deleteAlbum(album.name);
					}
				}
			}
		});
	}
	
	public boolean doesAlbumExist(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addAlbum(String name) {
		Album album = new Album(name);
		user.addAlbum(album);
		albums.add(album);
		albumsListView.refresh();
	}
	
	public void renameAlbum(String name, String newName) {
		user.renameAlbum(name, newName);
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				albums.get(i).name = newName;
			}
		}
		albumsListView.refresh();
	}
	
	public void deleteAlbum(String name) {
		user.deleteAlbum(name);
		for(int i = 0; i < albums.size(); i++) {
			if(name.equals(albums.get(i).name)) {
				albums.remove(i);
				return;
			}
		}
	}
}
