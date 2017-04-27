package assignment7;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;


public class Server extends Observable implements Observer {
	
	Controller control;
	int portNum;
	
	private int roomID;
	private int clientID;
	private List<ChatRoom> chatRooms;
	private List<ChatRoom> conversations;
	private List<ClientHandler> clients;
	private Map<String, Integer> usernames;
	private Map<String, Integer> chatroomNames;
	private ServerSocket server;
	
	Server(Controller control, int portNum) {
		this.control = control;
		this.portNum = portNum;
		roomID = 0;
		clientID = 0;
		chatRooms = new ArrayList<ChatRoom>();
		clients = new ArrayList<ClientHandler>();
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
				Socket client = server.accept();
				ClientHandler clientHandler = new ClientHandler(client, this);
				Platform.runLater(new Runnable() {                          
		            @Override
		            public void run() {
		            	control.serverText.appendText("Client " + clientID + " has connected\n");
		            }
		        });
				clients.add(clientHandler);
				clientID++;	
				addObserver(clientHandler);
				(new Thread(clientHandler)).start();
				//System.out.println("new client has connected in chat room " + roomID);
				//ChatRoom serverRoom = new ChatRoom(client, roomID, this);
				//rooms.add(serverRoom);
				//this.addObserver(serverRoom);
				//Thread thread = new Thread(serverRoom);
				//thread.start();
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

	@Override
	public void update(Observable o, Object arg) {
		//String clientMessage = "Client " + ((ChatRoom)o).getID() + ": " + (String)arg;
		//System.out.println(clientMessage);
		//notifyObservers(clientMessage);
	}

}
