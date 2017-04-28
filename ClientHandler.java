package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;

public class ClientHandler extends Observable implements Runnable, Observer  {
	
	private Server server;
	public BufferedReader in;
	public PrintWriter out;
	final public int ID;
	public String username;
	
	ClientHandler(Socket client, Server server) {
		this.server = server;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("PRINTOUT" + "Connected!");
		out.println("PRINTOUT" + "Welcome to the messaging server!");
		ID = server.clientID;
		username = Integer.toString(ID);
		server.fetchClientID.put(username, ID);
		out.println("CHANGEUN" + username);
		printAvailable();
		out.println("PRINTOUT" + "Enter \"help\" for a list of commands");
		out.flush();
	}
	
	private void changeUsername(String newUsername) {
		if (server.fetchClientID.containsKey(newUsername) && server.fetchClientID.get(newUsername) != ID) {
			out.println("PRINTOUT" + "Failed: username\"" + newUsername + "\" is taken");
			Platform.runLater(new Runnable() {                          
                @Override
                public void run() {
                	server.control.serverText.appendText("Failed: Client " + ID + " attempted to change username to \"" + newUsername + "\"\n");
                }
            });
		}
		else {
			server.fetchClientID.remove(username);
			server.fetchClientID.put(newUsername, ID);
			username = newUsername;
			out.println("CHANGEUN" + username);
			Platform.runLater(new Runnable() {                          
                @Override
                public void run() {
                	server.control.serverText.appendText("Client " + ID + " has changed username to \"" + username + "\"\n");
                }
            });
		}
	}
	
	private void printAvailable() {
		out.println("PRINTOUT" + "People Online");
		if (server.clients.isEmpty()) {
			out.println("PRINTOUT	no one is online");
		}
		else {
			for (ClientHandler client: server.clients) {
				if (client != this) {
					out.println("PRINTOUT	" + client.username);
				}
				else {
					if (server.clients.size() == 1) {
						out.println("PRINTOUT	no one is online");
					}
				}
			}
		}
		out.println("PRINTOUT" + "Chatrooms");
		if (server.chatrooms.isEmpty()) {
			out.println("PRINTOUT" + "	no chat rooms are available");
		}
		else {
			for (Chatroom room: server.chatrooms) {
				if (!room.isPrivate) {
					out.println("PRINTOUT	" + room.name + "(" + room.numClients + ")");
				}
			}
		}
	}
	private void printCommands() {
		out.println("PRINTOUT	Change your username - \"username MyUsername\"");
		out.println("PRINTOUT	Obtain an updated list of chatrooms and users - \"update\"");
		out.println("PRINTOUT	Create a new chatroom - \"chatroom ChatroomName\"");
		out.println("PRINTOUT	Connect to an existing chatroom - \"enter ChatroomName\"");
		out.println("PRINTOUT	Chat with an existing - \"connect Username\"");
		out.println("PRINTOUT	View list of commands again - \"help\"");
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String line = in.readLine();
				if (line.startsWith("SERVECMD")) {
					String args[] = line.substring(8).split("\\s+");
					/* commands are:
			         * username MyUsername
			         * update
			         * chatroom ChatroomName
			         * enter ChatroomName
			         * connect UserName
			         * help             
			         */
					if (args[0].equals("username")) {
						if(args.length == 1) {
							out.println("CHANGEUN");
						}
						else {
							changeUsername(args[1]);
						}
						out.flush();
					}
					else if (args[0].equals("update")) {
						printAvailable();
						out.flush();
					}
					else if (args[0].equals("chatroom")) {
						// cont
						out.flush();
					}
					else if (args[0].equals("enter")) {
						// cont
						out.flush();
					}
					else if (args[0].equals("connect")) {
						if(args.length == 1) {
							out.println("PRINTOUT" + "Specify a username after \"connect\"");
						}
						else {
							if (server.fetchClientID.containsKey(args[1])) {
								int connectID = server.fetchClientID.get(args[1]);
								if (connectID != ID) {
									Chatroom chatroom = new Chatroom(this, true, server);
									ClientHandler other = server.clients.get(connectID);
									chatroom.addClient(other);
									server.chatrooms.add(chatroom);
									addObserver(chatroom);
									Platform.runLater(new Runnable() {                          
						                @Override
						                public void run() {
						                	server.control.serverText.appendText("Chatroom " + chatroom.ID + " has been created\n");	
						                }
						            });	
									if (chatroom.ID < 10) {
										out.println("MAKERM" + "0" + chatroom.ID);
										other.out.println("MAKERM" + "0" + chatroom.ID);
									}
									else {
										out.println("MAKERM" + chatroom.ID);
										other.out.println("MAKERM" + chatroom.ID);
									}
								}
								else {
									out.println("PRINTOUT" + "Cannot connect to yourself");
								}
							}
							else {
								out.println("PRINTOUT" + "\"" + args[1] + "\": no such user");
							}
						}
						out.flush();
					}
					else if (args[0].equals("help")) {
						printCommands();
						out.flush();
					}
					else {
						out.println("PRINTOUT" + args[0] + ": invalid command");
						out.flush();
					}
					
				}
				else if (line.startsWith("SMSGRM")) {
					
					out.flush();
				}
				else if (line.startsWith("SMSGCL")) {
					
					out.flush();
				}
				else {
					throw new IOException();
				}
			} catch (IOException e) {
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	server.control.serverText.appendText("Failed: Client message or request could not be received\n");	
	                }
	            });
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
