package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
}
