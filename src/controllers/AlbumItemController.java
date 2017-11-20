package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
			gridPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent click) {
					gridPane.setStyle("-fx-background-color: #0295D1");
				}
			});
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
	
	public void deselect() {
		gridPane.setStyle("-fx-background-color: white");
	}
}
