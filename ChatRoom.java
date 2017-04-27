package assignment7;

import java.io.PrintWriter;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ChatRoom extends Observable implements Runnable, Observer {
	
	private List<ClientHandler> clients;
	private Scanner in;
	private PrintWriter out;
	
	ChatRoom(ClientHandler clientHandler, Server server) {
		clients.add(clientHandler);
		this.addObserver(server);
	}
	
	@Override
	public void run() {
		while (true) {
			if(in.hasNextLine()) {
				this.notifyObservers(in.nextLine());
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (this != o) {
			out.println(arg);
			out.flush();
		}		
	}
}
