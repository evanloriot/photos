package controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Album;
import models.Photo;
import models.PhotoListViewCell;
import models.User;
import utilities.Utilities;

public class SearchByDateController {
	@FXML
	Button logout;
	@FXML
	Button back;
	@FXML
	DatePicker startDate;
	@FXML
	DatePicker endDate;
	@FXML
	Button search;
	@FXML 
	ListView<Photo> photosListView;
	@FXML
	Button createAlbum;
	
	
	ObservableList<Photo> photos;
	User user;
	
	public void start(Stage mainStage) {
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
						photoController.album = photo.album;
						photoController.photoObj = photo;
						photoController.backLocation = "searchByDate";
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
					LocalDate s = startDate.getValue();
					LocalDate e = endDate.getValue();
					Instant st = Instant.from(s.atStartOfDay(ZoneId.systemDefault()));
					Instant en = Instant.from(e.atStartOfDay(ZoneId.systemDefault()));
					Date start = Date.from(st);
					Date end = Date.from(en);
					
					if(start != null && end != null) {
						photos = FXCollections.observableArrayList(getPhotosFromSearch(start, end));
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
	
	public ArrayList<Photo> getPhotosFromSearch(Date start, Date end){
		ArrayList<Photo> output = new ArrayList<Photo>();
		for(int i = 0; i < user.albums.size(); i++) {
			for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
				if(!start.after(user.albums.get(i).photos.get(j).captureDate) && !end.before(user.albums.get(i).photos.get(j).captureDate)) {
					output.add(user.albums.get(i).photos.get(j));
				}
			}
		}
		return output;
	}
}
