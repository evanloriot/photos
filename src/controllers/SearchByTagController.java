package controllers;

import java.util.ArrayList;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.Photo;
import models.PhotoListViewCell;
import models.User;
import utilities.Utilities;

public class SearchByTagController {
	@FXML
	Button logout;
	@FXML
	Button back;
	@FXML
	TextField parameters;
	@FXML
	Button search;
	@FXML
	Button help;
	@FXML 
	ListView<Photo> photosListView;
	@FXML
	Button createAlbum;
	
	
	ObservableList<Photo> photos;
	User user;
	
	public void start(Stage mainStage) {
		search.setDisable(true);
		
		parameters.textProperty().addListener((observable, oldValue, newValue) -> {
			search.setDisable(newValue.trim().isEmpty());
		});
		
		createAlbum.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					if(photos.size() == 0) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainStage);
					    alert.setTitle("Error");
					    alert.setHeaderText("No photos to make an album.");
					    alert.showAndWait();
					}
					else {
						Optional<String> result = Utilities.showDialog("Create","Create Album","Enter a name for ne album.","Name","Name:");
						result.ifPresent(albumName -> {
							Album album = new Album(albumName);
							ArrayList<Photo> photosArray = new ArrayList<Photo>();
							for(int i = 0; i < photos.size(); i++) {
								photosArray.add(photos.get(i));
							}
							album.photos = photosArray;
							album.numPhotos = photosArray.size();
							user.addAlbum(album);
						});
					}
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		search.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					if(areParametersValid(parameters.getText())) {
						photos = FXCollections.observableArrayList(getPhotosFromSearch(parameters.getText()));
						photosListView.setItems(photos);
						photosListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
							@Override
							public ListCell<Photo> call(ListView<Photo> listView){
								return new PhotoListViewCell();
							}
						});
					}
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		parameters.setOnKeyPressed((event) -> {
			if(event.getCode() == KeyCode.ENTER && !parameters.getText().isEmpty()) {
				try {
					if(areParametersValid(parameters.getText())) {
						photos = FXCollections.observableArrayList(getPhotosFromSearch(parameters.getText()));
						photosListView.setItems(photos);
						photosListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
							@Override
							public ListCell<Photo> call(ListView<Photo> listView){
								return new PhotoListViewCell();
							}
						});
					}
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		help.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.initOwner(mainStage);
				    alert.setTitle("Herlp");
				    alert.setHeaderText("Correct format of tags is as follows: \ntag=value,tag=value,tag=value");
				    alert.showAndWait();
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
	}
	
	public boolean areParametersValid(String tags) {
		return tags.matches("([a-zA-Z0-9]*[=][a-zA-Z0-9]*[,]?)*");
	}
	
	public ArrayList<Photo> getPhotosFromSearch(String search){
		ArrayList<Photo> output = new ArrayList<Photo>();
		for(int i = 0; i < user.albums.size(); i++) {
			for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
				for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++) {
					if(search.contains(user.albums.get(i).photos.get(j).tags.get(k))) {
						output.add(user.albums.get(i).photos.get(j));
					}
				}
			}
		}
		return output;
	}
}
