package models;

import java.util.ArrayList;

import controllers.PhotoItemController;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * This class manages each individual photo cell item that is displayed in 
 * the album view.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class PhotoListViewCell <T extends ArrayList<Photo>> extends ListCell<T> {
	/**
	 * This handles and updates that need to be done to a photo cell as a result of user interactions
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
				PhotoItemController photoItem = new PhotoItemController();
				photoItem.setPhoto(item.get(i));
				row.add(photoItem.getItem(), i, 0);
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
	 * Method that handles the user selecting a specific photo cell.
	 * @param row row of grid
	 * @param col column of grid
	 * @param grid grid of nodes
	 */
	public void select(int row, int col, GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();
		
		for(Node node : childrens) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				node.setStyle("-fx-background-color: #0295D1");
			}
			else {
				node.setStyle("-fx-background-color: white");
			}
		}
	}
	
	/**
	 * Helper method to deselect any selected photo cells
	 * @param grid grid of nodes
	 */
	public void deselect(GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();
		
		for(Node node : childrens) {
			node.setStyle("-fx-background-color: white");
		}
	}
}
