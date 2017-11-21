package controllers;

import java.io.IOException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.PhotoListViewCell;
import models.SerialUtils;
import models.User;
import utilities.Utilities;

/**
 * This class handles all interactions with the user while in the search by date view.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
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
	ListView<ArrayList<Photo>> photosListView;
	@FXML
	Button createAlbum;
	
	/**
	 * List used to keep track of all the photos that are found by the search and displayed to
	 * the user.
	 */
	ObservableList<ArrayList<Photo>> photos;
	
	/**
	 * Keeps track of the current user.
	 */
	User user;
	
	/**
	 * Beginning date of the search.
	 */
	Date searchStartDate;
	/**
	 * Ending date of the search.
	 */
	Date searchEndDate;
	
	/**
	 * Keeps track of the currently selected photo.
	 */
	Photo selected = null;
	
	/**
	 * Called when the user enters the searchByDate view.
	 * @param mainStage current application stage
	 */
	public void start(Stage mainStage) {
		if(searchStartDate != null && searchEndDate != null) {
			photos = FXCollections.observableArrayList(getPhotosFromSearch(searchStartDate, searchEndDate));
			photosListView.setItems(photos);
			photosListView.setCellFactory(x -> new PhotoListViewCell<>());

			startDate.setValue(searchStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			endDate.setValue(searchEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		
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
									photoController.album = photo.album;
									photoController.photoObj = photo;
									photoController.backLocation = "searchByDate";
									photoController.backStartDate = searchStartDate;
									photoController.backEndDate = searchEndDate;
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
								photoController.backLocation = "searchByDate";
								photoController.backStartDate = searchStartDate;
								photoController.backEndDate = searchEndDate;
								photoController.start(mainStage);
								
								Scene scene = new Scene(root);
								mainStage.setScene(scene);
							}
						}
						
					}
					catch(Exception e) {
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
							try{
								album.photos = photosArray;
								album.numPhotos = photosArray.size();
								user.addAlbum(album);
							} catch(Exception e){
								SerialUtils.errorAlert(e);
							}
						});
					}
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
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
						photosListView.setCellFactory(x -> new PhotoListViewCell<>());
						
						searchStartDate = start;
						searchEndDate = end;
					}
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
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/albums.fxml"));
					Parent root = (Parent) loader.load();
					
					AlbumsController albumsController = loader.getController();
					albumsController.user = user;
					albumsController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
				}
				catch(Exception e) {
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
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
	}
	
	/**
	 * Gets a list of photos that fit the current search parameters.
	 * @param start Beginning date of the user's search.
	 * @param end Ending date of the user's search.
	 * @return A list of photo object that have dates within the parameters.
	 */
	public ArrayList<ArrayList<Photo>> getPhotosFromSearch(Date start, Date end){
		ArrayList<ArrayList<Photo>> output = new ArrayList<ArrayList<Photo>>();
		ArrayList<Photo> row = new ArrayList<Photo>();
		int col;
		int count = 0;
		for(int i = 0; i < user.albums.size(); i++) {
			for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
				if(!start.after(user.albums.get(i).photos.get(j).captureDate) && !end.before(user.albums.get(i).photos.get(j).captureDate)) {
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
		return output;
	}
}
