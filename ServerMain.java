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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ServerMain extends Application {
	
	@Override
	public void start(Stage serverStage) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Controller control = new Controller();
		control.commandLine.setEditable(false);
		control.commandLine.setMouseTransparent(true);
		control.commandLine.setFocusTraversable(false);
		serverStage.setScene(new Scene(control.mainPane, 500, 500));
		serverStage.setX(screenBounds.getMinX() + 50);
		serverStage.setY(screenBounds.getMinY() + 50);
		serverStage.setTitle("Server");
		serverStage.show();
		Server server = new Server(control, 5000);
		(new Service<Void>() {
	        @Override
	        public Task<Void> createTask() {
	            return new Task<Void>() {           
	                @Override
	                public Void call() throws Exception {
	                    server.run();
	                    return null;
	                }
	            };
	        }
	    }).start();
	    
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
	
}
