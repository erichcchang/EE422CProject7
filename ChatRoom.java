package assignment7;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Chatroom {
	
	public boolean isPrivate;
	public int numClients;
	final public int ID;
	public String name;
	private List<ClientHandler> clients;
	
	public Tab tab;
	public TextArea chatLog;
	public Label info;
	
	Chatroom(ClientHandler clientHandler, boolean isPrivate, String name, Server server) {
		clients = new ArrayList<ClientHandler>();
		clients.add(clientHandler);
		this.isPrivate = isPrivate;
		numClients = 1;
		ID = server.roomID;
		this.name = name;
		Platform.runLater(new Runnable() {                          
            @Override
            public void run() {
            	createRoom(name, clientHandler.username);
            	tab.setText(name + " (" + numClients + ")");
            	info.setText(clientHandler.username);
            	server.control.tabs.getTabs().add(tab);
        		server.control.tabs.getSelectionModel().select(tab);
            }
        });
	}
	
	public void addClient(ClientHandler clientHandler) {
		clients.add(clientHandler);
		numClients++;
		refreshAll();
		Platform.runLater(new Runnable() {                          
            @Override
            public void run() {
            	tab.setText(name + " (" + numClients + ")");
            	info.setText(info.getText() + ", " + clientHandler.username);
            }
        });
	}
	
	private void refresh(ClientHandler clientHandler) {
		String clientsList = "";
		String roomNum = ID < 10 ? "0" : "";
		roomNum += ID;
		String privateChar = isPrivate ? "1 " : "0 ";
		for (ClientHandler client: clients) {
			clientsList = clientsList + " " + client.username;
		}
		if (ID < 10) {
			clientHandler.out.println("MAKERM"  + roomNum + privateChar + numClients + " " + name + clientsList);
		}
		else {
			clientHandler.out.println("MAKERM" + roomNum + privateChar + numClients + " " + name + clientsList);
		}
		clientHandler.out.flush();
	}
	
	public void sendMessage(String message) { // observer/able candidate
		Platform.runLater(new Runnable() {                          
            @Override
            public void run() {
            	chatLog.appendText(message + "\n");
            }
        });
		String roomNum = ID < 10 ? "0" : "";
		roomNum += ID;
		for (ClientHandler client: clients) {
			client.out.println("CHATRM" + roomNum + message);
			client.out.flush();
		}
	}
	
	public void refreshAll() { // observer/able candidate
		for (ClientHandler client: clients) {
			refresh(client);
		}
	}
	
	private void createRoom(String title, String information) {
		BorderPane room = new BorderPane();
		tab = new Tab(title, room);
		room.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		BorderPane centerBorder = new BorderPane();
		room.setCenter(centerBorder);
		centerBorder.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
		chatLog = new TextArea();
		info = new Label(information);
		centerBorder.setTop(info);
		BorderPane.setMargin(info, new Insets(0.0, 0.0, 10.0, 0.0));
		BorderPane.setAlignment(info, Pos.CENTER);
		centerBorder.setCenter(chatLog);
		chatLog.setEditable(false);
	}
	
}
