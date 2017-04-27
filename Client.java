package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Client extends Observable implements Observer {
	
	Controller control;
	int portNum;
	
	private BufferedReader in;
	private PrintWriter out;
	
	Client(Controller control, int portNum) {
		this.control = control;
		this.portNum = portNum;
	}
	
	public void run() {
		try {
			control.serverText.appendText("Connecting to server ...\n");
			Socket client = new Socket("localhost", portNum);
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
			return;
		} catch (IOException e) {
			control.serverText.appendText("Failed: Port Unavailable\n");
			return;
		}	
		while (true) {
			try {
				String line = in.readLine();
				/* protocol is:
				 * PRINTOUT - print text to serverText
				 * CHANGEUN - change username
	             * MAKERM## - make chat room with assigned ID ##
	             * MAKECT## - start chat with client assigned ID ##
	             * CHATRM## - receive message from chat room with ID ## 
	             * CLIENT## - receive message from client ##
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
					int ID = Integer.parseInt(line.substring(6, 8));
					// cont
				}
				else if (line.startsWith("MAKERM")) {
					int ID = Integer.parseInt(line.substring(6, 8));
					// cont
				}
				else if (line.startsWith("MAKECT")) {
					int ID = Integer.parseInt(line.substring(6, 8));
					// cont
				}
            	else if (line.startsWith("CHATRM")) {
            		int ID = Integer.parseInt(line.substring(6, 8));
            		// cont
            	}
            	else if (line.startsWith("CLIENT")) {
            		int ID = Integer.parseInt(line.substring(6, 8));
            		// cont
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
