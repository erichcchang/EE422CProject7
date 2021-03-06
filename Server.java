/* EE422C Project 7 submission by
 * Ho-chang Chang
 * hc23882
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;

public class Server {
	
	public Controller control;
	private int portNum;
	private ServerSocket server;
	public int clientID;
	public int roomID;
	
	public List<Chatroom> chatrooms;
	public List<ClientHandler> clients;
	public Map<String, Integer> fetchClientID;
	public Map<String, Integer> fetchChatroomID;

	Server(Controller control, int portNum) {
		this.control = control;
		this.portNum = portNum;
		roomID = 0;
		clientID = 0;
		chatrooms = new ArrayList<Chatroom>();
		clients = new ArrayList<ClientHandler>();
		fetchClientID = new HashMap<String, Integer>();
		fetchChatroomID = new HashMap<String, Integer>();
	}
	
	public void run() {
		control.serverText.appendText("Initializing...\n");
		try {
			server = new ServerSocket(portNum);
		} catch (IOException e) {
			control.serverText.appendText("Failed: Port " + portNum + " already bound\n");
			return;
		}
		control.serverText.appendText("Initialized!\n");
		while (true) {
			try {
				Socket clientSocket = server.accept();
				ClientHandler client = new ClientHandler(clientSocket, this);
				control.serverText.appendText("Client " + clientID + " has connected\n");
				clients.add(client);	
				fetchClientID.put(client.username, client.ID);
				clientID++;				
				(new Thread(client)).start();
			} catch (IOException e) {
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	control.serverText.appendText("Unable to establish connection with client\n");
	                }
	            });
			}		
		}
	}

}
