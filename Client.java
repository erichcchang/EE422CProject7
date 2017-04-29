package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Client {
	
	Controller control;
	int portNum;
	
	private String host;
	public String username;
	public BufferedReader in;
	public PrintWriter out;
	public Map<Integer, ClientRoom> fetchRoom;
	
	Client(Controller control, String host, int portNum) {
		this.control = control;
		this.host = host;
		this.portNum = portNum;
		fetchRoom = new HashMap<Integer, ClientRoom>();
	}
	
	public void run() {
		try {		
			control.serverText.appendText("Connecting to server ...\n");
			@SuppressWarnings("resource")
			Socket client = new Socket(host, portNum);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
			control.commandLine.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
			        if (keyEvent.getCode() == KeyCode.ENTER)  {
			            String text = control.commandLine.getText();
			            text = "SERVECMD" + text;
			            out.println(text);
			            out.flush();
			            control.commandLine.setText("");
			            /* commands are:
			             * username MyUsername
			             * update
			             * chatroom ChatroomName
			             * enter ChatroomName
			             * connect UserName
			             * help             
			             */
			        }
			    }
			});
		} catch (UnknownHostException e) {
			control.serverText.appendText("Failed: Unknown Host\n");
			control.serverText.appendText("Enter host to connect to:\n");
			return;
		} catch (IOException e) {
			control.serverText.appendText("Failed: Port Unavailable\n");
			control.serverText.appendText("Enter host to connect to:\n");
			return;
		}	
		while (true) {
			try {
				String line = in.readLine();
				/* protocol is:
				 * PRINTOUT - print text to serverText
				 * CHANGEUN - change username
	             * MAKERM## - make chat room with assigned ID ##
	             * CHATRM## - receive message from chatroom with ID ## 
	             */
				if (line.startsWith("PRINTOUT")) {
					Platform.runLater(new Runnable() {                          
		                @Override
		                public void run() {
		                	control.serverText.appendText(line.substring(8) + "\n");
		                }
		            });
				}
				else if (line.startsWith("CHANGEUN")) {
					if (line.equals("CHANGEUN")) {
						Platform.runLater(new Runnable() {                          
			                @Override
			                public void run() {
			                	control.serverText.appendText("Your username is \"" + username + "\"\n");
			                }
			            });
					}
					else {
						username = line.substring(8);
						Platform.runLater(new Runnable() {                          
			                @Override
			                public void run() {
			                	control.serverText.appendText("Your username has been changed to \"" + username + "\"\n");
			                }
			            });
					}
				}
				else if (line.startsWith("MAKERM")) {
					String args[] = line.substring(6).split("\\s");
					int chatroomID = Integer.parseInt(args[0].substring(0, 2));
					int isPrivate = Integer.parseInt(args[0].substring(2));
					int numClients = Integer.parseInt(args[1]);
					String name = args[2];
					List<String> usernames = new ArrayList<String>();
					for (int i = 3; i < args.length; i++) {
						usernames.add(args[i]);
					}
					if (fetchRoom.containsKey(chatroomID)) {
						ClientRoom clientRoom = fetchRoom.get(chatroomID);
						clientRoom.set(numClients, usernames, this);
					}
					else {
						ClientRoom clientRoom = new ClientRoom(chatroomID, isPrivate, numClients, name, usernames, this);
						fetchRoom.put(chatroomID, clientRoom);
					}				
				}
            	else if (line.startsWith("CHATRM")) {
            		int ID = Integer.parseInt(line.substring(6, 8));
            		Platform.runLater(new Runnable() {                          
    	                @Override
    	                public void run() {
    	                	fetchRoom.get(ID).chatLog.appendText(line.substring(8) + "\n");
    	                }
    	            });
            	}
            	else {
            		throw new IOException();
            	}
			} catch (IOException e) {
				Platform.runLater(new Runnable() {                          
	                @Override
	                public void run() {
	                	control.serverText.appendText("Incoming read failed\n");
	                }
	            });
			}
		}
	}
	
}
