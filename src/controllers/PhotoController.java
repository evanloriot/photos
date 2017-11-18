package controllers;

import java.text.SimpleDateFormat;
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
import models.User;
import utilities.Utilities;

public class PhotoController {
	@FXML
	Label title;
	@FXML
	Button logout;
	@FXML
	Button back;
	@FXML
	ImageView photo;
	@FXML
	TextField caption;
	@FXML
	Text date;
	@FXML
	Button editCaption;
	@FXML
	TextField tag;
	@FXML
	Button addTag;
	@FXML
	Button deleteTag;
	@FXML
	ListView<String> tags;
	
	ObservableList<String> obsTags;
	User user;
	Album album;
	Photo photoObj;
	String backLocation;
	
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
						photoObj.caption = captionText;
						caption.setText(captionText);
					});
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		addTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					if(isTagFormatted(tag.getText())){
						photoObj.tags.add(tag.getText());
						obsTags.add(tag.getText());
						tag.setText("");
					}
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
		
		tag.setOnKeyPressed((event) -> {
			if(event.getCode() == KeyCode.ENTER && !tag.getText().isEmpty()) {
				try {
					if(isTagFormatted(tag.getText())){
						photoObj.tags.add(tag.getText());
						obsTags.add(tag.getText());
						tag.setText("");
					}
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
						photoObj.deleteTag(tag);
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
						
						//need to remove
						Scene scene = new Scene(root);
						mainStage.setScene(scene);
					}
					else if(backLocation.equals("searchByDate")) {
						//back to date
					}
					else {
						//back to tags
					}
					
//					Scene scene = new Scene(root);
//					mainStage.setScene(scene);
				}
				catch(Exception e) {
					System.out.println("error");
					e.printStackTrace();
				}
			}
		});
	}
	
	public boolean isTagFormatted(String tag) {
		return tag.matches("[a-zA-Z0-9]*[=][a-zA-Z0-9]*");
	}
}