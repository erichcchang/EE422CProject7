/* EE422C Project 7 submission by
 * Ho-chang Chang
 * hc23882
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment7;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Chatroom extends Observable implements Observer {
	
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
		addObserver(clientHandler);
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
		addObserver(clientHandler);
		numClients++;
		refreshAll();
	}
	
	private void refresh(ClientHandler clientHandler) {
		String clientsList = "";
		String roomNum = ID < 10 ? "0" : "";
		roomNum += ID;
		String privateChar = isPrivate ? "1 " : "0 ";
		for (ClientHandler client: clients) {
			clientsList = clientsList + " " + client.username;
		}
		if (isPrivate) {
			Platform.runLater(new Runnable() {                          
	            @Override
	            public void run() {
	            	info.setText(clients.get(0).username + ", " + clients.get(1).username);
	            }
	        });
		}
		else {
			Platform.runLater(new Runnable() {                          
	            @Override
	            public void run() {
	            	String subTitle = clients.get(0).username;
	            	for (int i = 1; i < clients.size(); i++) {
	            		subTitle = subTitle + ", " + clients.get(i).username;
	            	}
	            	tab.setText(name + " (" + numClients + ")");
	            	info.setText(subTitle);
	            }
	        });
		}
		if (ID < 10) {
			clientHandler.out.println("MAKERM"  + roomNum + privateChar + numClients + " " + name + clientsList);
		}
		else {
			clientHandler.out.println("MAKERM" + roomNum + privateChar + numClients + " " + name + clientsList);
		}
		clientHandler.out.flush();
	}
	
	public void sendMessage(String message) {
		Platform.runLater(new Runnable() {                          
            @Override
            public void run() {
            	chatLog.appendText(message + "\n");
            }
        });
		String roomNum = ID < 10 ? "0" : "";
		roomNum += ID;
		setChanged();
		notifyObservers("CHATRM" + roomNum + message);
	}
	
	public void refreshAll() {
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

	@Override
	public void update(Observable o, Object arg) {
		refreshAll();
	}
	
}
