package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.SerialUtils;
import models.User;
import utilities.Utilities;

/**
 * The PhotoController class handles all the interactions the user
 * has with photos that are already in an album.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class PhotoController {
	/**
	 * View title
	 */
	@FXML
	Label title;
	/**
	 * Button to log user out of system.
	 */
	@FXML
	Button logout;
	/**
	 * Button to return to previous view.
	 */
	@FXML
	Button back;
	/**
	 * Image of photo
	 */
	@FXML
	ImageView photo;
	/**
	 * Caption text. Non-editable.
	 */
	@FXML
	TextField caption;
	/**
	 * Date of image last modified (aka creation date)
	 */
	@FXML
	Text date;
	/**
	 * Button to prompt caption edit window.
	 */
	@FXML
	Button editCaption;
	/**
	 * Field to type in a tag to be added.
	 */
	@FXML
	TextField tag;
	/**
	 * Button to add tag to photo typed into 'tag' field.
	 */
	@FXML
	Button addTag;
	/**
	 * Button to delete selected tag from photo.
	 */
	@FXML
	Button deleteTag;
	/**
	 * Listing of all tags associated with photo.
	 */
	@FXML
	ListView<String> tags;
	
	/**
	 * Holds list of tags associated with a photo
	 * and held in an observable list so that it can be
	 * displayed to the user.
	 */
	ObservableList<String> obsTags;
	
	/**
	 * The user associated with the album that contains this photo.
	 */
	User user;
	
	/**
	 * The album containing the photo that is currently being interacted with.
	 */
	Album album;
	
	/**
	 * The photo object that the user is currently interacting with.
	 */
	Photo photoObj;
	
	/**
	 * Tells the user of the context in which they are interacting with this photo
	 * and where to return to after they are finished.
	 */
	String backLocation;
	
	/**
	 * If the user is in this context from a tag search this param contains the 
	 * content of that search
	 */
	String backSearchParameters;
	
	/**
	 * If the user is in this context from a date search this contains the start date.
	 */
	Date backStartDate;
	
	/**
	 * If the user is in this context from a date search this contains the end date.
	 */
	Date backEndDate;
	
	/**
	 * Called when the a user enters the Photo view. Handles interactions with the user when editing a photo.
	 * @param mainStage current application stage
	 */
	public void start(Stage mainStage) {
		obsTags = FXCollections.observableArrayList(photoObj.tags);
		tags.setItems(obsTags);
		
		title.setText(title.getText() + " " + user.username);
		
		photo.setImage(new Image(photoObj.location));
		
		caption.setText(photoObj.caption);
		
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		date.setText(ft.format(photoObj.captureDate));
		
		addTag.setDisable(true);
		
		tag.textProperty().addListener((observable, oldValue, newValue) -> {
			addTag.setDisable(newValue.trim().isEmpty());
		});

		
		editCaption.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Optional<String> result = Utilities.showDialog("Save","Edit Caption","Enter a new caption.","Caption","Caption:");
					result.ifPresent(captionText -> {
						user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).caption = captionText;
						caption.setText(captionText);
						try {
							SerialUtils.writeUserToFile(user);
						}
						catch(IOException e){
							SerialUtils.errorAlert(e);
						}
						
					});
				}
				catch(Exception e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		addTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					if(isTagFormatted(tag.getText())){
						for(int i = 0; i < user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.size(); i++) {
							if(user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.get(0).equals(tag.getText())){
								Alert alert = new Alert(AlertType.ERROR);
								alert.initOwner(mainStage);
							    alert.setTitle("Error");
							    alert.setHeaderText("Tag already exists.");
							    alert.showAndWait();
							    tag.setText("");
								return;
							}
						}
						user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.add(tag.getText());
						SerialUtils.writeUserToFile(user);
						obsTags.add(tag.getText());
						tag.setText("");
					}
				}
				catch(IOException e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		tag.setOnKeyPressed((event) -> {
			if(event.getCode() == KeyCode.ENTER && !tag.getText().isEmpty()) {
				try {
					if(isTagFormatted(tag.getText())){
						for(int i = 0; i < user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.size(); i++) {
							if(user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.get(0).equals(tag.getText())){
								Alert alert = new Alert(AlertType.ERROR);
								alert.initOwner(mainStage);
							    alert.setTitle("Error");
							    alert.setHeaderText("Tag already exists.");
							    alert.showAndWait();
							    tag.setText("");
								return;
							}
						}
						try{
							user.getAlbum(album.name).getPhoto(photoObj.location, photoObj.instance).tags.add(tag.getText());
							obsTags.add(tag.getText());
							tag.setText("");
							SerialUtils.writeUserToFile(user);
						} catch(IOException e){
							SerialUtils.errorAlert(e);
						}
					}
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
				catch(IOException e) {
					SerialUtils.errorAlert(e);
				}
			}
		});
		
		deleteTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				String tag = tags.getSelectionModel().getSelectedItem();
				if(tag == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainStage);
				    alert.setTitle("Error");
				    alert.setHeaderText("Please select a tag.");
				    alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Are you sure you want to delete this tag: \"" + tag + "\"?");
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK) {
						try{
							photoObj.deleteTag(tag);
							SerialUtils.writeUserToFile(user);
						} catch(Exception e){
							SerialUtils.errorAlert(e);
						}
						for(int i = 0; i < obsTags.size(); i++) {
							if(tag.equals(obsTags.get(i))) {
								obsTags.remove(i);
								break;
							}
						}
					}
				}
			}
		});
		
		back.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					Parent root;
					if(backLocation.equals("album")) {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/album.fxml"));
						root = (Parent) loader.load();
						
						AlbumController albumController = loader.getController();
						albumController.user = user;
						albumController.album = album;
						albumController.start(mainStage);
					}
					else if(backLocation.equals("searchByDate")) {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/searchByDate.fxml"));
						root = (Parent) loader.load();
						
						SearchByDateController searchByDateController = loader.getController();
						searchByDateController.user = user;
						searchByDateController.searchStartDate = backStartDate;
						searchByDateController.searchEndDate = backEndDate;
						searchByDateController.start(mainStage);
					}
					else {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/searchByTag.fxml"));
						root = (Parent) loader.load();
						
						SearchByTagController searchByTagController = loader.getController();
						searchByTagController.user = user;
						searchByTagController.searchParameters = backSearchParameters;
						searchByTagController.start(mainStage);
					}
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
	 * Check to make sure the tag is of the format 'string=string'
	 * @param tag Takes in a string that will be used as a tag
	 * @return boolean -- True if the tag is formatted properly, false otherwise
	 */
	public boolean isTagFormatted(String tag) {
		return tag.matches("[a-zA-Z0-9]*[=][a-zA-Z0-9]*");
	}
}
