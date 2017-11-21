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

/**
 * This class handles each cell in an a list of albums 
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class AlbumListViewCell<T extends ArrayList<Album>> extends ListCell<T> {
	
	/**
	 * This handles and updates that need to be done to a album cell as a result of user interactions
	 */
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
	
	/**
	 * Method that handles the user selecting a specific album cell.
	 * @param row row of grid
	 * @param col column of grid
	 * @param grid grid of nodes
	 */
	public void select(int row, int col, GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
	            node.setStyle("-fx-background-color: #0295D1");
	        }
	        else {
	        	node.setStyle("-fx-background-color: white");
	        }
	    }
	}
	
	/**
	 * Helper method to deselect any selected album cell
	 * @param grid grid of nodes
	 */
	public void deselect(GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();

	    for (Node node : childrens) {
        	node.setStyle("-fx-background-color: white");
	    }
	}
}
