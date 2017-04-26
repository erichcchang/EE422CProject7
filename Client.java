package assignment7;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Client {

	int portNum = 5000;
	Socket client;
	Socket[] clients;
	
	@FXML
	TextArea chatLog;
	
	@FXML
	TextArea textPrompt;
	
	@FXML
	Button sendButton;
	
	void start() {
		try {
			client = new Socket("localhost", portNum);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			return;
		} catch (IOException e) {
			System.out.println("Port unavailable");
		}
	}
	
	
}
