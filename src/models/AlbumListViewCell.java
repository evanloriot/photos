package models;

import controllers.AlbumItemController;
import javafx.scene.control.ListCell;

public class AlbumListViewCell extends ListCell<Album> {
	@Override
	public void updateItem(Album album, boolean empty) {
		super.updateItem(album,  empty);
		if(album != null) {
			AlbumItemController item = new AlbumItemController();
			item.setAlbum(album);
			setGraphic(item.getItem());
		}
		else {
			setGraphic(null);
		}
	}
}
