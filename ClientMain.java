package assignment7;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage clientStage) {
		Controller control = new Controller();
		clientStage.setScene(new Scene(control.mainPane, 500, 500));
		clientStage.setTitle("Client");
		clientStage.show();
		
		Client client = new Client(control, 6000);
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
	    
	}
	
	public static void main(String[] args) {
		Application.launch();

	}
	
}