package assignment7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ServerMain extends Application {

	Stage serverStage;
	Rectangle2D screenBounds;
	
	@Override
	public void start(Stage primaryStage) {
		serverStage = primaryStage;
		screenBounds = Screen.getPrimary().getVisualBounds();
		readControllerFXML();
		(new Server()).start();
	}
	
	private void readControllerFXML() {
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(ServerMain.class.getResource("ServerController.fxml"));
			AnchorPane controller = (AnchorPane)loader.load();
			serverStage.setScene(new Scene(controller));
			serverStage.show();
			serverStage.setTitle("Server");
			serverStage.setX(screenBounds.getMinX() + 50);
			serverStage.setY(screenBounds.getMinY() + 50);
		} catch (Exception e) {
			System.out.println("Cannot find FXML file");
			System.out.println(ServerMain.class.getResource("ServerController.fxml"));
		}
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
}
