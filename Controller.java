package assignment7;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Controller {

	//private Map<Integer, >
	public AnchorPane mainPane;
	public TextArea serverText;
	public TextField commandLine;	
	private TabPane tabs;
	
	VBox onlineClients;
	VBox chatRooms;
	
	Controller() {
		mainPane = new AnchorPane();
		TabPane tabs = new TabPane();
		mainPane.getChildren().add(tabs);
		AnchorPane.setTopAnchor(tabs, 0.0);
		AnchorPane.setRightAnchor(tabs, 0.0);
		AnchorPane.setBottomAnchor(tabs, 0.0);
		AnchorPane.setLeftAnchor(tabs, 0.0);
		BorderPane formatOptions = new BorderPane();
		Tab optionsTab = new Tab("Options", formatOptions);
		tabs.getTabs().add(optionsTab);
		formatOptions.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		BorderPane centerBorder = new BorderPane();
		BorderPane bottomBorder = new BorderPane();
		formatOptions.setCenter(centerBorder);
		formatOptions.setBottom(bottomBorder);
		centerBorder.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
		bottomBorder.setPadding(new Insets(1.0, 0.0, 0.0, 0.0));
		serverText = new TextArea();
		commandLine = new TextField();
		Label serverTitle = new Label("Server");
		centerBorder.setTop(serverTitle);
		BorderPane.setMargin(serverTitle, new Insets(0.0, 0.0, 10.0, 0.0));
		BorderPane.setAlignment(serverTitle, Pos.CENTER);
		centerBorder.setCenter(serverText);
		bottomBorder.setCenter(commandLine);
		serverText.setEditable(false);
		
		/*
		BorderPane topBorder = new BorderPane();
		BorderPane bottomBorder = new BorderPane();
		formatOptions.setTop(topBorder);
		formatOptions.setBottom(bottomBorder);
		Label topLabel = new Label("Who's Online");
		Label bottomLabel = new Label("Join a Chat Room");
		topBorder.setTop(topLabel);
		bottomBorder.setTop(bottomLabel);
		ScrollPane topScroll = new ScrollPane();
		ScrollPane bottomScroll = new ScrollPane();
		VBox topVBox = new VBox();
		VBox bottomVBox = new VBox();
		topScroll.setContent(topVBox);
		bottomScroll.setContent(bottomVBox);
		topBorder.setCenter(topScroll);
		bottomBorder.setCenter(bottomScroll);
		*/	
	}
	
	public Tab createRoom(String title, String participants) {
		BorderPane room = new BorderPane();
		Tab tab = new Tab(title, room);
		tabs.getTabs().add(tab);
		room.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		BorderPane centerBorder = new BorderPane();
		BorderPane bottomBorder = new BorderPane();
		room.setCenter(centerBorder);
		room.setBottom(bottomBorder);
		centerBorder.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
		bottomBorder.setPadding(new Insets(1.0, 0.0, 0.0, 0.0));
		TextArea chatLog = new TextArea();
		TextArea chatLine = new TextArea();
		Button sendButton = new Button("Send");
		Label serverTitle = new Label(participants);
		centerBorder.setTop(serverTitle);
		BorderPane.setMargin(serverTitle, new Insets(0.0, 0.0, 10.0, 0.0));
		BorderPane.setAlignment(serverTitle, Pos.CENTER);
		centerBorder.setCenter(chatLog);
		bottomBorder.setCenter(chatLine);
		bottomBorder.setRight(sendButton);
		chatLog.setEditable(false);
		return tab;
	}
	
}
