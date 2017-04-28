package assignment7;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Chatroom extends Observable implements Observer {
	
	public boolean isPrivate;
	public int numClients;
	final public int ID;
	public String name;
	private List<ClientHandler> clients;
	
	Chatroom(ClientHandler clientHandler, boolean isPrivate, Server server) {
		clients.add(clientHandler);
		this.isPrivate = isPrivate;
		numClients = 1;
		ID = server.roomID;
		name = Integer.toString(ID);
		server.roomID++;
	}
	
	void addClient(ClientHandler clientHandler) {
		clientHandler.addObserver(this);
		clients.add(clientHandler);
		numClients++;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
	}
}
