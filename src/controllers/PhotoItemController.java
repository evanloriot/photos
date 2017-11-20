package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Photo;

public class PhotoItemController {
	@FXML
	GridPane gridPane;
	@FXML
	ImageView photo;
	@FXML
	Text caption;
	
	public PhotoItemController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/_photoListItem.fxml"));
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
	
	public void setPhoto(Photo photo) {
		this.photo.setImage(new Image(photo.location));
		caption.setText(photo.caption);
	}
	
	public GridPane getItem() {
		return gridPane;
	}
	
	public void deselect() {
		gridPane.setStyle("-fx-background-color: white");
	}
}
