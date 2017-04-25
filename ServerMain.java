package assignment7;

import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Server.start();
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
}
