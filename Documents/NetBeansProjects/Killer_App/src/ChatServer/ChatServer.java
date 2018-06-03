/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;

import Models.PublisherController;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Marc
 */
public class ChatServer extends Application{
    private int portNumber = 1099;
    private String bindingName = "ChatServer";
    private Registry registry;
    private PublisherController publisher;
    
    public static void main(String[] args) {        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws RemoteException {
        publisher = new PublisherController();
        try {
            registry = LocateRegistry.createRegistry(portNumber);
        }
        catch(Exception ex) {
            registry = LocateRegistry.getRegistry(portNumber);
        }
        registry.rebind(bindingName, publisher);
        System.out.println("Alles is gelukt");
    }
}
