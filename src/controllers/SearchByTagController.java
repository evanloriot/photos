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
import models.SerialUtils;
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
	ListView<ArrayList<Photo>> photosListView;
	@FXML
	Button createAlbum;
	
	
	ObservableList<ArrayList<Photo>> photos;
	User user;
	
	String searchParameters;
	
	Photo selected = null;
	
	public void start(Stage mainStage) {
		search.setDisable(true);
		
		parameters.textProperty().addListener((observable, oldValue, newValue) -> {
			search.setDisable(newValue.trim().isEmpty());
		});
		
		if(searchParameters != null) {
			photos = FXCollections.observableArrayList(getPhotosFromSearch(searchParameters));
			photosListView.setItems(photos);
			photosListView.setCellFactory(x -> new PhotoListViewCell<>());
			
			parameters.setText(searchParameters);
		}
		
		photosListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Photo photo = new Photo("");
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
									photoController.album = photo.album;
									photoController.photoObj = photo;
									photoController.backLocation = "searchByTag";
									photoController.backSearchParameters = searchParameters;
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
								photoController.album = photo.album;
								photoController.photoObj = photo;
								photoController.backLocation = "searchByTag";
								photoController.backSearchParameters = searchParameters;
								photoController.start(mainStage);
								
								Scene scene = new Scene(root);
								mainStage.setScene(scene);
							}
						}
						
						
					}
					catch(Exception e) {
						System.out.println("error");
						e.printStackTrace();
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
						Optional<String> result = Utilities.showDialog("Create","Create Album","Enter a name for the new album.","Name","Name:");
						result.ifPresent(albumName -> {
							Album album = new Album(albumName);
							ArrayList<Photo> photosArray = new ArrayList<Photo>();
							for(int i = 0; i < photos.size(); i++) {
								for(int j = 0; j < photos.get(i).size(); j++) {
									photosArray.add(photos.get(i).get(j));
								}
							}
							try {
							album.photos = photosArray;
							album.numPhotos = photosArray.size();
							user.addAlbum(album);
							SerialUtils.writeUserToFile(user);
							} catch (Exception e){
								e.printStackTrace();
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
		
		search.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					if(areParametersValid(parameters.getText())) {
						photos = FXCollections.observableArrayList(getPhotosFromSearch(parameters.getText()));
						photosListView.setItems(photos);
						photosListView.setCellFactory(x -> new PhotoListViewCell<>());
						
						searchParameters = parameters.getText();
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
						photosListView.setCellFactory(x -> new PhotoListViewCell<>());
						
						searchParameters = parameters.getText();
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
	
	public ArrayList<ArrayList<Photo>> getPhotosFromSearch(String search){
		ArrayList<ArrayList<Photo>> output = new ArrayList<ArrayList<Photo>>();
		ArrayList<Photo> row = new ArrayList<Photo>();
		int col;
		int count = 0;
		for(int i = 0; i < user.albums.size(); i++) {
			for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
				for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++) {
					if(search.contains(user.albums.get(i).photos.get(j).tags.get(k))) {
						col = count % 6;
						if(col == 0) {
							row = new ArrayList<Photo>();
							output.add(row);
						}
						row.add(user.albums.get(i).photos.get(j));
						count++;
					}
				}
			}
		}
		return output;
	}
}
