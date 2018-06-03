/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Database.IDatabase;
import java.io.Serializable;
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
public class LoginController implements Serializable{
    private Registry registry;
    private String bindingName = "Database";
    private IDatabase database;

    public LoginController() {
        try {
            this.registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            database = (IDatabase)registry.lookup(bindingName);
        } catch (RemoteException ex) {
            throw new RuntimeException();
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public User Login(String username, String password) {
        return database.Login(username, password);
    }
    
    
}
