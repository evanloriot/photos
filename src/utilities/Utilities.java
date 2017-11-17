package utilities;

import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class Utilities {
	public static Optional<String> showDialog(String confirmText, String title, String header, String promptText, String labelText){
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		
		ButtonType addButtonType = new ButtonType(confirmText, ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField username = new TextField();
		username.setPromptText(promptText);
		
		grid.add(new Label(labelText), 0, 0);
		grid.add(username, 1, 0);
		
		Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
		addButton.setDisable(true);
		
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			addButton.setDisable(newValue.trim().isEmpty());
		});
		
		dialog.getDialogPane().setContent(grid);
		
		Platform.runLater(() -> username.requestFocus());
		
		dialog.setResultConverter(dialogButton -> {
			if(dialogButton == addButtonType) {
				return username.getText();
			}
			return null;
		});
		
		return dialog.showAndWait();
	}
}
