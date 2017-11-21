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
 * @param <T>
 */
public class PhotoListViewCell <T extends ArrayList<Photo>> extends ListCell<T> {
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
	 * @param row
	 * @param col
	 * @param grid
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
	 * Helper method
	 * @param grid
	 */
	public void deselect(GridPane grid) {
		ObservableList<Node> childrens = grid.getChildren();
		
		for(Node node : childrens) {
			node.setStyle("-fx-background-color: white");
		}
	}
}
