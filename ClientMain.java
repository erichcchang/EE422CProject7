/* EE422C Project 7 submission by
 * Ho-chang Chang
 * hc23882
 * 16220
 * Slip days used: <0>
 * Spring 2017
 */

package assignment7;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage clientStage) {
		Controller control = new Controller();
		clientStage.setScene(new Scene(control.mainPane, 500, 500));
		clientStage.setTitle("Client");
		clientStage.show();
		control.serverText.setText("Enter host to connect to:\n");
		control.commandLine.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent keyEvent) {
		        if (keyEvent.getCode() == KeyCode.ENTER)  {
		            String text = control.commandLine.getText();
		            Client client = new Client(control, text, 5000);
		            (new Service<Void>() {
		    	        @Override
		    	        public Task<Void> createTask() {
		    	            return new Task<Void>() {           
		    	                @Override
		    	                public Void call() throws Exception {
		    	                    client.run();
		    	                    return null;
		    	                }
		    	            };
		    	        }
		    	    }).start();
		            control.commandLine.setText("");
		            
		        }
		    }
		});
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
	
}