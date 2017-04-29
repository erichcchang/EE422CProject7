/* EE422C Project 7 submission by
 * Ho-chang Chang
 * hc23882
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment7;

import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class ClientRoom {
	
	public boolean isPrivate;
	public int numClients;
	final public int ID;
	public String name;
	private List<String> users;
	
	public Tab tab;
	public TextArea chatLog;
	public TextArea chatLine;
	public Label info;
	public Button sendButton;

	ClientRoom(int chatroomID, int intPrivate, int numClients, String roomName, List<String> usernames, Client client) {
		if (intPrivate == 1) {
			isPrivate = true;
		}
		else {
			isPrivate = false;
		}
		ID = chatroomID;
		name = roomName;
		Platform.runLater(new Runnable() {                          
            @Override
            public void run() {
            	createRoom();
            	set(numClients, usernames, client);
            	client.control.tabs.getTabs().add(tab);
        		client.control.tabs.getSelectionModel().select(tab);
        		sendButton.setOnAction(new EventHandler<ActionEvent>() {
    				@Override 
    			    public void handle(ActionEvent e) {
    					String lines[] = chatLine.getText().split("\n");
    					chatLine.setText("");
    					for (int i = 0; i < lines.length; i++) {
    						if (ID < 10) {
    							client.out.println("SMSGRM" + "0" + ID + lines[i]);
    						}
    						else {
    							client.out.println("SMSGRM" + ID + lines[i]);
    						}
    					}
    					client.out.flush();
    			    }
    		});
            }
        });
	}
	
	void set(int clientCount, List<String> usernames, Client client) {
		users = usernames;
		numClients = clientCount;
		if (isPrivate) {
			Platform.runLater(new Runnable() {                          
	            @Override
	            public void run() {
	            	String title = name;
	            	String subTitle = users.get(0) + ", " + users.get(1);
        			if (users.get(0).equals(client.username)) {
        				title = users.get(1);
        			}
        			else {
        				title = users.get(0);
        			}
	            	tab.setText(title);
	            	info.setText(subTitle);
	            }
	        });
		}
		else {
			Platform.runLater(new Runnable() {                          
	            @Override
	            public void run() {
	            	String subTitle = usernames.get(0);
	            	for (int i = 1; i < users.size(); i++) {
	            		subTitle = subTitle + ", " + usernames.get(i);
	            	}
	            	tab.setText(name + " (" + numClients + ")");
	            	info.setText(subTitle);
	            }
	        });
		}
	}
	
	private void createRoom() {
		BorderPane room = new BorderPane();
		tab = new Tab("", room);
		room.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		BorderPane centerBorder = new BorderPane();
		BorderPane bottomBorder = new BorderPane();
		room.setCenter(centerBorder);
		room.setBottom(bottomBorder);
		centerBorder.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
		bottomBorder.setPadding(new Insets(1.0, 0.0, 0.0, 0.0));
		chatLog = new TextArea();
		chatLine = new TextArea();
		sendButton = new Button("Send");
		info = new Label();
		centerBorder.setTop(info);
		BorderPane.setMargin(info, new Insets(0.0, 0.0, 10.0, 0.0));
		BorderPane.setAlignment(info, Pos.CENTER);
		centerBorder.setCenter(chatLog);
		bottomBorder.setCenter(chatLine);
		BorderPane.setMargin(chatLine, new Insets(0.0, 5.0, 0.0, 0.0));
		bottomBorder.setRight(sendButton);
		BorderPane.setMargin(sendButton, new Insets(0.0, 0.0, 0.0, 5.0));
		chatLog.setEditable(false);
	}
	
}
