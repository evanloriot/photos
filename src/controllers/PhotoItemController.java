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
import models.SerialUtils;

/**
 * This class handles interactions with individual photo item.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class PhotoItemController {
	@FXML
	GridPane gridPane;
	@FXML
	ImageView photo;
	@FXML
	Text caption;
	
	/**
	 * Used to interact with an individual photo item.
	 */
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
			SerialUtils.errorAlert(e);
		}
	}
	
	/**
	 * Helper method used to set some attributes of a photo.
	 * @param photo photo to set instance to
	 */
	public void setPhoto(Photo photo) {
		this.photo.setImage(new Image(photo.location));
		caption.setText(photo.caption);
	}
	
	/**
	 * Helper method that returns the gridpane for this particular photo item.
	 * @return GridPane -- Gird that contains the photo cell with image and caption/date.
	 */
	public GridPane getItem() {
		return gridPane;
	}
	
	/**
	 * Helper method to deselect highlighted photo object
	 */
	public void deselect() {
		gridPane.setStyle("-fx-background-color: white");
	}
}
