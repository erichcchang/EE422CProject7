/* EE422C Project 7 submission by
 * Ho-chang Chang
 * hc23882
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javafx.application.Platform;

public class ClientHandler extends Observable implements Runnable, Observer {
	
	Socket clientSocket;
	private Server server;
	public BufferedReader in;
	public PrintWriter out;
	final public int ID;
	public String username;
	public List<Chatroom> clientRooms;
	private static Random RNG = new Random();
	
	ClientHandler(Socket client, Server server) {
		clientSocket = client;
		this.server = server;
		clientRooms = new ArrayList<Chatroom>();
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("PRINTOUT" + "Connected!");
		out.println("PRINTOUT" + "Welcome to the messaging server!");
		ID = server.clientID;
		char[] charArray = new char[4];
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (RNG.nextInt(26000) % 26 + 97);
		}
		// may be taken
		username = new String(charArray);
		out.println("CHANGEUN" + username);
		printAvailable();
		out.println("PRINTOUT" + "Enter \"help\" for a list of commands");
		out.flush();
	}
	
	private void changeUsername(String newUsername) {
		if (server.fetchClientID.containsKey(newUsername) && server.fetchClientID.get(newUsername) != ID) {
			out.println("PRINTOUT" + "Failed: username\"" + newUsername + "\" is taken");
			out.flush();
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
			synchronized (this) {
				out.println("CHANGEUN" + username);
				out.flush();
				setChanged();
				notifyObservers();
			}
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
		int count = 0;
		for (ClientHandler client: server.clients) {
			if (client != this) {
				out.println("PRINTOUT	" + client.username);
				count++;
			}
		}
		if (count == 0) {
			out.println("PRINTOUT	no one is online");
		}
		out.println("PRINTOUT" + "Chatrooms");
		count = 0;
		for (Chatroom room: server.chatrooms) {
			if (!room.isPrivate) {
				out.println("PRINTOUT	" + room.name + " (" + room.numClients + ")");
				count++;
			}
		}
		if (count == 0) {
			out.println("PRINTOUT" + "	no chat rooms are available");
		}
		out.flush();
	}
	
	private void enter(String name) {
		if (server.fetchChatroomID.containsKey(name)) {
			Chatroom chatroom = server.chatrooms.get(server.fetchChatroomID.get(name));
			if (chatroom.isPrivate) {
				out.println("PRINTOUT" + "No chatroom named \"" + name + "\"");
				out.flush();
			}
			else {
				chatroom.addClient(this);
				addObserver(chatroom);
				chatroom.sendMessage(username + " has entered the chatroom");
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	server.control.serverText.appendText("Client " + ID + " has entered chatroom " + chatroom.ID + "\n");	
	                }
	            });
			}
		}
		else {
			out.println("PRINTOUT" + "No chatroom named \"" + name + "\"");
			out.flush();
		}
	}
	
	private void connectTo(String user) {
		if (server.fetchClientID.containsKey(user)) {
			int connectID = server.fetchClientID.get(user);
			if (connectID != ID) {
				Chatroom chatroom = new Chatroom(this, true, Integer.toString(server.roomID), server);
				ClientHandler other = server.clients.get(connectID);
				chatroom.addClient(other);
				clientRooms.add(chatroom);
				addObserver(chatroom);
				server.fetchChatroomID.put(chatroom.name, chatroom.ID);
				server.chatrooms.add(chatroom);
				server.roomID++;
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	server.control.serverText.appendText("Chatroom " + chatroom.ID + " has been created privately between " + "Clients " + ID + " and " + other.ID + "\n");
	                }
	            });	
				chatroom.sendMessage(username + " has connected");
				chatroom.sendMessage(other.username + " has connected");
				out.flush();
			}
			else {
				out.println("PRINTOUT" + "Cannot connect to yourself");
				out.flush();
			}
		}
		else {
			out.println("PRINTOUT" + "\"" + user + "\": no such user");
			out.flush();
		}
	}
	
	private void createChatroom(String name) {
		if (server.fetchChatroomID.containsKey(name)) {
			out.println("PRINTOUT" + "Failed: chatroom name \"" + name + "\" is taken");
			out.flush();
		}
		else {
			Chatroom chatroom = new Chatroom(this, false, name, server);
			clientRooms.add(chatroom);
			addObserver(chatroom);
			server.fetchChatroomID.put(chatroom.name, chatroom.ID);
			server.chatrooms.add(chatroom);
			chatroom.refreshAll();
			server.roomID++;
			Platform.runLater(new Runnable() {                          
                @Override
                public void run() {
                	server.control.serverText.appendText("Chatroom " + chatroom.ID + " has been created\n");
                	server.control.serverText.appendText("Client " + ID + " has entered chatroom " + chatroom.ID + "\n");
                }
            });
			chatroom.sendMessage(username + " has connected to the chatroom");
		}
	}
	
	private void printCommands() {
		out.println("PRINTOUT	Change your username - \"username MyUsername\"");
		out.println("PRINTOUT	Obtain an updated list of chatrooms and users - \"update\"");
		out.println("PRINTOUT	Create a new chatroom - \"chatroom ChatroomName\"");
		out.println("PRINTOUT	Connect to an existing chatroom - \"enter ChatroomName\"");
		out.println("PRINTOUT	Chat with an existing - \"connect Username\"");
		out.println("PRINTOUT	View list of commands again - \"help\"");
		out.flush();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String line = in.readLine();
				if (line.startsWith("SERVECMD")) {
					String args[] = line.substring(8).split("\\s+");
					/* receive commands are:
			         * username MyUsername
			         * update
			         * chatroom ChatroomName
			         * enter ChatroomName
			         * connect UserName
			         * help             
			         */
					/* send commands are:
					 * PRINTOUT - print out texts to client's serverText
					 * CHANGEUN - change username
		             * MAKERM## - make chat room with assigned ID ##
		             * MAKECT## - start chat with client assigned ID ##
		             * CHATRM## - send message from chatroom with ID ## 
		             */
					if (args[0].equals("username")) {
						if(args.length == 1) {
							out.println("CHANGEUN");
							out.flush();
						}
						else {
							changeUsername(args[1]);
						}
					}
					else if (args[0].equals("update")) {
						printAvailable();
					}
					else if (args[0].equals("chatroom")) {
						if (args.length == 1) {
							out.println("PRINTOUT" + "Specify a chatroom name with \"chatroom\"");
							out.flush();
						}
						else {
							createChatroom(args[1]);
						}
					}
					else if (args[0].equals("enter")) {
						if (args.length == 1) {
							out.println("PRINTOUT" + "Specify a chatroom with \"enter\"");
							out.flush();
						}
						else {
							enter(args[1]);
						}
					}
					else if (args[0].equals("connect")) {
						if (args.length == 1) {
							out.println("PRINTOUT" + "Specify a username with \"connect\"");
							out.flush();
						}
						else {
							connectTo(args[1]);
						}
					}
					else if (args[0].equals("help")) {
						printCommands();
					}
					else {
						out.println("PRINTOUT" + args[0] + ": invalid command");
						out.flush();
					}
					
				}
				else if (line.startsWith("SMSGRM")) {
					int roomID = Integer.parseInt(line.substring(6, 8));
					server.chatrooms.get(roomID).sendMessage(this.username + ": " + line.substring(8));
				}
				else {
					throw new IOException();
				}
			} catch (IOException e) {
				//disconnect
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	server.control.serverText.appendText("Client " + ID + " has disconnected\n");
	                }
	            });
				return;
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		out.println(arg);
		out.flush();
		
	}

}
