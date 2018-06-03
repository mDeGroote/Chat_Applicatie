/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Marc
 */
public class DatabaseServer extends Application{
    private int portNumber = 1099;
    private String bindingName = "Database";
    private Registry registry;
    private Database database = new Database();
    
    public static void main(String[] args) {        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            registry = LocateRegistry.createRegistry(portNumber);
        }
        catch(Exception ex) {
            registry = LocateRegistry.getRegistry(portNumber);
        }
        registry.rebind(bindingName, database);
        System.out.println("alles is gelukt");
    }
}
