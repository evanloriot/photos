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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Album;
import models.AlbumListViewCell;
import models.SerialUtils;
import models.User;
import utilities.Utilities;

public class AlbumsController {
	@FXML
	Label title;
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
	ListView<ArrayList<Album>> albumsListView;
	
	ObservableList<ArrayList<Album>> albums;

	Album selected = null;
	
	User user;
	
	public void start(Stage mainStage) {
		title.setText(title.getText() + user.username);
		
		albums = FXCollections.observableArrayList(getAlbums());
		albumsListView.setItems(albums);
		albumsListView.setCellFactory(x -> new AlbumListViewCell<>());
		
		albumsListView.refresh();
		
		albumsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				Album album = new Album("");
				if(click.getClickCount() == 2) {
					try {
						if(albumsListView.getSelectionModel().getSelectedItem() != null && ((int)click.getSceneX() / 133) < albumsListView.getSelectionModel().getSelectedItem().size()) {
							if(albums.size() == 1) {
								if(click.getSceneY() < 325) {
									album = albumsListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
									
									FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/album.fxml"));
									Parent root = (Parent) loader.load();
									
									AlbumController albumController = loader.getController();
									albumController.user = user;
									albumController.album = album;
									albumController.start(mainStage);
									
									Scene scene = new Scene(root);
									mainStage.setScene(scene);
								}
							}
							else {
								album = albumsListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
								
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/album.fxml"));
								Parent root = (Parent) loader.load();
								
								AlbumController albumController = loader.getController();
								albumController.user = user;
								albumController.album = album;
								albumController.start(mainStage);
								
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
				else if(click.getClickCount() == 1) {
					if(albumsListView.getSelectionModel().getSelectedItem() != null && ((int)click.getSceneX() / 133) < albumsListView.getSelectionModel().getSelectedItem().size()) {
						if(albums.size() == 1) {
							if(click.getSceneY() < 325) {
								selected = albumsListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
							}
						}
						else{
							selected = albumsListView.getSelectionModel().getSelectedItem().get((int)click.getSceneX() / 133);
						}
					}
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
		
		searchByTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/searchByTag.fxml"));
					Parent root = (Parent) loader.load();
					
					SearchByTagController searchByTagController = loader.getController();
					searchByTagController.user = user;
					searchByTagController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
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
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/searchByDate.fxml"));
					Parent root = (Parent) loader.load();
					
					SearchByDateController searchByDateController = loader.getController();
					searchByDateController.user = user;
					searchByDateController.start(mainStage);
					
					Scene scene = new Scene(root);
					mainStage.setScene(scene);
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
				Album album = selected;
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
				Album album = selected;
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
			for(int j = 0; j < albums.get(i).size(); j++) {
				if(name.equals(albums.get(i).get(j).name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void addAlbum(String name) {
		try{
			Album album = new Album(name);
			user.addAlbum(album);
			albums.get(albums.size() - 1).add(album);
			resizeAlbums();
			albumsListView.refresh();
			SerialUtils.writeUserToFile(user);	
		} catch(Exception e){
			System.out.println("Error adding new album");
			e.printStackTrace();
		}
		
	}
	
	public void renameAlbum(String name, String newName) {
		user.renameAlbum(name, newName);
		try {
			SerialUtils.writeUserToFile(user);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < user.albums.size(); i++) {
			if(name.equals(user.albums.get(i).name)) {
				user.albums.get(i).name = newName;
			}
		}
		albumsListView.refresh();
	}
	
	public void deleteAlbum(String name) {
		user.deleteAlbum(name);
		for(int i = 0; i < albums.size(); i++) {
			for(int j = 0; j < albums.get(i).size(); j++) {
				if(name.equals(albums.get(i).get(j).name)) {
					try{
						albums.get(i).remove(j);
						resizeAlbums();
						albumsListView.refresh();
						SerialUtils.writeUserToFile(user);			
					} catch(Exception e){
						System.out.println("Error adding new album");
						e.printStackTrace();
					}
					return;
				}
			}
		}
	}
	
	public ArrayList<ArrayList<Album>> getAlbums(){
		ArrayList<ArrayList<Album>> output = new ArrayList<ArrayList<Album>>();
		ArrayList<Album> row = new ArrayList<Album>();
		int col;
		for(int i = 0; i < user.albums.size(); i++) {
			col = i % 6;
			if(col == 0) {
				row = new ArrayList<Album>();
				output.add(row);
			}
			row.add(user.albums.get(i));
		}
		return output;
	}
	
	public void resizeAlbums() {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).size() > 6) {
				if(i == albums.size() - 1) {
					albums.add(new ArrayList<Album>());
				}
				while(albums.get(i).size() > 6) {
					albums.get(i+1).add(0, albums.get(i).get(albums.get(i).size() - 1));
					albums.get(i).remove(albums.get(i).size() - 1);
				}
			}
			else if(i != albums.size() - 1 && albums.get(i).size() < 6) {
				albums.get(i).add(albums.get(i+1).get(0));
				albums.get(i+1).remove(0);
				if(albums.get(i+1).size() == 0) {
					albums.remove(i+1);
				}
			}
		}
	}
}
