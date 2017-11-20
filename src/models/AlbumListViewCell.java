package models;

import java.util.ArrayList;

import controllers.AlbumItemController;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class AlbumListViewCell<T extends ArrayList<Album>> extends ListCell<T> {
	
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item,  empty);
		if(item == null || empty) {
			setGraphic(null);
		}
		else {
			GridPane row = new GridPane();
			row.setPrefWidth(800);
			row.setPrefHeight(225);
			
			for(int i = 0; i < item.size(); i++) {
				AlbumItemController albumItem = new AlbumItemController();
				albumItem.setAlbum(item.get(i));
				row.add(albumItem.getItem(), i, 0);
			}
			
			this.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent click) {
					select(0, ((int)click.getSceneX() / 133), row);
				}
			});
			
			this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
				deselect(row);
			});
			
			setGraphic(row);
		}
	}
	
	public void select(int row, int col, GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();

	    for (Node node : childrens) {
	        if(grid.getRowIndex(node) == row && grid.getColumnIndex(node) == col) {
	            node.setStyle("-fx-background-color: #0295D1");
	        }
	        else {
	        	node.setStyle("-fx-background-color: white");
	        }
	    }
	}
	
	public void deselect(GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();

	    for (Node node : childrens) {
        	node.setStyle("-fx-background-color: white");
	    }
	}
}
