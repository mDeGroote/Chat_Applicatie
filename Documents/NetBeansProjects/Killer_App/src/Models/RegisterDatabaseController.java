/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Database.IDatabase;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc
 */
public class RegisterDatabaseController {
    private IDatabase database;
    private Registry registry;

    public RegisterDatabaseController() {
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            database = (IDatabase)registry.lookup("Database");
        } catch (RemoteException ex) {
            throw new RuntimeException();
        } catch (NotBoundException ex) {
            Logger.getLogger(RegisterDatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RegisterUser(String name, String username, String password) {
        try {
            database.RegisterUser(name, username, password);
        }
        catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException();
        }
    }
    
    
}
