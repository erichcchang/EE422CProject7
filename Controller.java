package assignment7;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Controller {
	
	public AnchorPane mainPane;
	public TextArea serverText;
	public TextField commandLine;	
	public TabPane tabs;
	
	Controller() {
		mainPane = new AnchorPane();
		tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
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
	}
	
}
