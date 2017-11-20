package application;

import java.io.IOException;

import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Photos class is the driver of our application.
 * @author Evan Loriot
 * @author Joseph Klaszky
 */
public class Photos extends Application{
	/**
	 * Starts the app and loads the first login view.
	 * @exception IOException -- Can be raised any time there is an issue with the users' files.
	 * @see IOException
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/login.fxml"));
			
			GridPane root = (GridPane) loader.load();
			
			LoginController loginController = loader.getController(); 
			loginController.start(primaryStage);
			
			Scene scene = new Scene(root, 800, 600);
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		}
		catch(Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
