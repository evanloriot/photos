package models;

import controllers.PhotoItemController;
import javafx.scene.control.ListCell;

public class PhotoListViewCell extends ListCell<Photo> {
	@Override
	public void updateItem(Photo photo, boolean empty) {
		super.updateItem(photo,  empty);
		if(photo != null) {
			PhotoItemController item = new PhotoItemController();
			item.setPhoto(photo);
			setGraphic(item.getItem());
		}
		else {
			setGraphic(null);
		}
	}
}
