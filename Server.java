package assignment7;

import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	int portNum = 5000;
	ServerSocket server;
	Socket[] clients;
	
	@FXML
	TextArea chatLog;
	
	@FXML
	TextArea textPrompt;
	
	@FXML
	Button sendButton;
	
	void start() {
		try {
			server = new ServerSocket(portNum);
		} catch (IOException e) {
			System.out.println("Port " + portNum + " already bound");
			return;
		}
		while (true) {
			Socket request;
			try {
				request = server.accept();
			} catch (IOException e) {
				System.out.println("Cannot accept client");
				return;
			}
			Thread thread = new Thread(new Runnable() {
				
					@Override
					public void run() {
						try {
							DataInputStream in = new DataInputStream(request.getInputStream());
							DataOutputStream out = new DataOutputStream(request.getOutputStream());
							while (true) {
								
							}
							
						} catch (IOException e) {
							System.out.println("Socket Unavailable");
							return;
						}		
					}
					
				});
			thread.start();
			
		}
		// observables
	}
	

}
