package assignment7;

import javafx.fxml.*;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	int portNum = 5000;
	Socket[] clients;
	
	@FXML
	TextArea chatLog;
	
	static void start() throws Exception {
		ServerSocket serverSocket = new ServerSocket();
		while(true){
			Socket request = serverSocket.accept();
			InputStream in = request.getInputStream();
			OutputStream out = request.getOutputStream();
			(new Thread()).start();
			
		}
		// observables
	}
	

}
