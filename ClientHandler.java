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
	private BufferedReader in;
	private PrintWriter out;
	public String username;
	
	ClientHandler(Socket client, Server server) {
		this.server = server;
		addObserver(server);
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("PRINTOUT" + "Connected!");
		out.println("PRINTOUT" + "Welcome to the messaging server!");
		// print available chatrooms and users
		out.println("PRINTOUT" + "try one the following commands:");
		printCommands();
		out.flush();
	}
	
	private void printCommands() {
		out.println("PRINTOUT" + "Change your username - \"username MyUsername\"");
		out.println("PRINTOUT" + "Obtain an updated list of chatrooms and users - \"update\"");
		out.println("PRINTOUT" + "Create a new chatroom - \"chatroom ChatroomName\"");
		out.println("PRINTOUT" + "Connect to an existing chatroom - \"enter ChatroomName\"");
		out.println("PRINTOUT" + "Chat with an existing - \"connect Username\"");
		out.println("PRINTOUT" + "View list of commands again - \"help\"");
		/* commands are:
         * username MyUsername
         * update
         * chatroom ChatroomName
         * enter ChatroomName
         * connect UserName
         * help             
         */
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String line = in.readLine();
				if (line.startsWith("SERVECMD")) {
					String args[] = line.substring(8).split("\\s+");
					if (args[0].equals("username")) {
						
					}
					else if (args[0].equals("update")) {
						// cont
					}
					else if (args[0].equals("chatroom")) {
						// cont
					}
					else if (args[0].equals("enter")) {
						// cont
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
					
				}
				else if (line.startsWith("SMSGCL")) {
					
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
