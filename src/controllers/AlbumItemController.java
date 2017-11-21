package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Album;
import models.SerialUtils;

/**
 * This class handles interactions with individual album items.
 * @author Evan Loriot
 * @author Joseph Klaszky
 *
 */
public class AlbumItemController {
	/**
	 * GridPane for one album cell
	 */
	@FXML
	GridPane gridPane;
	/**
	 * Album name
	 */
	@FXML
	Text data;
	/**
	 * Album image - first picture thumb if numPhotos != 0, else default album thumb
	 */
	@FXML
	ImageView thumb;
	
	/**
	 * Class handling the creation of a single cell (1/6) from albums display.
	 * @author Evan Loriot
	 * @author Joseph Klaszky
	 */
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
			SerialUtils.errorAlert(e);
		}
	}

	/**
	 * Sets some attributes for a passed album.
	 * @param album album to set instance to
	 */
	public void setAlbum(Album album) {
		thumb.setImage(album.getThumb());
		data.setText(album.toString());
	}
	
	/**
	 * Returns an album item's gridpane
	 * @return gridPane for this album item
	 */
	public GridPane getItem() {
		return gridPane;
	}
	
	/**
	 * Helper method to deselect an album item
	 */
	public void deselect() {
		gridPane.setStyle("-fx-background-color: white");
	}
}
