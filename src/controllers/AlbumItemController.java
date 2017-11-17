package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Album;

public class AlbumItemController {
	@FXML
	GridPane gridPane;
	@FXML
	Text data;
	@FXML
	ImageView thumb;
	
	public AlbumItemController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/_albumListItem.fxml"));
		loader.setController(this);
		try {
			loader.load();
		}
		catch(Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	public void setAlbum(Album album) {
		thumb.setImage(album.getThumb());
		data.setText(album.toString());
	}
	
	public GridPane getItem() {
		return gridPane;
	}
}
