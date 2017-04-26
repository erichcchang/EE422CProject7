package assignment7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientMain extends Application {

	Stage clientStage;
	Rectangle2D screenBounds;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage clientStage = primaryStage;
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		readControllerFXML();
		(new Client()).start();

	}
	
	private void readControllerFXML() {
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(ServerMain.class.getResource("ClientController.fxml"));
			AnchorPane control = (AnchorPane)loader.load();
			clientStage.setScene(new Scene(control));
			clientStage.show();
			clientStage.setTitle("Client");
			clientStage.setX(screenBounds.getMinX() + 50);
			clientStage.setY(screenBounds.getMinY() + 50);
		} catch (Exception e) {
			System.out.println("Cannot find FXML file");
			System.out.println(ServerMain.class.getResource("ClientController.fxml"));
		}
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
}